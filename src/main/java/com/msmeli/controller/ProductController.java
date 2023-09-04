package com.msmeli.controller;

import com.msmeli.service.feignService.MeliService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/meli")
public class ProductController {

    private final MeliService meliService;


    public ProductController(MeliService meliService) {
        this.meliService = meliService;
    }

    @GetMapping("/site/{site}/search/{query}")
    public List<Object> getProductSearch(@PathVariable String site,@PathVariable String query){
        return meliService.getProductSearch(query,site);
    }
}
