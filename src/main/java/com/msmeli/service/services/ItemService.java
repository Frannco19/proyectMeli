package com.msmeli.service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msmeli.dto.response.ItemDTO;
import com.msmeli.dto.response.OneProductResponseDTO;

import java.util.List;

public interface ItemService {

    List<ItemDTO> getSellerItems(Integer sellerId);

    List<ItemDTO> getCatalogItems(String productId);

    OneProductResponseDTO getOneProduct(String productId) throws JsonProcessingException;

}
