package com.msmeli.service.implement;

import com.msmeli.dto.StockDTO;
import com.msmeli.dto.request.StockBySupplierRequestDTO;
import com.msmeli.dto.response.SupplierStockResponseDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.*;
import com.msmeli.repository.SuppliersSellersRepository;
import com.msmeli.service.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final StockService stockService;

    public SuppliersSellersServiceImpl(SuppliersSellersRepository suppliersSellersRepository, SupplierService supplierService, SellerService sellerService, SupplierStockService supplierStockService, ModelMapper mapper, StockService stockService) {
        this.suppliersSellersRepository = suppliersSellersRepository;
        this.supplierService = supplierService;
        this.sellerService = sellerService;
        this.supplierStockService = supplierStockService;
        this.mapper = mapper;
        this.stockService = stockService;
    }

    @Override
    public List<SuppliersSellers> create(StockBySupplierRequestDTO stockBySupplierRequestDTO) throws ResourceNotFoundException {
        Supplier supplier = supplierService.findById(stockBySupplierRequestDTO.getSupplierId());
        Seller seller = sellerService.findById(1);
        return stockBySupplierRequestDTO.getContent().stream().map(supplierStockDTO -> {
            Optional<SuppliersSellers> suppliersSellersFound = suppliersSellersRepository.filterSupplierSeller(supplier.getId(), seller.getId(), supplierStockDTO.getSku());
            SupplierStock supplierStock;
            SuppliersSellers suppliersSellers;
            if (suppliersSellersFound.isPresent()) {
                supplierStock = suppliersSellersFound.get().getSupplierStock();
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
    public List<SupplierStockResponseDTO> findAllBySellerId(Long id) throws ResourceNotFoundException {
        List<SuppliersSellers> suppliersSellers = suppliersSellersRepository.findAllBySellerId(id);
        if (suppliersSellers.isEmpty())
            throw new ResourceNotFoundException("No hay stock de provedores para este seller");
        return suppliersSellers.stream().map(stock -> {
            SupplierStockResponseDTO supplierStock = mapper.map(stock.getSupplierStock(), SupplierStockResponseDTO.class);
            supplierStock.setNickname(stock.getSupplier().getSupplierName());
            return supplierStock;
        }).toList();
    }

    @Override
    public Page<SuppliersSellers> findAllBySellerPaged(Long id, int offset, int pageSize) throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(offset, pageSize);
        Page<SuppliersSellers> suppliersSellersPage = suppliersSellersRepository.getSuppliersSellersBySellerId(id, pageable);
        if (suppliersSellersPage.getContent().isEmpty())
            throw new ResourceNotFoundException("No hay mas elementos para mostrar");
        return suppliersSellersPage;
    }

    @Override
    public List<StockDTO> getStockAndSupplierStock(Long id) throws ResourceNotFoundException {
        List<Stock> sellerStock = stockService.findAll(id);
        return sellerStock.stream().map(e -> {
            StockDTO stockDTO = mapper.map(e, StockDTO.class);
            Optional<SuppliersSellers> suppliersSellers = suppliersSellersRepository.findBySkuAndSellerId(e.getSku(), id);
            if (suppliersSellers.isPresent()) {
                stockDTO.setSupplierContent(mapper.map(suppliersSellers.get().getSupplierStock(), SupplierStockResponseDTO.class));
                stockDTO.getSupplierContent().setNickname(suppliersSellers.get().getSupplier().getSupplierName());
            }
            return stockDTO;
        }).toList();
    }


    public Page<StockDTO> getStockAndSupplierStock(Long id, int offset, int pageSize) throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(offset, pageSize);
        Page<Stock> sellerStock = stockService.findAllPaged(id, pageable);
        return pageDTO(pageable, sellerStock, id);
    }

    private Page<StockDTO> pageDTO(Pageable pageable, Page<Stock> stockPage, Long sellerId) {
        List<StockDTO> stock = stockPage.stream().map(e -> {
            StockDTO stockDTO = mapper.map(e, StockDTO.class);
            Optional<SuppliersSellers> suppliersSellers = suppliersSellersRepository.findBySkuAndSellerId(e.getSku(), sellerId);
            if (suppliersSellers.isPresent()) {
                stockDTO.setSupplierContent(mapper.map(suppliersSellers.get().getSupplierStock(), SupplierStockResponseDTO.class));
                stockDTO.getSupplierContent().setNickname(suppliersSellers.get().getSupplier().getSupplierName());
            }
            return stockDTO;
        }).toList();
        return new PageImpl<>(stock, pageable, stockPage.getTotalElements());
    }
}
