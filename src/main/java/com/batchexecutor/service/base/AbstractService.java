package com.batchexecutor.service.base;

import com.batchexecutor.enumeration.BatchResult;
import com.batchexecutor.exception.NotReadyToExecuteException;
import com.batchexecutor.logging.Loggable;

public abstract class AbstractService implements Loggable, Service {

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
