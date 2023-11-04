package com.msmeli.controller;

import com.msmeli.dto.request.StockBySupplierRequestDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.SuppliersSellers;
import com.msmeli.service.services.SupplierService;
import com.msmeli.service.services.SuppliersSellersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meli/supplier")
public class SupplierController {

    private final SuppliersSellersService suppliersSellersService;

    public SupplierController(SuppliersSellersService suppliersSellersService) {
        this.suppliersSellersService = suppliersSellersService;
    }

    @PostMapping("/createStock")
    public ResponseEntity<List<SuppliersSellers>> createStock(@Valid @RequestBody StockBySupplierRequestDTO stockBySupplierRequestDTO) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(suppliersSellersService.create(stockBySupplierRequestDTO));
    }

    @GetMapping("/bySeller/{sellerId}")
    public ResponseEntity<List<SuppliersSellers>> findAllBySellerId(@PathVariable Long sellerId) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.FOUND).body(suppliersSellersService.findAllBySellerId(sellerId));
    }
}