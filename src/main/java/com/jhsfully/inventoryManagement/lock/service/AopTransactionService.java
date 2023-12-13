package com.jhsfully.inventoryManagement.lock.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AopTransactionService {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object proceed(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}
