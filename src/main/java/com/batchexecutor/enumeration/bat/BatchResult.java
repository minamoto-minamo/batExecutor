package com.batchexecutor.enumeration.bat;

public enum BatchResult {
    SUCCESS(0, "正常終了"),
    ERROR(1, "異常終了"),
    WAIT(2, "実行待機");

    private final int code;
    private final String message;

    BatchResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public static BatchResult fromCode(int code) {
        for (BatchResult status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }
}
