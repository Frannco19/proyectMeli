package com.msmeli.controller;

import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.service.services.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/item")
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/seller/items/{sellerId}")
    public List<ItemResponseDTO> sellerItems(@PathVariable Integer sellerId){
        return itemService.getSellerItems(sellerId);
    }

    @GetMapping("/catalog/{productId}")
    public List<ItemResponseDTO> catalogItems(@PathVariable String productId){
        return itemService.getCatalogItems(productId);
    }

}
