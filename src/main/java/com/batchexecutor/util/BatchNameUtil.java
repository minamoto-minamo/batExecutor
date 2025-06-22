package com.batchexecutor.util;

public class BatchNameUtil {

	private BatchNameUtil() {
	}

	public static String inferJobName(Class<?> clazz) {
		Class<?> realClass = resolveOriginalClass(clazz);
		return realClass.getSimpleName().replaceFirst("Config$", "");
	}

	public static String getMethodName(int i) {
		return Thread.currentThread().getStackTrace()[2 + i].getMethodName();
	}

	public static String createClassAndMethod(Class<?> clazz) {
		return inferJobName(clazz) + "#" + getMethodName(1);
	}

	private static Class<?> resolveOriginalClass(Class<?> clazz) {
		// Spring CGLIB Proxy の場合、スーパークラスが元のクラスになる
		while (clazz.getName().contains("$$")) {
			clazz = clazz.getSuperclass();
		}
		return clazz;
	}
}
