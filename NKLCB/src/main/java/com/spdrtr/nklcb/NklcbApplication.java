package com.spdrtr.nklcb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
public class NklcbApplication {

    public static void main(String[] args) {
        SpringApplication.run(NklcbApplication.class, args);
    }

}
