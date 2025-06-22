package com.batchexecutor.util;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class YamlConfigStore {

	private static final Yaml YAML = new Yaml();
	private static final String DEFAULT_PREFIX = "prop/";
	private static final String DEFAULT_EXTENSION = ".yml";

	private final Map<String, Map<String, Object>> cache = new ConcurrentHashMap<>();

	private YamlConfigStore() {}

	public static YamlConfigStore getInstance() {
		return Holder.INSTANCE;
	}

	public Map<String, Object> getConfig(String name) {
		String path = DEFAULT_PREFIX + name + DEFAULT_EXTENSION;
		return cache.computeIfAbsent(path, this::loadYaml);
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
