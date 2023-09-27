package com.msmeli.service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.dto.response.OneProductResponseDTO;

import java.util.List;

public interface ItemService {

    public List<ItemResponseDTO> getSellerItems(Integer sellerId);

//    public List<ItemResponseDTO> getCatalogItems(String productId);

    public OneProductResponseDTO getOneProduct(String productId) throws JsonProcessingException;

}
