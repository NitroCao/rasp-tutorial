package org.nitroc.javaagent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class Transformer implements ClassFileTransformer {
    private static final Logger logger = LoggerFactory.getLogger(Transformer.class);

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        String hookClassName = "java/lang/ProcessImpl";
        if (!className.equals(hookClassName)) {
            return ClassFileTransformer.super.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
        }

        ClassPool classPool = ClassPool.getDefault();
        try {
            CtClass ctClass = classPool.get(hookClassName.replace("/", "."));
            CtConstructor[] initMethod = ctClass.getDeclaredConstructors();
//            initMethod[0].insertBefore("org.nitroc.javaagent.bootstrap.EngineLoader.systemClassLoader.loadClass(\"org.nitroc.javaagent.ProcessHook\").getMethod(\"check\", new Class[]{Class.forName(byte[].class.getName())}).invoke(null, new Object[]{$1});");
            initMethod[0].insertBefore("org.nitroc.javaagent.ProcessHook.check($1);");
            return ctClass.toBytecode();
        } catch (Exception e) {
            logger.error("[TOYRASP] failed to hook {}", hookClassName, e);
        }
        return ClassFileTransformer.super.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
    }
}
