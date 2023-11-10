package com.msmeli.service.services;

import com.msmeli.dto.TopSoldDetailedProductDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.GeneralCategory;

import java.util.List;

public interface GeneralCategoryService {
    void createAll();
    List<GeneralCategory> findAll() throws ResourceNotFoundException;
    List<TopSoldDetailedProductDTO> getTopProductsByCategory(String id);
}
