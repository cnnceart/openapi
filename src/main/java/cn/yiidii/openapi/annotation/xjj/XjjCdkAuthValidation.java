package cn.yiidii.openapi.annotation.xjj;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * xjj cdk校验
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface XjjCdkAuthValidation {
    /**
     * 是否需要校验，默认不需要
     * @return
     */
    boolean isValidated() default false;
}
