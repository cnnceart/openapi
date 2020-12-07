package cn.yiidii.openapi.unicommeituan.controller.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 中国联通登录Form类
 * </p>
 *
 * @author: YiiDii Wang
 * @create: 2020-12-02 21:20
 */
@Data
public class ChinaUnicomLoginForm {

    /**
     * 手机号码
     */
    @NotBlank(message = "请输入手机号码")
    @Pattern(regexp = "\\d{11}", message = "手机号码格式不正确")
    private String mobile;

    /**
     * 验证码
     */
    @NotBlank(message = "请输入验证码")
    @Pattern(regexp = "\\d{4,6}", message = "验证码格式不正确")
    private String password;

    /**
     * 图片验证码
     */
    private String userContent;

    /**
     * 图片验证码的imageId
     */
    private String imageId;

    /**
     * imei
     */
    private String imei;

    /**
     * 类型。0：实际；1：虚拟
     */
    private Integer type;

}
