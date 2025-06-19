package com.batchexecutor.exception;

import com.batchexecutor.enumeration.bat.WaitType;

public class NotReadyToExecuteException extends RuntimeException {
    public NotReadyToExecuteException() {
        super("まだ実行するタイミングではありません。");
    }

    public NotReadyToExecuteException(WaitType type) {
        switch (type){
            case NO_FILE -> {
          }
            case PRE_BATCH_NOT_COMPLETED -> {

            }
        }


    }

    public NotReadyToExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotReadyToExecuteException(Throwable cause) {
        super(cause);
    }
}
