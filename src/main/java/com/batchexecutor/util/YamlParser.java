package com.batchexecutor.util;

import java.util.Map;

/**
 * Yamlから呼んだMapからデータを取得する。
 */
public class YamlParser {
	private YamlParser() {}

	public static Object resolveKey(Object config, String keyPath) {
		var current = config;
		for (var key : keyPath.split("\\.")) {
			if (current instanceof Map<?, ?> map && map.containsKey(key)) {
				current = map.get(key);
			} else {
				throw new IllegalArgumentException("解決できません: " + keyPath + "（'" + key + "' が見つかりません）");
			}
		}
		return current;
	}

}
