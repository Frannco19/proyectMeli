package com.msmeli.controller;

import com.msmeli.dto.StockDTO;
import com.msmeli.dto.request.StockRequestDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.SellerRefactor;
import com.msmeli.model.Stock;
import com.msmeli.service.implement.StockServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        stockService.saveSellerStock(requestDTO);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<StockDTO>>findAllByAuthenticatedUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){

        Pageable pageable = PageRequest.of(page, size);
        Page<StockDTO> stockPage = stockService.findAllByAuthenticatedUser(pageable);

        return new ResponseEntity<>(stockPage, HttpStatus.OK);
    }
}
