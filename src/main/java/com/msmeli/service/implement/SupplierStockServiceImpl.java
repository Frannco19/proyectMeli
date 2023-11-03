package com.msmeli.service.implement;

import com.msmeli.dto.request.SupplierStockRequestDTO;
import com.msmeli.model.Supplier;
import com.msmeli.model.SupplierStock;
import com.msmeli.repository.SupplierStockRepository;
import com.msmeli.service.services.SupplierStockService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierStockServiceImpl implements SupplierStockService {

    private final SupplierStockRepository supplierStockRepository;
    private final ModelMapper mapper;

    public SupplierStockServiceImpl(SupplierStockRepository supplierStockRepository, ModelMapper mapper) {
        this.supplierStockRepository = supplierStockRepository;
        this.mapper = mapper;
    }

    public List<SupplierStock> create(Supplier supplier, List<SupplierStockRequestDTO> stockDTO) {
        List<SupplierStock> supplierStock = stockDTO.stream().map(item -> {
            SupplierStock stock = mapper.map(item, SupplierStock.class);
            stock.setSupplier(supplier);
            return stock;
        }).toList();
        return supplierStockRepository.saveAll(supplierStock);
    }
}
