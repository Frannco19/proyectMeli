package com.msmeli.service.implement;

import com.msmeli.dto.request.StockBySupplierRequestDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Seller;
import com.msmeli.model.Supplier;
import com.msmeli.model.SupplierStock;
import com.msmeli.model.SuppliersSellers;
import com.msmeli.repository.SuppliersSellersRepository;
import com.msmeli.service.services.SellerService;
import com.msmeli.service.services.SupplierService;
import com.msmeli.service.services.SupplierStockService;
import com.msmeli.service.services.SuppliersSellersService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SuppliersSellersServiceImpl implements SuppliersSellersService {

    private final SuppliersSellersRepository suppliersSellersRepository;
    private final SupplierService supplierService;
    private final SellerService sellerService;
    private final SupplierStockService supplierStockService;
    private final ModelMapper mapper;

    public SuppliersSellersServiceImpl(SuppliersSellersRepository suppliersSellersRepository, SupplierService supplierService, SellerService sellerService, SupplierStockService supplierStockService, ModelMapper mapper) {
        this.suppliersSellersRepository = suppliersSellersRepository;
        this.supplierService = supplierService;
        this.sellerService = sellerService;
        this.supplierStockService = supplierStockService;
        this.mapper = mapper;
    }

    @Override
    public List<SuppliersSellers> create(StockBySupplierRequestDTO stockBySupplierRequestDTO) throws ResourceNotFoundException {
        Supplier supplier = supplierService.findById(stockBySupplierRequestDTO.getSupplierId());
        Seller seller = sellerService.findById(1);
        return stockBySupplierRequestDTO.getSupplierStock().stream().map(supplierStockDTO -> {
            Optional<SuppliersSellers> suppliersSellersFound = suppliersSellersRepository.filterSupplierSeller(supplier.getId(), seller.getId(), supplierStockDTO.getSku());
            SupplierStock supplierStock;
            SuppliersSellers suppliersSellers;
            if (suppliersSellersFound.isPresent()) {
                supplierStock = suppliersSellersFound.get().getSupplierStock();
                supplierStock.setPrice(supplierStockDTO.getPrice());
                supplierStock.setAvailableQuantity(supplierStockDTO.getAvailableQuantity());
                suppliersSellers = suppliersSellersFound.get();
                suppliersSellers.setSupplierStock(supplierStockService.createOrUpdateSupplierStock(supplierStock));
            } else {
                suppliersSellers = new SuppliersSellers();
                suppliersSellers.setSupplier(supplier);
                suppliersSellers.setSeller(seller);
                suppliersSellers.setSupplierStock(supplierStockService.createOrUpdateSupplierStock(mapper.map(supplierStockDTO, SupplierStock.class)));
            }
            return suppliersSellersRepository.save(suppliersSellers);
        }).toList();
    }

    @Override
    public List<SuppliersSellers> findAllBySellerId(Long id) throws ResourceNotFoundException {
        List<SuppliersSellers> suppliersSellers = suppliersSellersRepository.findAllBySellerId(id);
        if (suppliersSellers.isEmpty())
            throw new ResourceNotFoundException("No hay stock de provedores para este seller");
        return suppliersSellers;
    }

    @Override
    public Page<SuppliersSellers> findAllBySellerPaged(Long id, int offset, int pageSize) throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(offset, pageSize);
        Page<SuppliersSellers> suppliersSellersPage = suppliersSellersRepository.getSuppliersSellersBySellerId(id, pageable);
        if (suppliersSellersPage.getContent().isEmpty())
            throw new ResourceNotFoundException("No hay mas elementos para mostrar");
        return suppliersSellersPage;
    }


}
