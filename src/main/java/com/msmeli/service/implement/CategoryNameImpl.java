package com.msmeli.service.implement;

import com.msmeli.model.CategoryName;
import com.msmeli.repository.CategoryNameRepository;
import com.msmeli.service.services.CategoryNameService;

import java.util.List;

public class CategoryNameImpl implements CategoryNameService {

    private final CategoryNameRepository categoryNameRepository;

    public CategoryNameImpl(CategoryNameRepository categoryNameRepository) {
        this.categoryNameRepository = categoryNameRepository;
    }

    @Override
    public List<CategoryName> findAll() {
        return categoryNameRepository.findAll();

    }
}
