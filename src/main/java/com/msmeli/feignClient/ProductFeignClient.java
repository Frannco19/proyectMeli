package com.msmeli.feignClient;

import com.msmeli.global.Global;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.msmeli.MsMeliApplication.MELI_URL;
import static com.msmeli.global.Global.ACCESS_TOKEN;

@FeignClient(name = "Product", url = MELI_URL)
public interface ProductFeignClient {

    @GetMapping("/products/{productId}/items")
    @Headers(
        "Authorization: " + ACCESS_TOKEN
            )
    public String getProductSearch(@PathVariable String productId);
}
