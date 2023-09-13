package com.msmeli.controller;

import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.service.services.ItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/seller/items/{seller_id}")
    public List<ItemResponseDTO> sellerItems(@PathVariable Integer seller_id){
        return itemService.getSellerItems(seller_id);
    }


}
