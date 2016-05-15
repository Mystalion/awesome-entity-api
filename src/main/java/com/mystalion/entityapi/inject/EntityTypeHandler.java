package com.mystalion.entityapi.inject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;

import com.mystalion.entityapi.Reflection;

import lombok.Getter;

@Getter
@SuppressWarnings("unchecked")
public class EntityTypeHandler {

	private static final Class<?> ENTITY_TYPES = Reflection.getNMSClass("EntityTypes");
	//
	private static Map<String, Class<?>> DEFAULT_NAME_TO_CLASS; // c
	private static Map<Class<?>, String> DEFAULT_CLASS_TO_NAME; // d
	private static Map<Integer, Class<?>> DEFAULT_ID_TO_CLASS; // e 
	private static Map<Class<?>, Integer> DEFAULT_CLASS_TO_ID; // f 
	private static Map<String, Integer> DEFAULT_NAME_TO_ID; // g
	//
	private Map<String, Class<?>> nameToClass; // c
	private Map<Class<?>, String> classToName; // d
	private Map<Integer, Class<?>> idToClass; // e 
	private Map<Class<?>, Integer> classToId; // f 
	private Map<String, Integer> nameToId; // g

	public static void copyDefaults() throws Exception {
		if (DEFAULT_CLASS_TO_ID != null) {
			throw new IllegalStateException();
		}
		DEFAULT_NAME_TO_CLASS = new HashMap<>((Map<String, Class<?>>) Reflection.getField(ENTITY_TYPES, "c").get(null));
		DEFAULT_CLASS_TO_NAME = new HashMap<>((Map<Class<?>, String>) Reflection.getField(ENTITY_TYPES, "d").get(null));
		DEFAULT_ID_TO_CLASS = new HashMap<>((Map<Integer, Class<?>>) Reflection.getField(ENTITY_TYPES, "e").get(null));
		DEFAULT_CLASS_TO_ID = new HashMap<>((Map<Class<?>, Integer>) Reflection.getField(ENTITY_TYPES, "f").get(null));
		DEFAULT_NAME_TO_ID = new HashMap<>((Map<String, Integer>) Reflection.getField(ENTITY_TYPES, "g").get(null));
	}

	public static void restoreDefaults() throws Exception {
		writeMap(DEFAULT_NAME_TO_CLASS, Reflection.getField(ENTITY_TYPES, "c"));
		writeMap(DEFAULT_CLASS_TO_NAME, Reflection.getField(ENTITY_TYPES, "d"));
		writeMap(DEFAULT_ID_TO_CLASS, Reflection.getField(ENTITY_TYPES, "e"));
		writeMap(DEFAULT_CLASS_TO_ID, Reflection.getField(ENTITY_TYPES, "f"));
		writeMap(DEFAULT_NAME_TO_ID, Reflection.getField(ENTITY_TYPES, "g"));
	}

	private static void writeMap(Map<?, ?> map, Field field) throws Exception {
		Map<Object, Object> o = (Map<Object, Object>) field.get(null);
		o.clear();
		for (Entry<?, ?> entry : map.entrySet()) {
			o.put(entry.getKey(), entry.getValue());
		}
	}

	public EntityTypeHandler() throws Exception {
		nameToClass = (Map<String, Class<?>>) Reflection.getField(ENTITY_TYPES, "c").get(null);
		classToName = (Map<Class<?>, String>) Reflection.getField(ENTITY_TYPES, "d").get(null);
		idToClass = (Map<Integer, Class<?>>) Reflection.getField(ENTITY_TYPES, "e").get(null);
		classToId = (Map<Class<?>, Integer>) Reflection.getField(ENTITY_TYPES, "f").get(null);
		nameToId = (Map<String, Integer>) Reflection.getField(ENTITY_TYPES, "g").get(null);
	}

	public void registerEntity(String name, Class<?> clazz, int id) {
		nameToClass.put(name, clazz);
		classToName.put(clazz, name);
		classToId.put(clazz, Integer.valueOf(id));
	}

	public <T> T spawnEntity(Class<T> clazz, Location location) {
		try {
			Object world = Reflection.getHandle(location.getWorld());
			T entity = Reflection.getConstructor(clazz, Reflection.getNMSClass("World")).newInstance(world);
			Reflection.getMethod(clazz, "setLocation", double.class, double.class, double.class, float.class, float.class).invoke(entity, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
			Reflection.getMethod(Reflection.getNMSClass("World"), "addEntity", Reflection.getNMSClass("Entity")).invoke(world, entity);
			return entity;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
