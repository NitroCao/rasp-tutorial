package org.nitroc.javaagent.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.jar.JarFile;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static ClassLoader systemClassLoader;

    static {
        systemClassLoader = getSystemLoader();
    }

    public static void premain(String args, Instrumentation inst) throws Exception {
        logger.info("[DemoAgent] in premain method");
        logger.info("[DemoAgent] agent args: {}", args);

        load(inst);
    }

    private static synchronized void load(Instrumentation inst) throws Exception {
        String jarPath = getLocalJarPath();
        inst.appendToBootstrapClassLoaderSearch(new JarFile(jarPath));
        String baseDir = new File(jarPath).getParent();

        if (ClassLoader.getSystemClassLoader() instanceof URLClassLoader) {
            logger.info("preparing to load engine class");
            Method method = Class.forName("java.net.URLClassLoader").getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(systemClassLoader, Paths.get(baseDir, "engine-1.0.jar").toUri().toURL());
            method.invoke(ClassLoader.getSystemClassLoader(), Paths.get(baseDir, "engine-1.0.jar").toUri().toURL());
            Class<?> engineClass = systemClassLoader.loadClass("org.nitroc.javaagent.DemoAgent");
            Method startMethod = engineClass.getDeclaredMethod("start", Instrumentation.class);
            Object engine = engineClass.getDeclaredConstructor().newInstance();
            startMethod.invoke(engine, inst);
        } else {
//            logger.info("{}", ClassLoader.getSystemClassLoader().getClass().getName());
            systemClassLoader = ClassLoader.getSystemClassLoader();
            Method method = systemClassLoader.getClass().getDeclaredMethod("appendToClassPathForInstrumentation", String.class);
            method.setAccessible(true);
            method.invoke(systemClassLoader, Paths.get(baseDir, "engine-1.0.jar").toString());
            Class<?> engineClass = systemClassLoader.loadClass("org.nitroc.javaagent.DemoAgent");
            Method startMethod = engineClass.getDeclaredMethod("start", Instrumentation.class);
            Object engine = engineClass.getDeclaredConstructor().newInstance();
            logger.info("success");
            startMethod.invoke(engine, inst);
        }
    }

    public static void agentmain(String args, Instrumentation inst) {
        logger.info("[DemoAgent] in agentmain method");
//        inst.addTransformer(new Transformer(), true);
    }

    private static String getLocalJarPath() {
        URL pathUrl = App.class.getProtectionDomain().getCodeSource().getLocation();
        String jarPath = null;

        try {
            jarPath = URLDecoder.decode(pathUrl.getFile().replace("+", "%2B"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("failed to get filepath of current Jar package");
            e.printStackTrace();
        }

        return jarPath;
    }

    private static ClassLoader getSystemLoader() {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        while (systemClassLoader.getParent() != null && !systemClassLoader.getClass().getName().equals("sun.misc.Launcher$ExtClassLoader")) {
            systemClassLoader = systemClassLoader.getParent();
        }
        logger.info("systemClassLoader: {}", systemClassLoader.getClass().getName());
        return systemClassLoader;
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
