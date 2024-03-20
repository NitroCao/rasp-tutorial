package org.nitroc.javaagent.engine.Hook;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;

public class ProcessHook extends HookBase {
    private final static String className = "java/lang/ProcessImpl";

    public static void check(byte[] command) {
        if (command != null && command.length > 0) {
            String exePath = new String(command, 0, command.length - 1);
            System.out.println(exePath);
        }
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public boolean isTargetClass(String className) {
        if (className == null) {
            return false;
        }

        return className.equals(ProcessHook.className);
    }

    private String createHookSource() {
        return String.format("%s.check($1);", ProcessHook.class.getCanonicalName());
    }

    public byte[] hook() {
        ClassPool classPool = ClassPool.getDefault();
        try {
            CtClass ctClass = classPool.get(ProcessHook.className.replace("/", "."));
            CtConstructor[] constructors = ctClass.getDeclaredConstructors();
            if (constructors.length == 0) {
                logger.error("[TOYRASP] failed to get constructors of {}", ProcessHook.class.getCanonicalName());
                return null;
            }
            constructors[0].insertBefore(createHookSource());
            return ctClass.toBytecode();
        } catch (Exception e) {
           logger.error("[TOYRASP] failed to hook {}", ProcessHook.class.getCanonicalName(), e);
        }

        return null;
    }
}
