package cn.yiidii.openapi.xjj.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum NewsType {

    CDK_PROMOTE(1,"cdk邀请"),
    CDK_USE(2,"cdk使用");

    private Integer type;
    private String desc;

}
