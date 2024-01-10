package com.msmeli.service.implement;

import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.AllGeneralCategory;
import com.msmeli.model.GeneralCategory;
import com.msmeli.repository.AllGeneralCategoryRepository;
import com.msmeli.service.feignService.MeliService;
import com.msmeli.service.services.AllGeneralCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllGeneralCategoryServiceImpl implements AllGeneralCategoryService {

    private final AllGeneralCategoryRepository allGeneralCategoryRepository;
    private final MeliService meliService;

    public AllGeneralCategoryServiceImpl(AllGeneralCategoryRepository allGeneralCategoryRepository, MeliService meliService){
        this.allGeneralCategoryRepository = allGeneralCategoryRepository;
        this.meliService = meliService;
    }

    @Override
    public void saveAllGeneralCategory()throws ResourceNotFoundException, AppException {
        allGeneralCategoryRepository.saveAll(meliService.findAllGeneralCategories());

    }
    @Override
    public List<AllGeneralCategory> findAll()throws ResourceNotFoundException, AppException{
        return allGeneralCategoryRepository.findAll();
    }


}



