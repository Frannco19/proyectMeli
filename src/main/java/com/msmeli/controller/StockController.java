package com.msmeli.controller;

import com.msmeli.dto.request.StockRequestDTO;
import com.msmeli.service.implement.StockServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockServiceImpl stockService;

    public StockController(StockServiceImpl stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/save")
    public void saveUserStock(@RequestBody StockRequestDTO requestDTO){
        stockService.saveUserStock(requestDTO);
    }
}
