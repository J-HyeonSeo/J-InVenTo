package com.jhsfully.inventoryManagement.lock;

import com.jhsfully.inventoryManagement.lock.service.AopTransactionService;
import com.jhsfully.inventoryManagement.lock.service.RedissonLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ProcessLockAspect {

    private final RedissonLockService redissonLockService;
    private final AopTransactionService aopTransactionService;

    @Around("@annotation(processLock)")
    public Object lock(ProceedingJoinPoint joinPoint, ProcessLock processLock) throws Throwable {

        String keyExpression = processLock.key();
        String key = getKey(keyExpression, joinPoint);
        String group = processLock.group();

        redissonLockService.lock(group, key, processLock.waitTime(), processLock.leaseTime());
        try {
            return aopTransactionService.proceed(joinPoint);
        } finally {
            redissonLockService.unlock(group, key);
        }
    }

    private String getKey(String expression, ProceedingJoinPoint joinPoint) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        ExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression(expression).getValue(context, String.class);
    }

}
