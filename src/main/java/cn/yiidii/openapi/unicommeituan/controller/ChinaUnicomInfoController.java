package cn.yiidii.openapi.unicommeituan.controller;

import cn.yiidii.openapi.unicommeituan.controller.form.ChinaUnicomInfoFrom;
import cn.yiidii.openapi.unicommeituan.service.ChinaUnicomInfoService;
import cn.yiidii.openapi.unicommeituan.service.MeituanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/ChinaUnicomInfo")
@Api(tags = "中国联通星期五")
@Validated
@RequiredArgsConstructor
public class ChinaUnicomInfoController {

    private final ChinaUnicomInfoService chinaUnicomInfoService;
    private final MeituanService meituanService;

    @GetMapping("/all")
    @ApiIgnore
//    @ApiOperation(value = "获取所有联通账户信息")
    public Object getAllChinaUnicomInfo() {
        return chinaUnicomInfoService.getAllChinaUnicom();
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加手机号")
    public String addChinaUnicomInfo(@RequestBody @Valid ChinaUnicomInfoFrom chinaUnicomInfoFrom) {
        int row = chinaUnicomInfoService.addChinaUnicomInfo(chinaUnicomInfoFrom);
        if (row > 0) {
            return "添加成功";
        }
        return "添加失败";
    }

    @PostMapping("/meituan/test")
    @ApiOperation(value = "美团测试")
    public String meituanTest(@RequestBody @Valid ChinaUnicomInfoFrom chinaUnicomInfoFrom) {
        String msg = meituanService.meituanTest(chinaUnicomInfoFrom);
        return msg;
    }

    @DeleteMapping("/del/{phoneNum}")
    @ApiOperation(value = "删除手机号")
    public String delChinaUnicomInfo(@PathVariable @NotNull String phoneNum){
        int row = chinaUnicomInfoService.delChinaUnicomInfoByPhoneNum(phoneNum);
        if(row >=1 ){
            return String.format("删除手机号【phoneNum】成功！");
        }
        return String.format("删除手机号【phoneNum】失败！");
    }

}
