package org.nitroc.javaagent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.TimeUnit;

public class DemoAgent {
    private boolean stop = false;
    private static final Logger logger = LoggerFactory.getLogger(DemoAgent.class);

    public void start(Instrumentation inst) {
        logger.info("DemoAgent started");
//        try {
//            while (!stop) {
//                TimeUnit.SECONDS.sleep(4);
//            }
//        } catch (InterruptedException e) {
//            logger.info("interrupt signal received");
//        }
    }

    public void stop(Instrumentation inst) {
        stop = true;
    }
}
