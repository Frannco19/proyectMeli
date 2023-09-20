package com.msmeli.feignClient;

import com.msmeli.configuration.feign.FeignClientConfiguration;
import com.msmeli.exception.ResourceNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.msmeli.MsMeliApplication.MELI_URL;

@FeignClient(name = "Meli", url = MELI_URL, configuration = FeignClientConfiguration.class)
public interface MeliFeignClient {

    @GetMapping("/products/{productId}/items")
    public String getProductSearch(@PathVariable String productId);

    @GetMapping("/sites/MLA/search?nickname={nickname}")
    public String getSellerByNickname(@PathVariable String nickname);

    @GetMapping("/categories/{categoryId}")
    public String getCategory(@PathVariable String categoryId);

    @GetMapping("/sites/MLA/search?nickname={nickname}&catalog_listing=true")
    public String getSellerCatalogItems(@PathVariable String nickname);

    @GetMapping("/sites/MLA/search?seller_id={seller_id}")
    public String getSellerBySellerId(@PathVariable Integer seller_id);

    @GetMapping("/items/{item_id}")
    public String getImageAndSku(@PathVariable String item_id);

//    //Puede funcionar mas a futuro
//    @GetMapping("/highlights/MLA/item/{product_id}")
//    public String getItemPosition(@PathVariable String product_id);

    @GetMapping("/highlights/MLA/category/{category_id}")
    public String getItemPositionByCategory(@PathVariable String category_id) throws ResourceNotFoundException;;
    @GetMapping("/highlights/MLA/item/{item_id}")
    public String getItemPositionByItemId(@PathVariable String item_id);

    @GetMapping("/highlights/MLA/product/{product_id}")
    public String getItemPositionByProductId(@PathVariable String product_id);

    @GetMapping("/sites/MLA/listing_types")
    public String getTypeName();


}
