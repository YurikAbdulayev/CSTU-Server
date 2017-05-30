package com.cstu.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MessageRestController {

    @GetMapping
    public ResponseEntity<String> testMessage() {
        return ResponseEntity.ok().body("Hello World");
    }
}
