package com.msmeli.service.implement;

import com.msmeli.dto.TopSoldDetailedProductDTO;
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
        generalCategoryRepository.saveAll(meliService.findGeneralCategories().parallelStream().map(category -> {
            int totalSold = 0;
            double totalCost = .0;
            for (TopSoldDetailedProductDTO product : getTopProductsByCategory(category.getId())) {
                totalSold += product.getSold_quantity();
                if (product.getBuy_box_winner() != null) {
                    totalCost += product.getBuy_box_winner().getPrice() * product.getSold_quantity();
                }
            }
            category.setTotalSold(totalSold);
            category.setAverageSoldPrice(Math.round((totalCost / totalSold) * 100.0) / 100.0);
            return category;
        }).toList());
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
        meliService.getTopProductsByCategory(id).getContent().parallelStream().forEach(product -> {
            if (product.getType().equals("PRODUCT"))
                detailedProducts.add(meliService.getTopProductDetails(product.getId()));
        });
        return detailedProducts;
    }

}
