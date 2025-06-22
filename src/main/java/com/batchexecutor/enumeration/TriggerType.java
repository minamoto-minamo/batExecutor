package com.batchexecutor.enumeration;

public enum TriggerType {
	REST("正常終了"),
	SCHEDULER("実行待機");

	private final String message;

	TriggerType(String message) {
		this.message = message;
	}

	public String message() {
		return message;
	}
}
