package com.jhsfully.inventoryManagement.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProcessLock {
    String key() default "";
    String group() default "";
    long waitTime() default 3L; //lock을 취득할 때까지 대기할 시간.
    long leaseTime() default 2L; //unlock이 수행되기 전까지 lock을 점유하는 시간.
}
