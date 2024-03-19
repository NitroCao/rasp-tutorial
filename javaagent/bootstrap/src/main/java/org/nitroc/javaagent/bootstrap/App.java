package org.nitroc.javaagent.bootstrap;

import java.lang.instrument.Instrumentation;

public class App {
    public static void premain(String args, Instrumentation inst) throws Exception {
        System.out.println("[TOYRASP] in premain method");
        System.out.printf("[TOYRASP] agent args: %s%n", args);

        EngineLoader.load(inst);
    }

    public static void agentmain(String args, Instrumentation inst) {
        System.out.println("[TOYRASP] in agentmain method");
//        inst.addTransformer(new Transformer(), true);
    }

    public static void main(String[] args) {
        System.out.println("[TOYRASP] Hello World!");
    }
}
