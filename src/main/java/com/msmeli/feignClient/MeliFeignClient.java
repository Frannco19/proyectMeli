package com.msmeli.feignClient;

import com.msmeli.configuration.feign.FeignClientConfiguration;
import com.msmeli.exception.ResourceNotFoundException;
import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static com.msmeli.MsMeliApplication.MELI_URL;
import static com.msmeli.global.Global.ACCESS_TOKEN;

@FeignClient(name = "Meli", url = MELI_URL, configuration = FeignClientConfiguration.class)
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

    @GetMapping("/sites/MLA/search?nickname={nickname}&catalog_listing=true")
    public String getSellerCatalogItems(@PathVariable String nickname);

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



//    //Puede funcionar mas a futuro
//    @GetMapping("/highlights/MLA/item/{product_id}")
//    public String getItemPosition(@PathVariable String product_id);

    @GetMapping("/highlights/MLA/category/{category_id}")
    public String getItemPositionByCategory(@PathVariable String category_id) throws ResourceNotFoundException;;

    @GetMapping("/sites/MLA/listing_types")
    public String getTypesNames();


}
