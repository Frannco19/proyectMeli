package com.msmeli.service.services;

import com.msmeli.dto.request.StockBySupplierRequestDTO;
import com.msmeli.dto.request.SupplierResquestDTO;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Supplier;
import com.msmeli.model.SupplierStock;

import java.util.List;

public interface SupplierService {
    List<SupplierStock> uploadSupplierStock(StockBySupplierRequestDTO stockBySupplierRequestDTO) throws ResourceNotFoundException;

    Supplier findById(Integer id) throws ResourceNotFoundException;
    void createSupplier(SupplierResquestDTO supplierResquestDTO) throws AppException;
    List<Supplier>listSupplier() throws AppException;
}
