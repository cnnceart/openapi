package cn.yiidii.openapi.advice;

import cn.yiidii.openapi.base.vo.Result;
import cn.yiidii.openapi.base.vo.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    /**
     * 处理请求参数格式错误 @RequestBody上 validate
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(","));
        return Result.error(ResultCodeEnum.ILLEGAL_ARGUMENT_ERROR, message);
    }

    /**
     * 处理请求参数格式错误 @RequestParam上validate
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Result constraintViolationExceptionHandler(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
        return Result.error(ResultCodeEnum.ILLEGAL_ARGUMENT_ERROR, message);
    }

    /**
     * 处理@Valid 验证路径中请求实体校验失败后抛出的异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result<Object> bindExceptionHandler(BindException e) {
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(","));
        return Result.error(ResultCodeEnum.ILLEGAL_ARGUMENT_ERROR, message);
    }

    @ExceptionHandler(Exception.class)
    public Result handleEx(Exception ex) {
        //log.error("{}", ex);
        return Result.error("500000", ex.getMessage());
    }

}
