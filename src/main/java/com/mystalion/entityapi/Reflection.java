package com.mystalion.entityapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

public class Reflection {

	private static final Map<String, Class<?>> CLASS_CACHE = new HashMap<>();
	private static final Map<String, Field> FIELD_CACHE = new HashMap<>();
	private static final Map<String, Method> METHOD_CACHE = new HashMap<>();
	private static final Map<String, Constructor<?>> CONSTRUCTOR_CACHE = new HashMap<>();
	//
	private static final String NMS_PACKAGE_NAME = Bukkit.getServer().getClass().getPackage().getName();
	public static final String NMS_VERSION = NMS_PACKAGE_NAME.substring(NMS_PACKAGE_NAME.lastIndexOf('.') + 1);

	public static Class<?> getClass(String name) {
		if (!CLASS_CACHE.containsKey(name)) {
			try {
				CLASS_CACHE.put(name, Class.forName(name));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return CLASS_CACHE.get(name);
	}

	public static Field getField(Class<?> clazz, String name) {
		String index = fieldCacheIndex(clazz, name);
		if (!FIELD_CACHE.containsKey(index)) {
			try {
				Field field = clazz.getDeclaredField(name);
				field.setAccessible(true);
				FIELD_CACHE.put(index, field);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return FIELD_CACHE.get(index);
	}

	private static String fieldCacheIndex(Class<?> c, String n) {
		return c.getName() + "~" + n;
	}

	public static Method getMethod(Class<?> clazz, String name, Class<?>... parameters) {
		String cacheIndex = methodCacheIndex(clazz, name, parameters);
		if (!METHOD_CACHE.containsKey(cacheIndex)) {
			try {
				Method method = clazz.getMethod(name, parameters);
				METHOD_CACHE.put(cacheIndex, method);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return METHOD_CACHE.get(cacheIndex);
	}

	private static String methodCacheIndex(Class<?> c, String n, Class<?>... p) {
		return c.getName() + "~" + n + Arrays.toString(p);
	}

	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... p) {
		String cacheIndex = constructorCacheIndex(clazz, p);
		if (!CONSTRUCTOR_CACHE.containsKey(cacheIndex)) {
			try {
				Constructor<T> constructor = clazz.getConstructor(p);
				CONSTRUCTOR_CACHE.put(cacheIndex, constructor);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return (Constructor<T>) CONSTRUCTOR_CACHE.get(cacheIndex);
	}

	private static String constructorCacheIndex(Class<?> c, Class<?>... p) {
		return c.getName() + "~" + Arrays.toString(p);
	}

	public static Class<?> getNMSClass(String className) {
		return getClass("net.minecraft.server." + NMS_VERSION + "." + className);
	}

	public static Class<?> getOCBClass(String className) {
		return getClass("org.bukkit.craftbukkit." + NMS_VERSION + "." + className);
	}

	public static Class<?> getInnerClass(Class<?> c, String className) {
		Class<?> clazz = null;
		try {
			for (Class<?> cl : c.getDeclaredClasses()) {
				if (cl.getSimpleName().equals(className)) {
					clazz = cl;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getHandle(Object obj) {
		try {
			return (T) getMethod(obj.getClass(), "getHandle").invoke(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
		boolean equal = true;
		if (l1.length != l2.length)
			return false;
		for (int i = 0; i < l1.length; i++)
			if (l1[i] != l2[i]) {
				equal = false;
				break;
			}
		return equal;
	}
}
