package com.batchexecutor.util;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class YamlConfigStore {

	private static final Yaml YAML = new Yaml();
	private static final String DEFAULT_PREFIX = "config/";
	private static final String DEFAULT_EXTENSION = ".yml";

	private final Map<String, Map<String, Object>> cache = new ConcurrentHashMap<>();

	private YamlConfigStore() {
	}

	public static YamlConfigStore getInstance() {
		return Holder.INSTANCE;
	}

	public Map<String, Object> getTableConfig(String sheetName) {
		return getConfig(DEFAULT_PREFIX + "table/" + sheetName + DEFAULT_EXTENSION);
	}

	public Map<String, Object> getJobConfig(String sheetName) {
		return getConfig(DEFAULT_PREFIX + "job/" + sheetName + DEFAULT_EXTENSION);
	}

	private Map<String, Object> getConfig(String name) {
		return cache.computeIfAbsent(name, this::loadYaml);
	}

	private Map<String, Object> loadYaml(String path) {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream(path)) {
			if (in == null) {
				throw new IllegalArgumentException("YAML not found: " + path);
			}
			return YAML.load(in);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load YAML: " + path, e);
		}
	}

	private static class Holder {
		private static final YamlConfigStore INSTANCE = new YamlConfigStore();
	}
}
