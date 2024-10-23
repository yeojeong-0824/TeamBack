package com.yeojeong.application.config.util.customannotation;

import com.yeojeong.application.config.redis.RedisMutex;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.concurrent.locks.ReentrantLock;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AnnotationExceptionAop {

    private final RedisMutex redisMutex;

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

    @Around("@annotation(com.yeojeong.application.config.util.customannotation.RedisLocker)")
    public Object locker(ProceedingJoinPoint joinPoint) throws Throwable {
        RedisLocker redisLocker = ((MethodSignature) joinPoint.getSignature())
                .getMethod()
                .getAnnotation(RedisLocker.class);

        Object[] args = joinPoint.getArgs();

        Long id = null;
        for(Object arg : args) {
            if(arg instanceof Long) {
                id = (Long) arg;
                break;
            }
        }

        String key = redisLocker.key() + id;
        long timeout = redisLocker.timeout();

        if(redisMutex.lock(key, timeout)){
            try {
                return joinPoint.proceed();
            } finally {
                redisMutex.unlock(key);
            }
        } else {
            log.info("lock 획득 실패");
        }
        return null;
    }
}