package com.msmeli.feignClient;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static com.msmeli.MsMeliApplication.MELI_URL;
import static com.msmeli.global.Global.ACCESS_TOKEN;

@FeignClient(name = "Meli", url = MELI_URL)
public interface MeliFeignClient {

    @GetMapping("/products/{productId}/items")
    @Headers(
            "Authorization: " + ACCESS_TOKEN
    )
    public String getProductSearch(@PathVariable String productId);

    @GetMapping("/sites/MLA/search?nickname={nickname}")
    @Headers(
            "Authorization: " + ACCESS_TOKEN
    )
    public String getSellerByNickname(@PathVariable String nickname);

    @GetMapping("/categories/{categoryId}")
    @Headers(
            "Authorization: " + ACCESS_TOKEN
    )
    public String getCategory(@PathVariable String categoryId);

    @GetMapping("/sites/MLA/search?seller_id={seller_id}")
    @Headers(
            "Authorization: " + ACCESS_TOKEN
    )
    public String getSellerBySellerId(@PathVariable Integer seller_id);

    @GetMapping("/items/{item_id}")
    @Headers(
            "Authorization: " + ACCESS_TOKEN
    )
    public String getImageAndSku(@PathVariable String item_id);



}
