package com.msmeli.service.services;

import com.msmeli.dto.request.StockRequestDTO;
import com.msmeli.model.Stock;
import com.msmeli.model.UserEntity;

public interface StockService {
    UserEntity getUserById(Long id);
    void saveUserStock(StockRequestDTO requestDTO);
    Stock getBySku(String sku);
}
