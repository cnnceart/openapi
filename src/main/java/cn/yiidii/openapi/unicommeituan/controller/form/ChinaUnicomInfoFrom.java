package cn.yiidii.openapi.unicommeituan.controller.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * ChinaUnicom表单类
 */
@Data
public class ChinaUnicomInfoFrom {

    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = "^1[3|4|5|6|7|8|9][0-9]\\d{4,8}$", message = "手机号格式错误")
    private String phoneNum;
    @NotNull(message = "Cookie不能为空")
    private String cookie;
    private String scKey;
}
