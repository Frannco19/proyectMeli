package com.msmeli.service.services;

import com.msmeli.dto.request.StockBySupplierRequestDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.SuppliersSellers;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SuppliersSellersService {
    List<SuppliersSellers> create(StockBySupplierRequestDTO stockBySupplierRequestDTO) throws ResourceNotFoundException;
    List<SuppliersSellers> findAllBySellerId(Long id) throws ResourceNotFoundException;
    Page<SuppliersSellers> findAllBySellerPaged(Long sellerId, int offset, int pageSize) throws ResourceNotFoundException;
}
