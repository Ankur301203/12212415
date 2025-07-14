package com.ankur.logging.controller;

import com.ankur.logging.middleware.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private LoggingService loggingService;

    @GetMapping
    public ResponseEntity<String> test() {
        loggingService.log("backend", "info", "controller", "Test endpoint hit successfully.");
        return ResponseEntity.ok("Hello from backend.");
    }

    @GetMapping("/fail")
    public ResponseEntity<String> fail() {
        loggingService.log("backend", "error", "handler", "Simulated failure for testing.");
        System.out.println();
        return ResponseEntity.status(500).body("Something went wrong.");
    }
}

