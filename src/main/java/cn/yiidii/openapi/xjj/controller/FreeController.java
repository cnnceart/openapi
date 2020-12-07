package cn.yiidii.openapi.xjj.controller;

import cn.yiidii.openapi.base.exception.ServiceException;
import cn.yiidii.openapi.common.util.HttpClientUtil;
import cn.yiidii.openapi.common.util.IPUtil;
import cn.yiidii.openapi.common.util.dto.HttpClientResult;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 福利接口
 * </p>
 *
 * @author: YiiDii Wang
 * @create: 2020-11-27 12:42
 */
@Slf4j
@RestController
@RequestMapping("free")
public class FreeController {

    @Autowired
    private HttpClientUtil httpClientUtil;
    @Autowired
    private IPUtil ipUtil;
    @Autowired
    private HttpServletRequest request;

    @GetMapping("/{id}")
    public Object getFreeById(@NotNull(message = "id不能为空") @PathVariable Integer id,
                              @RequestParam(required = false) String ignoreTitle,
                              @RequestParam(required = false) String ignoreKey) {
        Map<String, String> params = new HashMap<>(16);
        params.put("id", String.valueOf(id));
        HttpClientResult resp = null;
        try {
            resp = httpClientUtil.doWWWFormUrlencodePost("https://1008610010.yohui.vip/index.php/Api/LiveApi/getLivelist", null, params);
        } catch (Exception e) {
            throw new ServiceException(String.format("接口异常: %s", e.getMessage()));
        }
        int code = resp.getCode();
        if (code != 200) {
            throw new ServiceException("接口异常: 状态码不等于200");
        }

        List<String> ignoreTitleList = new ArrayList<>();
        if (StringUtils.isNotBlank(ignoreTitle)) {
            String[] split = StringUtils.split(ignoreTitle, ",");
            ignoreTitleList = CollectionUtils.arrayToList(split);
        }

        List<String> ignoreKeyList = new ArrayList<>();
        if (StringUtils.isNotBlank(ignoreKey)) {
            String[] split = StringUtils.split(ignoreKey, ",");
            ignoreKeyList = CollectionUtils.arrayToList(split);
        }

        JSONObject respJo = JSONObject.parseObject(resp.getContent());
        JSONArray data = respJo.getJSONArray("data");
        JSONArray finalData = new JSONArray();
        for (Object o : data) {
            JSONObject jo = (JSONObject) o;
            String title = jo.getString("title");
            if (isIgnoreTitle(title, ignoreTitleList)) {
                continue;
            }
            for (String key : ignoreKeyList) {
                jo.remove(key);
            }
            finalData.add(jo);
        }

        String ip = ipUtil.getIpAddr(request);
        String location = ipUtil.getLocationByIp(ip);
        log.info("{}【{}】正在观看福利id:{}", ip, location, id);

        return finalData;
    }


    private boolean isIgnoreTitle(String title, List<String> ignoreTitleList) {
        if (CollectionUtils.isEmpty(ignoreTitleList)) {
            return false;
        }
        for (String t : ignoreTitleList) {
            if (StringUtils.containsIgnoreCase(title, t)) {
                return true;
            }
        }
        return false;
    }
}
