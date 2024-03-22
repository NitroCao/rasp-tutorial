package org.nitroc.javaagent.engine.Hook.server.tomcat;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.nitroc.javaagent.engine.Hook.HookBase;
import org.nitroc.javaagent.engine.reflection.Reflection;

import java.lang.reflect.InvocationTargetException;

public class HttpHook extends HookBase {
    private static final String targetClassName = "org/apache/catalina/core/StandardWrapperValve";

    public static void check(Object request) throws NullPointerException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (request == null) {
            throw new NullPointerException("null request");
        }

        String requestUri = (String) Reflection.invokeMethod(request,
                "org.apache.catalina.connector.Request", "getRequestURI", new Class[]{});
        logger.info("[TOYRASP] request uri: {}", requestUri);
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public byte[] hook() {
        ClassPool classPool = ClassPool.getDefault();
        try {
            CtClass ctClass = classPool.get(HttpHook.targetClassName.replace("/", "."));
            CtMethod[] methods = ctClass.getDeclaredMethods("invoke");
            if (methods.length == 0) {
                logger.error("[TOYRASP] failed to get invoke method of {}", HttpHook.targetClassName);
                return null;
            }
            methods[0].insertBefore(createHookSource());
            return ctClass.toBytecode();
        } catch (Exception e) {
            logger.error("[TOYRASP] failed to hook {}", HttpHook.targetClassName.replace("/", "."), e);
        }
        return null;
    }

    @Override
    public boolean isTargetClass(String className) {
        if (className == null) {
            return false;
        }
        return className.equals(HttpHook.targetClassName);
    }

    private String createHookSource() {
        return String.format("%s.check($1);", HttpHook.class.getCanonicalName());
    }
}
