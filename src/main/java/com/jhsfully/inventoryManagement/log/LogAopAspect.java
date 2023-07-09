package com.jhsfully.inventoryManagement.log;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class LogAopAspect {

    @Value("${log.save-get-log}")
    private boolean is_getSave;
    private final LogService logService;

    @Pointcut("execution(* com.jhsfully.inventoryManagement.restcontroller.*.*(..))")
    private void aopPointCut(){}

    @Around("aopPointCut()")
    private Object saveLogToDatabase(ProceedingJoinPoint pjp){

        if(!is_getSave){
            try {
                return pjp.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        //request객체 불러오기
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        LocalDateTime requestTime = LocalDateTime.now();

        //사용자 불러오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Object result;

        boolean is_success = true;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            result = pjp.proceed();
        }catch (Throwable e) {
            is_success = false;
            throw new RuntimeException(e);
        }finally {
            stopWatch.stop();

            LogDto.AddRequest addLogRequest = LogDto.AddRequest.builder()
                    .username(auth.getName())
                    .signature(pjp.getSignature().toShortString())
                    .requestUrl(request.getRequestURI())
                    .method(request.getMethod())
                    .elapsed(stopWatch.getLastTaskTimeMillis())
                    .at(requestTime)
                    .is_success(is_success)
                    .build();

            logService.addLog(addLogRequest);
        }
        return result;
    }
}
