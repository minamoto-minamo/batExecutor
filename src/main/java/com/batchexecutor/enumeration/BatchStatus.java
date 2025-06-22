package com.batchexecutor.enumeration;

public enum BatchStatus {
	YET(0, "未実行"),
	NOW(1, "実行中"),
	DONE(2, "実行済み"),
	ERROR(3, "エラー終了");

	private final int code;
	private final String message;

	BatchStatus(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int code() {
		return code;
	}

	public String message() {
		return message;
	}

	public static BatchStatus fromCode(int code) {
		for (BatchStatus status : values()) {
			if (status.code == code) {
				return status;
			}
		}
		throw new IllegalArgumentException("Unknown status code: " + code);
	}
}
