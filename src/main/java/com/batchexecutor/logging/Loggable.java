package com.batchexecutor.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface Loggable {
	default Logger logger() {
		return LoggerFactory.getLogger(this.getClass());
	}

	default String timestamp() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
	}

	default void logInfo(String message, Object... args) {
		log(logger()::info, message, args);
	}

	default void logWarn(String message, Object... args) {
		log(logger()::warn, message, args);
	}

	default void logError(String message, Throwable e, Object... args) {
		log(logger()::error, message, args); // 引数部分のフォーマット出力
		logger().error("--- stack trace ---", e); // スタックトレースのみ追加出力
	}

	default void logError(Throwable e) {
		logger().error("[{}] --- stack trace ---", timestamp(), e);
	}

	private void log(BiConsumer<String, Object[]> logMethod, String message, Object... args) {
		if (Objects.isNull(args)) args = new Object[0];

		String placeholder = generatePlaceholder(args.length);
		String format = "[{}] {}" + (args.length > 0 ? "\n" + placeholder : "");

		Object[] allArgs = new Object[args.length + 2];
		allArgs[0] = timestamp();
		allArgs[1] = message;
		System.arraycopy(args, 0, allArgs, 2, args.length);

		logMethod.accept(format, allArgs);
	}


	default String generatePlaceholder(int count) {
		return IntStream.range(0, count)
				.mapToObj(i -> "{}")
				.collect(Collectors.joining(" "));
	}
}