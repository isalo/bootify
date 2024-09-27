package com.cykor.hub.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("com.cykor.hub")
public class HubApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HubApplication.class, args);
    }

}
