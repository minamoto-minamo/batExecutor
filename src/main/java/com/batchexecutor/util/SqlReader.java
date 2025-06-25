package com.batchexecutor.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

public class SqlReader {

	private static final String DEFAULT_PREFIX = "/sql/";
	private static final String DEFAULT_EXTENSION = ".sql";

	private SqlReader() {
	}

	public static String readSql(String path) {
		try (InputStream is = SqlReader.class.getResourceAsStream(DEFAULT_PREFIX + path + DEFAULT_EXTENSION)) {
			if (Objects.isNull(is)) {
				throw new IllegalArgumentException("SQLファイルが見つかりません: " + path);
			}
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
				return reader.lines().collect(Collectors.joining("\n"));
			}

		} catch (Exception e) {
			throw new RuntimeException("SQLファイルの読み込みに失敗しました: " + path, e);
		}
	}
}
