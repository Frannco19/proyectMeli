package com.msmeli.service.services;

import com.msmeli.dto.response.ItemDTO;

import java.util.List;

public interface ItemService {

    public List<ItemDTO> getSellerItems(Integer sellerId);

    public List<ItemDTO> getCatalogItems(String productId);

}
