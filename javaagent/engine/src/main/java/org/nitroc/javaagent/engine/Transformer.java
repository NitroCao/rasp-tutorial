package org.nitroc.javaagent.engine;

import org.nitroc.javaagent.engine.Hook.HookBase;
import org.nitroc.javaagent.engine.Hook.ProcessHook;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;

public class Transformer implements ClassFileTransformer {
    private final HashSet<HookBase> hooks;

    public Transformer() {
        hooks = initHooks();
    }

    private static HashSet<HookBase> initHooks() {
        HashSet<HookBase> hooks = new HashSet<>();
        hooks.add(new ProcessHook());

        return hooks;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        HookBase hook = lookupHook(className);
        if (hook == null) {
            return ClassFileTransformer.super.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
        }

        byte[] code = hook.hook();
        if (code == null) {
            return ClassFileTransformer.super.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
        }
        return code;
    }
    
    private HookBase lookupHook(String className) {
        for (HookBase hook : hooks) {
            if (hook.isTargetClass(className)) {
                return hook;
            }
        }
        
        return null;
    }
}
