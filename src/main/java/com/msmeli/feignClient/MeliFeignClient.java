package com.msmeli.feignClient;

import com.msmeli.configuration.feign.FeignClientConfiguration;
import com.msmeli.dto.response.ItemAttributesDTO;
import com.msmeli.dto.response.ItemCatalogResponseDTO;
import com.msmeli.dto.response.SellerResponseDTO;
import com.msmeli.exception.ResourceNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import static com.msmeli.MsMeliApplication.MELI_URL;

@FeignClient(name = "Meli", url = MELI_URL, configuration = FeignClientConfiguration.class)
public interface MeliFeignClient {

    @GetMapping("/products/{productId}/items")
    public ItemCatalogResponseDTO getProductSearch(@PathVariable String productId);

    @GetMapping("/sites/MLA/search?nickname={nickname}")
    public SellerResponseDTO getSellerByNickname(@PathVariable String nickname);

    @GetMapping("/categories/{categoryId}")
    public String getCategory(@PathVariable String categoryId);

    @GetMapping("/sites/MLA/search?nickname={nickname}&catalog_listing=true")
    public String getSellerCatalogItems(@PathVariable String nickname);

    @GetMapping("/sites/MLA/search?seller_id={seller_id}")
    public SellerResponseDTO getSellerBySellerId(@PathVariable Integer seller_id);

    @GetMapping("/items/{item_id}")
    public ItemAttributesDTO getItemAtributtes(@PathVariable String item_id);



//    //Puede funcionar mas a futuro
//    @GetMapping("/highlights/MLA/item/{product_id}")
//    public String getItemPosition(@PathVariable String product_id);

    @GetMapping("/highlights/MLA/category/{category_id}")
    public String getItemPositionByCategory(@PathVariable String category_id) throws ResourceNotFoundException;;

    @GetMapping("/sites/MLA/listing_types")
    public String getTypesNames();


}
