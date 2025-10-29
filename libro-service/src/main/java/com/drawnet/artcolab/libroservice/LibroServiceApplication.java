package com.drawnet.artcolab.libroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class LibroServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibroServiceApplication.class, args);
    }

}
