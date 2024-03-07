package org.nitroc.webdemo.controllers;

import org.nitroc.webdemo.models.Greeting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/greet/{name}")
    Greeting greet(@PathVariable String name) {
        return new Greeting(name);
    }
}
