package com.msmeli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableScheduling
@SpringBootApplication
@Slf4j
public class MsMeliApplication {
    public static final String MELI_URL = "https://api.mercadolibre.com";
    public static void main(String[] args) {
        SpringApplication.run(MsMeliApplication.class, args);
        log.info("G&L App running...");
    }
}
