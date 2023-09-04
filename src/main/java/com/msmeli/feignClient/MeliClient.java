package com.msmeli.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static com.msmeli.MsMeliApplication.MELI_URL;

@FeignClient(name = "Meli", url = MELI_URL)
public interface MeliClient {

    @GetMapping("/sites/{site}/search?q={query}")
    public String getProductSearch(@RequestParam String query, @PathVariable String site);
}
