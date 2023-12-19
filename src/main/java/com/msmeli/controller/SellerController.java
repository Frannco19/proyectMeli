package com.msmeli.controller;


import com.msmeli.dto.response.TokenResposeDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Employee;
import com.msmeli.service.services.ItemService;
import com.msmeli.service.services.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/seller")
public class SellerController {

    private final SellerService sellerService;
    private final ItemService itemService;

    public SellerController(SellerService sellerService, ItemService itemService) {
        this.sellerService = sellerService;
        this.itemService = itemService;
    }

    @PostMapping("/tokenForTG")
    public void tokenForTg(@RequestParam String TG){
        sellerService.saveToken(TG);
    }

    @PostMapping("/saveAllItemForSeller")
    public void prueba() throws ResourceNotFoundException {
        itemService.saveAllItemForSeller();

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResposeDTO> refreshToken() {
        try {
            TokenResposeDTO refreshedToken = sellerService.refreshToken();
            return new ResponseEntity<>(refreshedToken, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/update-token")
    public ResponseEntity<TokenResposeDTO> updateToken(@RequestParam String TG) {
        try {
            TokenResposeDTO updatedToken = sellerService.updateToken(TG);
            return new ResponseEntity<>(updatedToken, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/update-access-token")
    public ResponseEntity<String> updateAccessToken(@RequestParam String newAccessToken) {
        try {
            sellerService.updateAccessToken(newAccessToken);
            return new ResponseEntity<>("Access Token actualizado exitosamente.", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("No se encontró al vendedor en la base de datos.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/employeesInfo/{sellerId}")
    public ResponseEntity<Map<String, Object>> getEmployeesInfoBySellerId(@PathVariable Long sellerId) {
        Map<String, Object> employeesInfo = sellerService.getEmployeesInfoBySellerId(sellerId);
        return ResponseEntity.ok(employeesInfo);
    }

    @GetMapping("/employees/all")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = sellerService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
}
