package com.msmeli.controller;


import com.msmeli.model.CategoryName;
import com.msmeli.service.services.CategoryNameService;
import org.springframework.web.bind.annotation.*;
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
