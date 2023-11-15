package com.msmeli.controller;

import com.msmeli.dto.TopSoldDetailedProductDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.GeneralCategory;
import com.msmeli.service.services.GeneralCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@CrossOrigin(origins = "http://201.216.243.146:10080")
//@CrossOrigin(origins = "http://localhost:4200")
//@CrossOrigin(origins = "https://ml.gylgroup.com")
@RequestMapping("/metrics")
public class MetricsController {

    private final GeneralCategoryService categoryService;

    public MetricsController(GeneralCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/listCategories")
    public ResponseEntity<List<GeneralCategory>> createAll() throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(categoryService.findAll());
    }

    @GetMapping("/topSold/{id}")
    public ResponseEntity<List<TopSoldDetailedProductDTO>> getTopProductsByCategory(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(categoryService.getTopProductsByCategory(id));
    }
}
