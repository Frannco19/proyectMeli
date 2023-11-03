package com.msmeli.service.implement;

import com.msmeli.dto.request.StockBySupplierRequestDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Supplier;
import com.msmeli.model.SupplierStock;
import com.msmeli.repository.SupplierRepository;
import com.msmeli.service.services.SupplierService;
import com.msmeli.service.services.SupplierStockService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierStockService supplierStockService;

    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierStockService supplierStockService) {
        this.supplierRepository = supplierRepository;
        this.supplierStockService = supplierStockService;
    }

    public List<SupplierStock> uploadSupplierStock(StockBySupplierRequestDTO stockBySupplierRequestDTO) throws ResourceNotFoundException {
        Supplier supplier = supplierRepository.findById(stockBySupplierRequestDTO.getSupplierId()).orElseThrow(() -> new ResourceNotFoundException("No supplier"));
        return supplierStockService.create(supplier, stockBySupplierRequestDTO.getSupplierStock());
    }
}
