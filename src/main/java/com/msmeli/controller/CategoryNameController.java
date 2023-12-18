package com.msmeli.controller;

import com.msmeli.dto.response.CatalogItemResponseDTO;
import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.model.CategoryName;
import com.msmeli.repository.CategoryNameRepository;
import com.msmeli.service.services.CategoryNameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;


@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/category")
public class CategoryNameController {

private final CategoryNameService categoryNameService;

    public CategoryNameController(CategoryNameService categoryNameService) {
        this.categoryNameService = categoryNameService;
    }

    @GetMapping("/name")
    public List<CategoryName> findAll(){
        return  categoryNameService.findAll().stream().toList();
    }
}
