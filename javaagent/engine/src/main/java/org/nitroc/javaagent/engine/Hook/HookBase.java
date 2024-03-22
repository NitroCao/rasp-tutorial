package org.nitroc.javaagent.engine.Hook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HookBase {
    protected static Logger logger = LoggerFactory.getLogger(HookBase.class.getName());

    public abstract boolean enabled();

    public abstract byte[] hook();

    public abstract boolean isTargetClass(String className);

}
