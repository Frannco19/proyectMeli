package com.msmeli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsMeliApplication {
    public static final String MELI_URL = "https://api.mercadolibre.com";
    public static void main(String[] args) {

        SpringApplication.run(MsMeliApplication.class, args);
    }

}
