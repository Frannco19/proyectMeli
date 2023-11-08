package com.msmeli.feignClient;

import com.msmeli.configuration.feign.FeignClientConfiguration;
import com.msmeli.dto.*;
import com.msmeli.dto.request.RefreshTokenRequestDTO;
import com.msmeli.dto.request.description.DescriptionCatalogDTO;
import com.msmeli.dto.request.description.DescriptionProductDTO;
import com.msmeli.dto.response.FeeResponseDTO;
import com.msmeli.dto.response.OptionsDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.CategoryName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.msmeli.MsMeliApplication.MELI_URL;

@FeignClient(name = "Meli", url = MELI_URL, configuration = FeignClientConfiguration.class)
public interface MeliFeignClient {

    @GetMapping("/products/{productId}/items")
    public ItemCatalogDTO getProductSearch(@PathVariable String productId);

    @GetMapping("/products/{productId}")
    public BoxWinnerDTO getProductWinnerSearch(@PathVariable String productId);

    @GetMapping("/sites/MLA/search?nickname={nickname}&catalog_listing=true&offset={offset}")
    public SellerDTO getSellerByNickname(@PathVariable String nickname, @PathVariable int offset);

    @GetMapping("/categories/{categoryId}")
    public String getCategory(@PathVariable String categoryId);

    @GetMapping("/sites/MLA/search?nickname={nickname}&catalog_listing=true")
    public SellerDTO getSellerCatalogItems(@PathVariable String nickname);

    @GetMapping("/sites/MLA/search?seller_id={seller_id}")
    public SellerDTO getSellerBySellerId(@PathVariable Integer seller_id);

    @GetMapping("/items/{item_id}")
    public ItemAttributesDTO getItemAtributtes(@PathVariable String item_id);

//    //Puede funcionar mas a futuro
//    @GetMapping("/highlights/MLA/item/{product_id}")
//    public String getBestSellerPosition(@PathVariable String product_id);

    @GetMapping("/highlights/MLA/category/{category_id}")
    public String getItemPositionByCategory(@PathVariable String category_id) throws ResourceNotFoundException;

    ;

    @GetMapping("/highlights/MLA/item/{item_id}")
    public String getItemPositionByItemId(@PathVariable String item_id);

    @GetMapping("/highlights/MLA/product/{product_id}")
    public String getItemPositionByProductId(@PathVariable String product_id);

    @GetMapping("/sites/MLA/listing_types")
    public String getTypeName();

    @GetMapping("/sites/MLA/listing_types")
    public List<ListingTypeDTO> saveListingTypes();

    @GetMapping("/products/{productId}")
    public String getBuyBoxWinner(@PathVariable String productId);

    @PostMapping("/oauth/token")
    public RefreshTokenDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenDTO);

    @GetMapping("/sites/MLA/listing_prices")
    public FeeResponseDTO getItemFee(@RequestParam("price") double price, @RequestParam("category_id") String category_id, @RequestParam("listing_type_id") String listing_type_id);

    @GetMapping("/items/{itemId}/shipping_options?zip_code=1804")
    OptionsDTO getShippingCostDTO(@PathVariable String itemId);

    @GetMapping("/products/{catalog_Product_Id}")
    DescriptionCatalogDTO getCatalogDescription(@PathVariable String catalog_Product_Id);

    @GetMapping("/items/{itemId}/description")
    DescriptionProductDTO getProductDescription(@PathVariable String itemId);

    @GetMapping("/sites/MLA/categories")
    List<CategoryName> getCategoryName();
}
