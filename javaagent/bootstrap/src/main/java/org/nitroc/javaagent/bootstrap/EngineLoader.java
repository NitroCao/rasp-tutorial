package org.nitroc.javaagent.bootstrap;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarFile;

public class EngineLoader {
    private static final String ENGINE_JAR = "engine.jar";
    private static final String ENGINE_CLASS = "org.nitroc.javaagent.DemoAgent";
    public static ClassLoader systemClassLoader;

    static {
        systemClassLoader = getSystemLoader();
    }

    public static synchronized void load(Instrumentation inst) throws Exception {
        String jarPath = getLocalJarPath();
        String baseDir = new File(jarPath).getParent();
        Path engineJarPath = Paths.get(baseDir, ENGINE_JAR);
        inst.appendToBootstrapClassLoaderSearch(new JarFile(jarPath));
        inst.appendToBootstrapClassLoaderSearch(new JarFile(engineJarPath.toString()));

        if (ClassLoader.getSystemClassLoader() instanceof URLClassLoader) {
            Method method = Class.forName("java.net.URLClassLoader").getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(systemClassLoader, engineJarPath.toUri().toURL());
            method.invoke(ClassLoader.getSystemClassLoader(), engineJarPath.toUri().toURL());
            Class<?> engineClass = systemClassLoader.loadClass(ENGINE_CLASS);
            Method startMethod = engineClass.getDeclaredMethod("start", Instrumentation.class);
            Object engine = engineClass.getDeclaredConstructor().newInstance();
            startMethod.invoke(engine, inst);
        } else {
            systemClassLoader = ClassLoader.getSystemClassLoader();
            Method method = systemClassLoader.getClass().getDeclaredMethod("appendToClassPathForInstrumentation", String.class);
            method.setAccessible(true);
            method.invoke(systemClassLoader, engineJarPath.toString());
            Class<?> engineClass = systemClassLoader.loadClass(ENGINE_CLASS);
            Method startMethod = engineClass.getDeclaredMethod("start", Instrumentation.class);
            Object engine = engineClass.getDeclaredConstructor().newInstance();
            startMethod.invoke(engine, inst);
        }
    }

    private static String getLocalJarPath() {
        URL pathUrl = App.class.getProtectionDomain().getCodeSource().getLocation();
        String jarPath = null;

        try {
            jarPath = URLDecoder.decode(pathUrl.getFile().replace("+", "%2B"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("[TOYRASP] failed to get filepath of current Jar package");
            e.printStackTrace();
        }

        return jarPath;
    }

    private static ClassLoader getSystemLoader() {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        while (systemClassLoader.getParent() != null && !systemClassLoader.getClass().getName().equals("sun.misc.Launcher$ExtClassLoader")) {
            systemClassLoader = systemClassLoader.getParent();
        }
        return systemClassLoader;
    }

}
