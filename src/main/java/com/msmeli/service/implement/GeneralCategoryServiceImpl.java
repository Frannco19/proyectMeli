package com.msmeli.service.implement;

import com.msmeli.dto.TopSoldDetailedProductDTO;
import com.msmeli.dto.TopSoldProductCategoryDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.GeneralCategory;
import com.msmeli.repository.GeneralCategoryRepository;
import com.msmeli.service.feignService.MeliService;
import com.msmeli.service.services.GeneralCategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeneralCategoryServiceImpl implements GeneralCategoryService {

    private final GeneralCategoryRepository generalCategoryRepository;
    private final MeliService meliService;

    public GeneralCategoryServiceImpl(GeneralCategoryRepository generalCategoryRepository, MeliService meliService) {
        this.generalCategoryRepository = generalCategoryRepository;
        this.meliService = meliService;
    }

    @Override
    public void createAll() {
        if (generalCategoryRepository.findAll().isEmpty())
            generalCategoryRepository.saveAll(meliService.findGeneralCategories());
    }

    @Override
    public List<GeneralCategory> findAll() throws ResourceNotFoundException {
        List<GeneralCategory> categoriesFound = generalCategoryRepository.findAll();
        if (categoriesFound.isEmpty()) throw new ResourceNotFoundException("No hay categorias generales");
        return categoriesFound;
    }

    @Override
    public List<TopSoldDetailedProductDTO> getTopProductsByCategory(String id) {
        List<TopSoldDetailedProductDTO> detailedProducts = new ArrayList<>();
        meliService.getTopProductsByCategory(id).getContent().stream().forEach(product -> {
            if (product.getType().equals("PRODUCT"))
                detailedProducts.add(meliService.getTopProductDetails(product.getId()));
        });
        return detailedProducts;
    }


}
