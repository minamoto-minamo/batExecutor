package com.batchexecutor.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SqlReader {

	private SqlReader() {
		// ユーティリティクラスのためインスタンス化禁止
	}

	public static String readSql(String path) {
		Path filePath = Paths.get(path);
		if (!Files.exists(filePath)) {
			throw new IllegalArgumentException("SQLファイルが見つかりません: " + path);
		}

		try {
			return Files.readString(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new IllegalArgumentException("SQLファイルの読み込みに失敗しました: " + path, e);
		}
	}
}
