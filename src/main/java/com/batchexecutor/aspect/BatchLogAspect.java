package com.batchexecutor.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BatchLogAspect {

    @Before("execution(* org.springframework.batch.core.launch.JobLauncher.run(..))")
    public void beforeJobExecution() {
        System.out.println("ジョブ開始");
    }

    @AfterReturning("execution(* org.springframework.batch.core.launch.JobLauncher.run(..))")
    public void afterJobExecution() {
        System.out.println("ジョブ終了");
    }
}
