package cn.yiidii.openapi.unicommeituan.controller.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 营业厅请求
 * </p>
 *
 * @author: YiiDii Wang
 * @create: 2020-11-30 21:53
 */
@Data
public class TelecomForm {

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = "^1[3|4|5|6|7|8|9][0-9]\\d{4,8}$", message = "手机号格式错误")
    public String mobile;
    /**
     * cookie
     */
    @NotNull(message = "Cookie不能为空")
    private String cookie;

}
