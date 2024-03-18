package org.nitroc.javaagent.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);


    public static void premain(String args, Instrumentation inst) throws Exception {
        logger.info("[DemoAgent] in premain method");
        logger.info("[DemoAgent] agent args: {}", args);

        EngineLoader.load(inst);
    }

    public static void agentmain(String args, Instrumentation inst) {
        logger.info("[DemoAgent] in agentmain method");
//        inst.addTransformer(new Transformer(), true);
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
