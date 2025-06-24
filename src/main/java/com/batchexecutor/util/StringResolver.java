package com.batchexecutor.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.batchexecutor.util.YamlParser.resolveKey;

public class StringResolver {

	private static final Pattern PLACEHOLDER = Pattern.compile("\\$\\{(.+?)}");
	private final String template;

	private StringResolver(String template) {
		this.template = template;
	}

	public static StringResolver of(String template) {
		return new StringResolver(template);
	}

	public StringResolver replaceWith(Map<String, Object> context) {
		var matcher = PLACEHOLDER.matcher(template);
		var result = new StringBuffer();

		while (matcher.find()) {
			var keyPath = matcher.group(1).trim();
			Object value = resolveKey(context, keyPath);

			String replacement;
			if (value == null) {
				throw new IllegalArgumentException("キー '" + keyPath + "' の値が null です");
			} else if (value instanceof String) {
				replacement = (String) value;
			} else if (value instanceof Number || value instanceof Boolean) {
				replacement = value.toString();
			} else {
				throw new IllegalArgumentException("キー '" + keyPath + "' の値がサポートされていません（型: " + value.getClass().getSimpleName() + "）");
			}

			matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
		}

		matcher.appendTail(result);
		return new StringResolver(result.toString());
	}

	@Override
	public String toString() {
		return template;
	}
}
