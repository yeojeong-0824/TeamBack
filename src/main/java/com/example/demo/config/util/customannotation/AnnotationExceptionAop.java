package com.example.demo.config.util.customannotation;

import com.example.demo.config.exception.NotFoundDataException;
import com.example.demo.config.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AnnotationExceptionAop {

    private final RedissonClient redissonClient;

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

    @Around("@annotation(RedissonLocker)")
    public Object redissonLocker(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RedissonLocker redissonLocker = signature.getMethod().getAnnotation(RedissonLocker.class);

        long waitTime = redissonLocker.waitTime();
        long leaseTime = redissonLocker.leaseTime();

        String key = redissonLocker.key();

        final String lockName = key + ":lock";
        final RLock lock = redissonClient.getLock(lockName);

        boolean locked = false;
        try {
            locked = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
            if(!locked){
                log.info("Lock({}) 획득 실패", lockName);
                return null;
            }
            Object result = joinPoint.proceed();
            return result;

        } catch (NotFoundDataException e) {
            throw new NotFoundDataException(e.getMessage());

        } catch (InterruptedException e) {
            throw new ServerException(e.getMessage());

        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}