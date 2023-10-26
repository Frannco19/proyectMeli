package com.msmeli.service.implement;

import com.msmeli.dto.request.StockRequestDTO;
import com.msmeli.model.Stock;
import com.msmeli.model.UserEntity;
import com.msmeli.repository.StockRepository;
import com.msmeli.repository.UserEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class StockServiceImpl {

    private final StockRepository stockRepository;
    private final UserEntityRepository userEntityRepository;

    private final ModelMapper modelMapper;

    public StockServiceImpl(StockRepository stockRepository, UserEntityRepository userEntityRepository, ModelMapper modelMapper) {
        this.stockRepository = stockRepository;
        this.userEntityRepository = userEntityRepository;
        this.modelMapper = new ModelMapper();
    }

    public UserEntity getUserById(Long id){
        return userEntityRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User with id "+id+" not found"));
    }

    public void saveUserStock(StockRequestDTO requestDTO){
         stockRepository.saveAll(requestDTO.getContent()
                 .parallelStream()
                 .map(e -> {
                    Stock userStock = modelMapper.map(e,Stock.class);
                    userStock.setUser_id(getUserById(requestDTO.getUser_id()));
                    return userStock;
        }).collect(Collectors.toList()));
    }


}
