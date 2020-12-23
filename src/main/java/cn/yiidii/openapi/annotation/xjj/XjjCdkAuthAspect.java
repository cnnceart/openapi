package cn.yiidii.openapi.annotation.xjj;

import cn.hutool.core.date.DateUtil;
import cn.yiidii.openapi.entity.xjj.Cdk;
import cn.yiidii.openapi.base.exception.ServiceException;
import cn.yiidii.openapi.xjj.common.NewsType;
import cn.yiidii.openapi.xjj.service.ICdkService;
import cn.yiidii.openapi.xjj.service.INewsService;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;


@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class XjjCdkAuthAspect {

    private final ICdkService cdkService;
    private final HttpServletRequest request;
    private final INewsService newsService;

    @Before(value = "@annotation(cn.yiidii.openapi.annotation.xjj.XjjCdkAuthValidation)")//已注解 @ApiLimitValidation 为切点
    public void verifyApiLimit(JoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();//注解的方法
        XjjCdkAuthValidation xjjCdkAuthValidation = method.getAnnotation(XjjCdkAuthValidation.class);//注解
        // 没有注解或者注解为false，不校验
        if (Objects.isNull(xjjCdkAuthValidation) || !xjjCdkAuthValidation.isValidated()) {
            return;
        }

        String cdkStr = request.getParameter("cdk");
        Cdk cdk = cdkService.getCdkByCdk(cdkStr);

        if (Objects.isNull(cdk)) {
            throw new ServiceException("无效的cdk~");
        }

        Date now = new Date();
        Date expireTime = cdk.getExpireTime();
        if (!expireTime.after(now)) {
            String msg = String.format("你的cdk: %s 授权时间已经结束【%s】", cdkStr, DateUtil.formatDateTime(expireTime));
            log.info(msg);
            throw new ServiceException(msg);
        }
        newsService.addNews(NewsType.CDK_USE.getType(), "正在观看xnm");
    }


}
