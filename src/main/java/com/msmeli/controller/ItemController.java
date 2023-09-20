package com.msmeli.controller;

import com.msmeli.dto.response.CatalogItemResponseDTO;
import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.service.feignService.MeliService;
import com.msmeli.service.services.ItemService;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/item")
public class ItemController {

    private ItemService itemService;
    private MeliService meliService;

    public ItemController(ItemService itemService, MeliService meliService) {
        this.itemService = itemService;
        this.meliService = meliService;
    }

    @GetMapping("/seller/items/{sellerId}")
    public List<ItemResponseDTO> sellerItems(@PathVariable Integer sellerId){
        return itemService.getSellerItems(sellerId);
    }

    @GetMapping("/catalog/{product_catalog_id}")
    public List<CatalogItemResponseDTO> getSellerItemCatalog (@PathVariable String product_catalog_id) {
        return meliService.getSellerItemCatalog(product_catalog_id);
    }

}
