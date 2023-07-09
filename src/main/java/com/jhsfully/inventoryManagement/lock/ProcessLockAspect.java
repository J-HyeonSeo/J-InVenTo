package com.jhsfully.inventoryManagement.lock;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Arrays;

@Aspect
@Component
public class ProcessLockAspect {

    private RedisTemplate<String, String> redisTemplate;
    private DefaultParameterNameDiscoverer parameterNameDiscoverer;

    public ProcessLockAspect(RedisTemplate<String, String> redisTemplate){
        this.redisTemplate = redisTemplate;
        this.parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    }

    @Around("@annotation(processLock)")
    public Object applyProcess(ProceedingJoinPoint joinPoint, ProcessLock processLock) throws Throwable {
        String valueString = processLock.value();
        String keyString = getKeyString(joinPoint, processLock.key());

        //ex) lockKey = 'lock:purchase-inbound-1'
        String lockKey = "lock:" + valueString + "-" + keyString;

        System.out.println(lockKey);

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

    private String getKeyString(ProceedingJoinPoint joinPoint, String path){

        Object[] args = joinPoint.getArgs();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(methodSignature.getMethod());

        String[] parameterPath = path.split("\\."); // 점(.)을 기준으로 나눔

        System.out.println("parameters : ");
        System.out.println(Arrays.toString(parameterNames));
        System.out.println(Arrays.toString(parameterPath));

        for (int i = 0; i < parameterNames.length; i++) {
            if(parameterNames[i].equals(parameterPath[0])){

                if(parameterPath.length == 1){ //멤버 변수가 없는 경우.
                    return args[i] + "";
                }

                try {
                    return findFieldValue(1, args[i], parameterPath);
                }catch (Exception e){
                    new RuntimeException("데이터 구조가 올바르지 않습니다.");
                }
            }
        }

        return "";
    }

    private String findFieldValue(int idx, Object arg, String[] fields) throws NoSuchFieldException, IllegalAccessException {
        Field field = arg.getClass().getDeclaredField(fields[idx]);
        field.setAccessible(true);

        Object fieldValue = field.get(arg);

        if(idx == fields.length - 1){
            return fieldValue + "";
        }else{
            return findFieldValue(idx + 1, fieldValue, fields);
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
