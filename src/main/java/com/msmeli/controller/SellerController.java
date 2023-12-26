package com.msmeli.controller;


import com.msmeli.dto.response.EmployeesResponseDto;
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
import java.util.stream.Collectors;

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
        } catch (Exception e) {
            // Manejo de otras excepciones
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getEmployeesBySellerId")
    public ResponseEntity<List<EmployeesResponseDto>> getEmployeesBySellerId() throws ResourceNotFoundException {
        List<EmployeesResponseDto> employeesList = sellerService.getEmployeesBySellerId();
        return ResponseEntity.ok(employeesList);
    }

    @GetMapping("/getAllEmployees")
    public ResponseEntity<List<EmployeesResponseDto>> getAllEmployees() {
        List<EmployeesResponseDto> employeesList = sellerService.getAllEmployees();
        return ResponseEntity.ok(employeesList);
    }


}
