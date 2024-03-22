package org.nitroc.javaagent.engine.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflection {
    public static Object invokeMethod(Object object, String className, String methodName, Class[] paramTypes) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ClassLoader classLoader = getClassLoader(object);
        Class<?> clazz = classLoader.loadClass(className);
        Method method = clazz.getMethod(methodName);
        return method.invoke(object, paramTypes);
    }

    private static ClassLoader getClassLoader(Object object) {
        if (object == null) {
            return ClassLoader.getSystemClassLoader();
        }

        ClassLoader classLoader = object.getClass().getClassLoader();
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        return classLoader;
    }
}
