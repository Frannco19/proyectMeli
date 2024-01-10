package com.msmeli.service.services;

import com.msmeli.dto.StockDTO;
import com.msmeli.dto.request.StockRequestDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Stock;
import com.msmeli.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StockService {
    UserEntity getUserById(Long id);
    void saveSellerStock(StockRequestDTO requestDTO);
    Stock findLastBySku(String sku);
    Integer getTotalStockBySku(String sku);
    List<StockDTO> findAllMapped(Long sellerId);
    List<Stock> findAll(Long sellerId) throws ResourceNotFoundException;
    Page<Stock> findAllPaged(Long sellerId, Pageable pageable) throws ResourceNotFoundException;
//    List<StockDTO>findAllByAuthenticatedUser();
    Page<StockDTO>findAllByAuthenticatedUser(Pageable pageable);
}

