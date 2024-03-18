package org.nitroc.webdemo.controllers;

import org.nitroc.webdemo.models.CmdResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@RestController
public class CmdController {
    @PostMapping("/cmd")
    CmdResult execute(@RequestParam(name = "cmd") String cmd) {
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
        try {
            Process process = builder.start();
            String output = new BufferedReader(new InputStreamReader(process.getInputStream())).lines().collect(Collectors.joining("\n"));
            return new CmdResult(output);
        } catch (Exception e) {
            return new CmdResult(e.toString());
        }
    }
}
