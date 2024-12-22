package com.yeojeong.application.config.util.customannotation;

import com.yeojeong.application.config.redis.RedisMutex;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AnnotationExceptionAop {

    private final RedisMutex redisMutex;

    private static final ExpressionParser expressionParser = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(RedisLocker)")
    public Object locker(ProceedingJoinPoint joinPoint) throws Throwable {
        RedisLocker redisLocker = ((MethodSignature) joinPoint.getSignature())
                .getMethod()
                .getAnnotation(RedisLocker.class);

        String methodKey = redisLocker.key();
        String methodValue = parseSpEL(joinPoint, redisLocker.value());

        String key = methodKey + ":" + methodValue;
        long timeout = redisLocker.timeout();

        if (redisMutex.lock(key, timeout)) {
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

    private String parseSpEL(JoinPoint joinPoint, String spEL) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        StandardEvaluationContext context = new MethodBasedEvaluationContext(null, signature.getMethod(), joinPoint.getArgs(), parameterNameDiscoverer);
        try {
            return expressionParser.parseExpression(spEL).getValue(context, String.class);
        } catch (Exception e) {
            return spEL;
        }
    }
}

