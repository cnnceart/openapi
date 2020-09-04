package cn.yiidii.openapi.advice;

import cn.yiidii.openapi.base.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局处理响应体
 */
@RestControllerAdvice
@Slf4j
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice {
    /**
     * 需要忽略的地址
     */
    private String[] ignores = new String[]{
            //过滤swagger相关的请求的接口，不然swagger会提示base-url被拦截
            "/swagger-resources",
            "/v2/api-docs"
    };

    /**
     * 判断url是否需要拦截
     */
    private boolean ignoring(String uri) {
        for (String string : ignores) {
            if (uri.contains(string)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object obj, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest request, ServerHttpResponse serverHttpResponse) {

        //判断url是否需要拦截
        if (this.ignoring(request.getURI().toString())) {
            return obj;
        }

        //如果返回的数据是ResultObjectModel、Byte类型则不进行封装
        if (obj instanceof Result || obj instanceof Byte) {
            return obj;
        }

        return getWrapperResponse(request, obj);
    }

    private Result getWrapperResponse(ServerHttpRequest request, Object data) {
        return Result.success(data);
    }
}
