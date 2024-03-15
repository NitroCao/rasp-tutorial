package org.nitroc.javaagent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

public class DemoAgent {
    private static final Logger logger = LoggerFactory.getLogger(DemoAgent.class);
    public static void premain(String args, Instrumentation inst) {
        logger.info("[DemoAgent] in premain method");
        inst.addTransformer(new Transformer(), true);
    }

    public static void agentmain(String args, Instrumentation inst) {
        logger.info("[DemoAgent] in agentmain method");
        inst.addTransformer(new Transformer(), true);
    }
}
