package com.example.character01_demo.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class helloworld {

    @RequestMapping("/")
    String home(){
        return "hello";
    }
    public static void main(String[] args) {
        SpringApplication.run(helloworld.class,args);
    }
}
