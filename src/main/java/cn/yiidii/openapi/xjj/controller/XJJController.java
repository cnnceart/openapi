package cn.yiidii.openapi.xjj.controller;

import cn.hutool.core.date.DateUtil;
import cn.yiidii.openapi.annotation.xjj.XjjCdkAuthValidation;
import cn.yiidii.openapi.annotation.xjj.XjjLogAnnotation;
import cn.yiidii.openapi.base.exception.ServiceException;
import cn.yiidii.openapi.entity.xjj.Cdk;
import cn.yiidii.openapi.base.vo.Result;
import cn.yiidii.openapi.entity.xjj.News;
import cn.yiidii.openapi.entity.xjj.XjjUsageData;
import cn.yiidii.openapi.xjj.controller.vo.CdkVo;
import cn.yiidii.openapi.xjj.service.*;
import cn.yiidii.openapi.xjj.service.dto.XJJInfo;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yiidii Wang
 * @desc xjj控制器（包含了cdk）
 */
@RestController
@Api(tags = "小奶猫解析")
@RequestMapping("xjj")
@Slf4j
public class XJJController {
    @Autowired
    private IXjjService IXJJService;
    @Autowired
    private ICdkService cdkService;
    @Autowired
    private ICdkPromoteService cdkPromoteService;
    @Autowired
    private IXjjLogService xjjLogService;
    @Autowired
    private INewsService newsService;

    @GetMapping("singleVipRoom")
    @ApiOperation(value = "获取单个收费房")
    @XjjLogAnnotation
    @XjjCdkAuthValidation(isValidated = true)
    public Object getSingleVIPRoom(@RequestParam String token,
                                   @RequestParam String roomId,
                                   @RequestParam("cdk") String cdkStr) {
        XJJInfo info = IXJJService.getSingleVipRoomAddr(token, roomId);
        JSONObject jo = new JSONObject();
        jo.put("token", token);
        jo.put("roomId", roomId);
        jo.put("addr", info.getAddr());
        jo.put("txTime", DateUtil.formatDateTime(new Date(info.getTxTime())));
        Cdk cdk = cdkService.getCdkByCdk(cdkStr);
        // 邀请
        cdkService.add10Day4Promote(cdk);
        String remark = cdk.getRemark();
        log.info("cdk: {}[{}]获取单个收费房[{}]", cdkStr, StringUtils.isBlank(remark) ? "--" : remark, roomId);
        return Result.success(jo, String.format("到期时间: %s", DateUtil.formatDateTime(cdk.getExpireTime())));
    }

    @GetMapping("vipRoomList")
    @ApiOperation(value = "获取收费房列表")
    @XjjCdkAuthValidation(isValidated = true)
    @XjjLogAnnotation
    public Object getVIPRoomList(@RequestParam String token,
                                 @RequestParam("cdk") String cdkStr) {
        List<XJJInfo> infoList = IXJJService.getVipRoomList(token);
        Cdk cdk = cdkService.getCdkByCdk(cdkStr);
        // 邀请
        cdkService.add10Day4Promote(cdk);
        String remark = cdk.getRemark();
        log.info("cdk: {}[{}]拿了两卷纸巾, 正准备撸啊撸啊撸~", cdkStr, StringUtils.isBlank(remark) ? "--" : remark, cdkStr);
        return Result.success(infoList, String.format("到期时间: %s", DateUtil.formatDateTime(cdk.getExpireTime())));
    }

    @GetMapping("cdk/generate")
    @ApiOperation(value = "注册生成cdk(默认0小时)")
    public Object invite(@RequestParam(value = "cdkId") @NotNull(message = "邀请cdk不能为空") String cdkPromoteId) {
        Cdk cdkExist = cdkService.getCdkById(cdkPromoteId);
        if (Objects.isNull(cdkExist)) {
            throw new ServiceException("邀请cdk不存在~");
        }
        Date expireTime = DateUtil.offsetHour(new Date(), 0);
        String cdkNew = cdkService.addCdk(expireTime);
        cdkPromoteService.addCdkPromote(cdkNew, cdkExist.getCdk());
        return cdkService.getCdkByCdk(cdkNew);
    }

    @GetMapping("cdk/usage")
    @ApiOperation(value = "接口使用情况")
    public Object getUsage(
            @RequestParam(value = "cdk") @NotNull(message = "cdk不能为空") String cdk) {

        List<XjjUsageData> xjjUsageDataList = xjjLogService.getXjjUsage(cdk);
        List<Integer> hourList = xjjUsageDataList.stream().map(XjjUsageData::getHour).collect(Collectors.toList());
        List<Long> countList = xjjUsageDataList.stream().map(XjjUsageData::getCount).collect(Collectors.toList());
        JSONObject jo = new JSONObject();
        jo.put("x", hourList);
        jo.put("y", countList);
        return jo;
    }

    @GetMapping("cdk/info")
    @ApiOperation(value = "根据id获取cdk信息")
    public Object getCdkInfo(@RequestParam(value = "cdkId", required = false) @NotNull(message = "cdkId不能为空") String cdkStr) {
        Cdk cdk = cdkService.getCdkByCdk(cdkStr);
        if (Objects.isNull(cdk)) {
            return null;
        }
        CdkVo cdkVo = new CdkVo();
        BeanUtils.copyProperties(cdk, cdkVo);
        return cdkVo;
    }

    @GetMapping("news")
    @ApiOperation(value = "获取最新动态")
    public Object getLatestNews(@RequestParam(value = "topN", required = false) Integer topN) {
        if(Objects.isNull(topN)){
            topN = 10;
        }

        List<News> topNNews = newsService.getTopNNews(topN);
        return topNNews;
    }


    // ===================== 管理员自用api, 不对外公布 =====================
    @PostMapping("cdk/add")
    @ApiOperation(value = "添加一个cdk")
    @ApiIgnore
    public Object addCdk(@RequestParam Integer hour) {
        Date expireTime = DateUtil.offsetHour(new Date(), hour);
        String cdkStr = cdkService.addCdk(expireTime);
        return cdkService.getCdkByCdk(cdkStr);
    }

}
