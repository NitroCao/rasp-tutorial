package org.nitroc.javaagent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

public class DemoAgent {
    private static final Logger logger = LoggerFactory.getLogger(DemoAgent.class);
    private boolean stop = false;

    public void start(Instrumentation inst) {
        logger.info("DemoAgent started");
        Transformer transformer = new Transformer();
        inst.addTransformer(transformer);
    }

    public void stop(Instrumentation inst) {
        stop = true;
    }
}
