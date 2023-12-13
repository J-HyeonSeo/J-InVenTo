package com.jhsfully.inventoryManagement.lock.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedissonLockService {

    private final RedissonClient redissonClient;

    //group-key를 기준으로 lock을 획득함.
    public void lock(Object group, Object key, long waitTime, long leaseTime)
        throws InterruptedException {
        RLock lock = redissonClient.getLock(getLockKey(group, key));
        log.debug("Trying lock for group-key : {}", getLockKey(group, key));
        try {
            boolean isLock = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
            if (!isLock) {
                log.error("Lock acquisition failed");
                throw new RuntimeException();
            }
        } catch (InterruptedException e){ //쓰레드가 중단된 경우, Lock을 가져올 수 없음.
            log.error("The thread is interrupted", e);
            throw new InterruptedException();
        }
    }

    //group-key를 기준으로 lock을 반환함.
    public void unlock(Object group, Object key){
        log.debug("Unlock for group-key : {}", getLockKey(group, key));
        redissonClient.getLock(getLockKey(group, key)).unlock();
    }

    private static String getLockKey(Object group, Object key) {
        return "LOCK:" + group.toString() + "-" + key.toString();
    }

}
