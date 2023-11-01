package com.msmeli.service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.dto.response.OneProductResponseDTO;
import com.msmeli.model.Item;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ItemService {

    public Page<ItemResponseDTO> getSellerItems(Integer sellerId, int offset, int pageSize);

//    public List<ItemResponseDTO> getCatalogItems(String productId);

    public OneProductResponseDTO getOneProduct(String productId) throws JsonProcessingException;

    public Page<ItemResponseDTO> getCatalogItems(Integer sellerId, int offset, int pageSize);
    List<ItemResponseDTO> getItems();
    List<Item> findAll();
    Item save(Item item);
    List<Item> searchProducts(String searchType, String searchInput);


}
