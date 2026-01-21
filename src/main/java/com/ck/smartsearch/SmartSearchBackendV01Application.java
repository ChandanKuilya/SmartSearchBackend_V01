package com.ck.smartsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SmartSearchBackendV01Application {

    public static void main(String[] args) {
        SpringApplication.run(SmartSearchBackendV01Application.class, args);
    }

}
