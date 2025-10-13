package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // tells Spring this class will handle web requests
public class HelloController {

    @GetMapping("/nigga")   // when you visit http://localhost:8080/hello
    public String sayHello() {
        return "Fuck you!";
    }
}
