package com.example.villagefirerisk.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLoggable {
    String module();
    String operation();
}
