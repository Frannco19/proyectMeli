package com.msmeli.service.services;

import com.msmeli.dto.StockDTO;
import com.msmeli.dto.request.StockBySupplierRequestDTO;
import com.msmeli.dto.response.SupplierStockResponseDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.SuppliersSellers;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SuppliersSellersService {
    List<SuppliersSellers> create(StockBySupplierRequestDTO stockBySupplierRequestDTO) throws ResourceNotFoundException;
    List<SupplierStockResponseDTO> findAllBySellerId(Long id) throws ResourceNotFoundException;
    Page<SuppliersSellers> findAllBySellerPaged(Long sellerId, int offset, int pageSize) throws ResourceNotFoundException;
    List<StockDTO> getStockAndSupplierStock(Long id) throws ResourceNotFoundException;
}
