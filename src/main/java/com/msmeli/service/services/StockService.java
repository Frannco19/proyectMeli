package com.msmeli.service.services;

import com.msmeli.dto.StockDTO;
import com.msmeli.dto.request.StockRequestDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Stock;
import com.msmeli.model.UserEntity;

import java.util.List;

public interface StockService {
    UserEntity getUserById(Long id);
    void saveUserStock(StockRequestDTO requestDTO);
    Stock findLastBySku(String sku);
    Integer getTotalStockBySku(String sku);
    List<StockDTO> findAllMapped(Long sellerId);

    List<Stock> findAll(Long sellerId) throws ResourceNotFoundException;
}
