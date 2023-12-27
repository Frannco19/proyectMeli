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

    @Override
    public SupplierStock createOrUpdateSupplierStock(SupplierStock supplierStock) {
        supplierStock.setPrice(Math.round(supplierStock.getPrice() * 100.0) / 100.0);
        return supplierStockRepository.save(supplierStock);
    }

    @Override
    public List<SupplierStock> create(Supplier supplier, List<SupplierStockRequestDTO> stockDTO) {
        List<SupplierStock> supplierStock = stockDTO.stream().map(item -> mapper.map(item, SupplierStock.class)).toList();
        return supplierStockRepository.saveAll(supplierStock);
    }
}
