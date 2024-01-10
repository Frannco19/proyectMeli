package com.msmeli.controller;

import com.msmeli.dto.TopSoldDetailedProductDTO;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.AllGeneralCategory;
import com.msmeli.model.GeneralCategory;
import com.msmeli.service.services.AllGeneralCategoryService;
import com.msmeli.service.services.GeneralCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(origins = "http://201.216.243.146:10080")
//@CrossOrigin(origins = "http://localhost:4200")
//@CrossOrigin(origins = "https://ml.gylgroup.com")
@RequestMapping("/metrics")
public class MetricsController {

    private final GeneralCategoryService categoryService;
    private final AllGeneralCategoryService generalCategoryService;

    public MetricsController(GeneralCategoryService categoryService, AllGeneralCategoryService generalCategoryService) {
        this.categoryService = categoryService;
        this.generalCategoryService = generalCategoryService;
    }

    @GetMapping("/listCategories")
    public ResponseEntity<List<GeneralCategory>> createAll() throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(categoryService.findAll());
    }

    @GetMapping("/topSold/{id}")
    public ResponseEntity<List<TopSoldDetailedProductDTO>> getTopProductsByCategory(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(categoryService.getTopProductsByCategory(id));
    }

    @PostMapping("save")
    public void saveAllGeneralCategory()throws ResourceNotFoundException, AppException {
        generalCategoryService.saveAllGeneralCategory();
    }

    @GetMapping("listAllCategory")
    public List<AllGeneralCategory> getAllGeneralCategories()throws ResourceNotFoundException, AppException {
        return generalCategoryService.findAll();
    }
}
