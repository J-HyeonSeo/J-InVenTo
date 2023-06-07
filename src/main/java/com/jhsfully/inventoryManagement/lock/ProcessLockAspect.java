package com.jhsfully.inventoryManagement.lock;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Aspect
@Component
@RequiredArgsConstructor
public class ProcessLockAspect {
    private final RedisTemplate<String, String> redisTemplate;

    @Around("@annotation(processLock)")
    public Object applyProcess(ProceedingJoinPoint joinPoint, ProcessLock processLock) throws Throwable {
        String keyString = processLock.key();
        String lockKey = "lock:" + keyString;
        boolean acquired = false;
        Object result = null;

        try{
            acquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", Duration.ofMillis(1_000L));
            if(acquired){
                result = joinPoint.proceed();
            }else{
                throw new IllegalStateException("Process is Locked!!");
            }
        }finally {
            if(acquired){
                redisTemplate.delete(lockKey);
            }
            return result;
        }
    }

}
