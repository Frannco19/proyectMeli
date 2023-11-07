package com.msmeli.controller;

import com.msmeli.dto.StockDTO;
import com.msmeli.dto.request.StockRequestDTO;
import com.msmeli.service.implement.StockServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockServiceImpl stockService;

    public StockController(StockServiceImpl stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/save")
    public void saveUserStock(@RequestBody StockRequestDTO requestDTO) {
        stockService.saveUserStock(requestDTO);
    }

    @GetMapping("/list/{sellerId}")
    public ResponseEntity<List<StockDTO>> findAll(@PathVariable Long sellerId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(stockService.findAllMapped(sellerId));
    }
}
