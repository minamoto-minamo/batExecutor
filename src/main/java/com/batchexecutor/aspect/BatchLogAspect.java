package com.batchexecutor.aspect;

import com.batchexecutor.exception.NotReadyToExecuteException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BatchLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(BatchLogAspect.class);

    @Around("execution(* com.batchexecutor.batch.tasklet..*.runWithRetry(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String taskletName = joinPoint.getTarget().getClass().getSimpleName();

        logger.info("Tasklet START: {}", taskletName);
        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            logger.info("Tasklet END: {} ({}ms)", taskletName, duration);
            return result;
        } catch (NotReadyToExecuteException e) {
            logger.warn("Tasklet RETRY requested by: {}", taskletName);
            throw e;
        } catch (InterruptedException e) {
            logger.error("Tasklet INTERRUPTED: {}", taskletName);
            throw e;
        } catch (Throwable t) {
            logger.error("Tasklet ERROR: {}", taskletName, t);
            throw t;
        }
    }
}
