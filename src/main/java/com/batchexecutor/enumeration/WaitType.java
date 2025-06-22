package com.batchexecutor.enumeration;

public enum WaitType {
    NO_FILE("ファイル未達"),
    PRE_BATCH_NOT_COMPLETED("事前実行バッチ未完了");

    private final String message;

    WaitType(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
