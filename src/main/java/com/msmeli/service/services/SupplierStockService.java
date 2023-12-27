package com.msmeli.service.services;

import com.msmeli.dto.request.SupplierStockRequestDTO;
import com.msmeli.model.Supplier;
import com.msmeli.model.SupplierStock;

import java.util.List;

public interface SupplierStockService {
    List<SupplierStock> create(Supplier supplier, List<SupplierStockRequestDTO> stockDTO);
    SupplierStock createOrUpdateSupplierStock(SupplierStock supplierStock);
}
