package com.msmeli.service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msmeli.dto.response.ItemDTO;
import com.msmeli.dto.response.OneProductResponseDTO;
import com.msmeli.model.Seller;

import java.util.List;

public interface ItemService {

    public List<ItemDTO> getSellerItems(Integer sellerId);

    public List<ItemDTO> getCatalogItems(String productId);

    public OneProductResponseDTO getOneProduct(String productId) throws JsonProcessingException;

}
