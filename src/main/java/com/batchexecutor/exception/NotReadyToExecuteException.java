package com.batchexecutor.exception;

import com.batchexecutor.enumeration.WaitType;

public class NotReadyToExecuteException extends RuntimeException {
	public NotReadyToExecuteException() {
		super("まだ実行するタイミングではありません。");
	}

	public NotReadyToExecuteException(WaitType type) {
		super("実行するタイミングではありません:" + type.message());
	}

	public NotReadyToExecuteException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotReadyToExecuteException(Throwable cause) {
		super(cause);
	}
}
