package com.msmeli.feignClient;

import com.msmeli.configuration.feign.FeignClientConfiguration;
import com.msmeli.dto.*;
import com.msmeli.dto.feign.ItemFeignDTO;
import com.msmeli.dto.feign.ItemIdsResponseDTO;
import com.msmeli.dto.request.TokenRequestDTO;
import com.msmeli.dto.request.description.DescriptionCatalogDTO;
import com.msmeli.dto.request.description.DescriptionProductDTO;
import com.msmeli.dto.response.FeeResponseDTO;
import com.msmeli.dto.response.OptionsDTO;
import com.msmeli.dto.response.TokenResposeDTO;
import com.msmeli.model.AllGeneralCategory;
import com.msmeli.model.GeneralCategory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.msmeli.MsMeliApplication.MELI_URL;

@FeignClient(name = "Meli", url = MELI_URL, configuration = FeignClientConfiguration.class)
public interface MeliFeignClient {

    @GetMapping("/products/{productId}/items")
    public ItemCatalogDTO getProductSearch(@PathVariable String productId);
    @GetMapping("/products/{productId}/items")
    public ItemCatalogDTO getProductSearch(@PathVariable String productId,@RequestParam int limit,@RequestParam int offset);

    @GetMapping("/products/{productId}")
    public BoxWinnerDTO getProductWinnerSearch(@PathVariable String productId, @RequestHeader("Authorization") String authorization);

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
    @GetMapping("/items/{item_id}")
    public ItemFeignDTO getItemAtributtesRe(@PathVariable String item_id,@RequestHeader("Authorization") String authorization);

//    //Puede funcionar mas a futuro
//    @GetMapping("/highlights/MLA/item/{product_id}")
//    public String getBestSellerPosition(@PathVariable String product_id);

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

    /*@PostMapping("/oauth/token")
    public RefreshTokenDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenDTO);*/

    @GetMapping("/sites/MLA/listing_prices")
    public FeeResponseDTO getItemFee(@RequestParam("price") double price, @RequestParam("category_id") String category_id, @RequestParam("listing_type_id") String listing_type_id);

    @GetMapping("/items/{itemId}/shipping_options?zip_code=1804")
    OptionsDTO getShippingCostDTO(@PathVariable String itemId);

    @GetMapping("/products/{catalog_Product_Id}")
    DescriptionCatalogDTO getCatalogDescription(@PathVariable String catalog_Product_Id);

    @GetMapping("/items/{itemId}/description")
    DescriptionProductDTO getProductDescription(@PathVariable String itemId);

    @GetMapping("/sites/MLA/categories")
    List<GeneralCategory> getGeneralCategory();

    @GetMapping("sites/MLA/categories")
    List<AllGeneralCategory> getAllGeneralCategory();

    @GetMapping("/highlights/MLA/category/{id}")
    TopSoldProductCategoryDTO getTopProductsByCategory(@PathVariable String id);

    @GetMapping("/products/{productId}")
    TopSoldDetailedProductDTO getTopProductDetails(@PathVariable String productId);


    @PostMapping("/oauth/token")
    TokenResposeDTO tokenForTG(@RequestBody TokenRequestDTO tokenRequestDTO);

    @GetMapping("/users/{userId}/items/search")
    ItemIdsResponseDTO getAllIDsForSeller(
            @PathVariable("userId") int userId,
            @RequestHeader("Authorization") String authorization
    );
    @PostMapping("/oauth/token")
    TokenResposeDTO refreshToken(TokenRequestDTO tokenRequestDTO);
}
