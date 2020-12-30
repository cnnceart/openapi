package cn.yiidii.openapi.smsbomb.controller;

import cn.yiidii.openapi.base.vo.Result;
import cn.yiidii.openapi.common.util.RedisUtil;
import cn.yiidii.openapi.smsbomb.service.ISmsBombService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *
 * </p>
 *
 * @author: YiiDii Wang
 * @create: 2020-12-28 22:53
 */
@RestController
@RequestMapping("smsBomb")
@RequiredArgsConstructor
@Api(tags = "短信Bomb")
@Validated
public class SmsBombController {

    private final ISmsBombService smsBombService;
    private final RedisUtil redisUtil;

    @GetMapping("boom")
    @ApiOperation(value = "Boom Boom Boom")
    public Result boom(@RequestParam(required = false) @NotBlank(message = "请输入手机号码") @Pattern(regexp = "\\d{11,}", message = "手机号格式不正确") String mobile) {
        smsBombService.bomb(mobile);
        return Result.success("成功");
    }

    @GetMapping("black")
    public Result black(@RequestParam(required = false) @NotBlank(message = "请输入手机号码") @Pattern(regexp = "\\d{11,}", message = "手机号格式不正确") String mobile) {
        long count = redisUtil.sSet("blackMobile", mobile);
        if (count > 0) {
            return Result.success("成功");
        }
        return Result.error();
    }

    @GetMapping("white")
    public Result white(@RequestParam(required = false) @NotBlank(message = "请输入手机号码") @Pattern(regexp = "\\d{11,}", message = "手机号格式不正确") String mobile) {
        long count = redisUtil.setRemove("blackMobile", mobile);
        if (count > 0) {
            return Result.success("成功");
        }
        return Result.error();
    }

    @GetMapping("curr")
    public Result curr() {
        Object mobileCache = redisUtil.get("mobileCache");
        return Result.success(mobileCache, "");
    }

}
