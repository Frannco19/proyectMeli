package com.msmeli.service.implement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msmeli.dto.StockDTO;
import com.msmeli.dto.request.StockRequestDTO;
import com.msmeli.model.Stock;
import com.msmeli.model.UserEntity;
import com.msmeli.repository.StockRepository;
import com.msmeli.repository.UserEntityRepository;
import com.msmeli.service.services.StockService;
import jakarta.persistence.EntityNotFoundException;
import net.minidev.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public List<StockDTO> findAll() {
        return stockRepository.findAll().stream().map(stock -> modelMapper.map(stock, StockDTO.class)).toList();
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(7)
    public void stockFromJson() throws IOException {
        System.out.println(stockRepository.findAll());
        if (stockRepository.findAll().isEmpty()) {
            ClassPathResource resource = new ClassPathResource("stock_example.json");
            ObjectMapper map = new ObjectMapper();
            StockRequestDTO requestDTO = map.readValue(resource.getInputStream(), StockRequestDTO.class);
            saveUserStock(requestDTO);
        }
    }
}
