package com.msmeli.controller;


import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Employee;
import com.msmeli.service.services.ItemService;
import com.msmeli.service.services.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
