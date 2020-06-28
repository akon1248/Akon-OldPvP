package com.akon.oldpvp.utils;

import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

    public static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + getVersion() + "." + name);
    }

    public static Class<?> getOBCClass(String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + name);
    }

    public static Object getHandle(Object obj) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeMethod(obj, "getHandle", new Class[0], new Object[0]);
    }

    public static Object getField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field;
        try {
            field = obj.getClass().getField(fieldName);
        } catch (NoSuchFieldException e) {
            Class clazz = obj.getClass();
            while (true) {
                try {
                    field = clazz.getDeclaredField(fieldName);
                    break;
                } catch (NoSuchFieldException ex) {
                    if (clazz == Object.class) {
                        throw ex;
                    }
                    clazz = clazz.getSuperclass();
                    continue;
                }
            }
        }
        field.setAccessible(true);
        return field.get(obj);
    }

    public static Object getField(Class clazz, Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    public static Object getStaticField(Class clazz, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(null);
    }

    public static void setField(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field;
        try {
            field = obj.getClass().getField(fieldName);
        } catch (NoSuchFieldException e) {
            Class clazz = obj.getClass();
            while (true) {
                try {
                    field = clazz.getDeclaredField(fieldName);
                    break;
                } catch (NoSuchFieldException ex) {
                    if (clazz == Object.class) {
                        throw ex;
                    }
                    clazz = clazz.getSuperclass();
                    continue;
                }
            }
        }
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & -17);
        field.set(obj, value);
    }

    public static void setField(Class clazz, Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static void setStaticField(Class clazz, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & -17);
        field.set(null, value);
    }

    public static Object invokeMethod(Object obj, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeMethod(obj, methodName, new Class[0], new Object[0]);
    }

    public static Object invokeMethod(Object obj, String methodName, Class[] paramTypes, Object[] params) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method;
        try {
            method = obj.getClass().getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            Class clazz = obj.getClass();
            while (true) {
                try {
                    method = clazz.getDeclaredMethod(methodName, paramTypes);
                    break;
                } catch (NoSuchMethodException ex) {
                    if (clazz == Object.class) {
                        throw ex;
                    }
                    clazz = clazz.getSuperclass();
                    continue;
                }
            }
        }
        method.setAccessible(true);
        return method.invoke(obj, params);
    }

    public static Object invokeMethod(Class clazz, Object obj, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeMethod(clazz, obj, methodName, new Class[0], new Object[0]);
    }

    public static Object invokeMethod(Class clazz, Object obj, String methodName, Class[] paramTypes, Object[] params) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = clazz.getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        return method.invoke(obj, params);
    }

    public static Object invokeStaticMethod(Class clazz, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeStaticMethod(clazz, methodName, new Class[0], new Object[0]);
    }

    public static Object invokeStaticMethod(Class clazz, String methodName, Class[] paramTypes, Object[] params) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = clazz.getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        return method.invoke(null, params);
    }

    public static <T> T invokeConstructor(Class<T> clazz) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return invokeConstructor(clazz, new Class[0], new Object[0]);
    }

    public static <T> T invokeConstructor(Class<T> clazz, Class[] paramTypes, Object[] params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<T> constructor = clazz.getDeclaredConstructor(paramTypes);
        constructor.setAccessible(true);
        return constructor.newInstance(params);
    }

}
