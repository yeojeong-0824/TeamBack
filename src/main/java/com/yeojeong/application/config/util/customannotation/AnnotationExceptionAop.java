package com.yeojeong.application.config.util.customannotation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AnnotationExceptionAop {

    @Around("@annotation(MethodTimer)")
    public Object methodTimer(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();

        long totalTimeMillis = stopWatch.getTotalTimeMillis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        MethodTimer requiredPermission = signature.getMethod().getAnnotation(MethodTimer.class);
        String method = requiredPermission.method();

        log.info("{}, 실행 시간: {} ms", method, totalTimeMillis);
        return result;
    }
}