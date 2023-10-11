package com.msmeli.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msmeli.dto.response.BuyBoxWinnerResponseDTO;
import com.msmeli.dto.response.CatalogItemResponseDTO;
import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.dto.response.OneProductResponseDTO;
import com.msmeli.service.feignService.MeliService;
import com.msmeli.service.services.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://201.216.243.146:10080")
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    private final MeliService meliService;

    public ItemController(ItemService itemService, MeliService meliService) {
        this.itemService = itemService;
        this.meliService = meliService;
    }

    @GetMapping("/seller/items")
    public Page<ItemResponseDTO> sellerItems(
            @RequestParam("sellerId") Integer sellerId,
            @RequestParam(value = "offset",defaultValue = "0") int offset,
            @RequestParam(value = "pageSize", defaultValue = "50", required = false) int pageSize
    ){
        return itemService.getSellerItems(sellerId,offset,pageSize);
    }

//    @GetMapping("/seller/allItems")
//    public Page<ItemResponseDTO> sellerAllItems(@RequestParam(value = "offset",defaultValue = "0") int offset){
//        return meliService.getAllSellerItems(offset);
//    }

    @GetMapping("/catalog/{product_catalog_id}")
    public List<CatalogItemResponseDTO> getSellerItemCatalog (@PathVariable String product_catalog_id) throws ParseException {
        return meliService.getSellerItemCatalog(product_catalog_id);
    }

    @GetMapping("/seller/catalog/{product_catalog_id}")
    public OneProductResponseDTO getOneCatalogProduct(@PathVariable String product_catalog_id) throws JsonProcessingException {
        return itemService.getOneProduct(product_catalog_id);
    }

    @GetMapping("/winner/{product_catalog_id}")
    public BuyBoxWinnerResponseDTO getBuyBoxWinner(@PathVariable String product_catalog_id) throws JsonProcessingException {
        return meliService.getBuyBoxWinner(product_catalog_id);
    }


}
