package com.jhsfully.inventoryManagement.lock;

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

        try{
            acquired = getLock(lockKey, 2_000L);
            if(acquired){
                return joinPoint.proceed();
            }else{
                throw new IllegalStateException("Process is Locked!!");
            }
        }finally {
            if(acquired){
                releaseLock(lockKey);
            }
        }
    }

    private boolean getLock(String lockKey, Long timeout) throws InterruptedException {
        Long startTime = System.currentTimeMillis();
        Long endTime = startTime + timeout;
        boolean result = false;

        while(System.currentTimeMillis() < endTime){
            result = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", Duration.ofMillis(15_000L));
            if(result){
                break;
            }
            Thread.sleep(100);
        }
        return result;
    }

    private void releaseLock(String lockKey){
        redisTemplate.delete(lockKey);
    }

}
