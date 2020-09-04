package cn.yiidii.openapi.base.exception;

import lombok.NoArgsConstructor;

/**
 * 基础异常类
 */
@NoArgsConstructor
public abstract class BaseException extends RuntimeException {

    public BaseException(String message) {
        super(message);
    }

}
