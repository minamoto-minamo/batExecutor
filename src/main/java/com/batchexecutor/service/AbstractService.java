package com.batchexecutor.service;

import com.batchexecutor.enumeration.BatchResult;
import com.batchexecutor.exception.NotReadyToExecuteException;
import com.batchexecutor.logging.Loggable;

abstract class AbstractService implements Loggable, Service {

    public BatchResult start() {
        try {

            exec();
            return BatchResult.SUCCESS;
        } catch (NotReadyToExecuteException e) {
            return BatchResult.WAIT;
        } catch (Exception e) {
            return BatchResult.ERROR;
        }
    }
}
