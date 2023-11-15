package com.msmeli.service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.dto.response.OneProductResponseDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Item;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ItemService {

    Page<ItemResponseDTO> getSellerItems(Integer sellerId, int offset, int pageSize);

    OneProductResponseDTO getOneProduct(String productId) throws ResourceNotFoundException;

    Page<ItemResponseDTO> getCatalogItems(Integer sellerId, int offset, int pageSize);

    List<ItemResponseDTO> getItems();

    List<Item> findAll();

    Item save(Item item);

    Page<ItemResponseDTO> searchProducts(String searchType, String searchInput, int offset, int pageSize, boolean isCatalogue, String isActive) throws ResourceNotFoundException;

    void createProductsCosts();

    Page<ItemResponseDTO> getItemsAndCostPaged(Integer id, int offset, int pageSize) throws ResourceNotFoundException;
}
