package com.msmeli.service.services;

import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.AllGeneralCategory;


import java.util.List;

public interface AllGeneralCategoryService {
    void saveAllGeneralCategory()throws ResourceNotFoundException, AppException;
    List<AllGeneralCategory> findAll()throws ResourceNotFoundException, AppException;
}
