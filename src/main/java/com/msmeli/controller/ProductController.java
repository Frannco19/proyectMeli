package com.msmeli.controller;

import com.msmeli.model.Product;
import com.msmeli.service.databaseService.ProductService;
import com.msmeli.service.feignService.MeliService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/meli")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/getall")
    public List<Product> getAll(){
        return productService.getAllProducts();
    }
}
