package com.msmeli.service.implement;

import com.msmeli.dto.StockDTO;
import com.msmeli.dto.request.StockRequestDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Stock;
import com.msmeli.model.UserEntity;
import com.msmeli.repository.StockRepository;
import com.msmeli.repository.UserEntityRepository;
import com.msmeli.service.services.StockService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final UserEntityRepository userEntityRepository;
    private final ModelMapper modelMapper;

    public StockServiceImpl(StockRepository stockRepository, UserEntityRepository userEntityRepository, ModelMapper modelMapper) {
        this.stockRepository = stockRepository;
        this.userEntityRepository = userEntityRepository;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userEntityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public void saveUserStock(StockRequestDTO requestDTO) {
        stockRepository.saveAll(requestDTO.getContent()
                .parallelStream()
                .map(e -> {
                    Stock userStock = modelMapper.map(e, Stock.class);
                    userStock.setUser_id(getUserById(requestDTO.getUser_id()));
                    userStock.setPrice(Math.round(userStock.getPrice() * 100.0) / 100.0);
                    return userStock;
                }).collect(Collectors.toList()));
    }

    @Override
    public Stock findLastBySku(String sku) {
        return stockRepository.findBySku(sku);
    }

    @Override
    public Integer getTotalStockBySku(String sku) {
        return stockRepository.getTotalBySku(sku);
    }

    @Override
    public List<StockDTO> findAllMapped(Long sellerId) {
        return stockRepository.findAllBySellerId(sellerId).stream().map(stock -> modelMapper.map(stock, StockDTO.class)).toList();
    }

    @Override
    public List<Stock> findAll(Long sellerId) throws ResourceNotFoundException {
        List<Stock> stockList = stockRepository.findAllBySellerId(sellerId);
        if(stockList.isEmpty()) throw new ResourceNotFoundException("El seller buscado no posee stock");
        return stockList;
    }
}
