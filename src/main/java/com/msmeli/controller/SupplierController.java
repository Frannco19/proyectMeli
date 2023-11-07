package com.msmeli.controller;

import com.msmeli.dto.StockDTO;
import com.msmeli.dto.request.StockBySupplierRequestDTO;
import com.msmeli.dto.response.SupplierStockResponseDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.SuppliersSellers;
import com.msmeli.service.services.SuppliersSellersService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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

    @GetMapping("/bySeller")
    public ResponseEntity<Page<SuppliersSellers>> findAllBySellerId(@RequestParam("sellerId") Long sellerId, @RequestParam(value = "offset", defaultValue = "0") int offset, @RequestParam(value = "pageSize", defaultValue = "50", required = false) int pageSize) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(suppliersSellersService.findAllBySellerPaged(sellerId, offset, pageSize));
    }

    @GetMapping("/bySeller/{sellerId}")
    public  ResponseEntity<List<SupplierStockResponseDTO>> findAllBySellerId(@PathVariable Long sellerId) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(suppliersSellersService.findAllBySellerId(sellerId));
    }

    @GetMapping("/bySellerStock/{sellerId}")
    public ResponseEntity<List<StockDTO>> getStockAndSupplierStock(@PathVariable long sellerId) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(suppliersSellersService.getStockAndSupplierStock(sellerId));
    }
}