package org.nitroc.javaagent.engine;

public class ProcessHook {
    public static void check(byte[] command) {
        if (command != null && command.length > 0) {
            String exePath = new String(command, 0, command.length - 1);
            System.out.println(exePath);
        }
    }
}
