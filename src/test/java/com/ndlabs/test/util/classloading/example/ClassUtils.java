package com.ndlabs.test.util.classloading.example;

public class ClassUtils {
	public static boolean isPresent(String name, ClassLoader classLoader) {
		try {
			classLoader.loadClass(name);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
