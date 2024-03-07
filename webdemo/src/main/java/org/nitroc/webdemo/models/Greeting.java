package org.nitroc.webdemo.models;

public class Greeting {
    private static final String GUEST = "Guest";
    public String name;
    public String msg;

    public Greeting(String name) {
        this.name = name;
        if (this.name == null) {
            this.name = GUEST;
        }
        this.msg = String.format("Hello, %s", name);
    }
}
