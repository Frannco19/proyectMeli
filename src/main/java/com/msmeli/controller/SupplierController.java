package com.msmeli.controller;

import com.msmeli.dto.request.StockBySupplierRequestDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.SupplierStock;
import com.msmeli.service.services.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/meli/supplier")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping("/createStock")
    public ResponseEntity<List<SupplierStock>> createStock(@RequestBody StockBySupplierRequestDTO stockBySupplierRequestDTO) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.uploadSupplierStock(stockBySupplierRequestDTO));
    }
}
