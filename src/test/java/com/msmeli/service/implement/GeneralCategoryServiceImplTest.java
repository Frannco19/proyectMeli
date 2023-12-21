package com.msmeli.service.implement;

import com.msmeli.dto.TopSoldDetailedProductDTO;
import com.msmeli.dto.TopSoldProductCategoryDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.GeneralCategory;
import com.msmeli.repository.GeneralCategoryRepository;
import com.msmeli.service.feignService.MeliService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeneralCategoryServiceImplTest {

    @Mock
    private GeneralCategoryRepository generalCategoryRepository;

    @Mock
    private MeliService meliService;

    @InjectMocks
    private GeneralCategoryServiceImpl generalCategoryService;

    @Test
    public void testCreateAll() {
        // Configurar comportamiento simulado para meliService y generalCategoryRepository
        when(meliService.findGeneralCategories()).thenReturn(getMockedCategories());
        when(meliService.getTopProductsByCategory(anyString())).thenReturn((TopSoldProductCategoryDTO) getMockedTopProducts());
        when(generalCategoryRepository.saveAll(anyList())).thenReturn(getMockedSavedCategories());


        // Llamar al método que estás probando
        generalCategoryService.createAll();

        // Verificar que los métodos necesarios se hayan llamado
        verify(meliService, atLeastOnce()).findGeneralCategories();
        verify(meliService, atLeastOnce()).getTopProductsByCategory(anyString());
        verify(generalCategoryRepository, atLeastOnce()).saveAll(anyList());

        // Realizar otras verificaciones según tus necesidades
    }

    @Test
    public void testFindAll() throws ResourceNotFoundException {
        // Configurar comportamiento simulado para generalCategoryRepository
        when(generalCategoryRepository.findAll()).thenReturn(getMockedCategories());

        // Llamar al método que estás probando
        List<GeneralCategory> result = generalCategoryService.findAll();

        // Verificar que el método findAll de generalCategoryRepository se haya llamado
        verify(generalCategoryRepository, atLeastOnce()).findAll();

        // Realizar otras verificaciones según tus necesidades
    }

    @Test
    public void testGetTopProductsByCategory() {
        // Configurar comportamiento simulado para meliService
        when(meliService.getTopProductsByCategory(anyString())).thenReturn((TopSoldProductCategoryDTO) getMockedTopProducts());

        // Llamar al método que estás probando
        List<TopSoldDetailedProductDTO> result = generalCategoryService.getTopProductsByCategory("categoryId");

        // Verificar que el método getTopProductsByCategory de meliService se haya llamado
        verify(meliService, atLeastOnce()).getTopProductsByCategory("categoryId");

        // Realizar otras verificaciones según tus necesidades
    }

    // Métodos privados para devolver datos mockeados (puedes personalizar según tus necesidades)

    private List<GeneralCategory> getMockedCategories() {
        // Crea y devuelve una lista de categorías mockeadas
        List<GeneralCategory> mockedCategories = new ArrayList<>();

        // Puedes agregar tantas categorías como necesites para tus pruebas
        GeneralCategory category1 = new GeneralCategory();
        category1.setId("1");
        category1.setName("Category 1");
        mockedCategories.add(category1);

        GeneralCategory category2 = new GeneralCategory();
        category2.setId("2");
        category2.setName("Category 2");
        mockedCategories.add(category2);

        return mockedCategories;
    }

    private List<TopSoldDetailedProductDTO> getMockedTopProducts() {
        // Crea y devuelve una lista de productos mockeados
        List<TopSoldDetailedProductDTO> mockedTopProducts = new ArrayList<>();

        // Puedes agregar tantos productos como necesites para tus pruebas
        TopSoldDetailedProductDTO product1 = new TopSoldDetailedProductDTO();
        product1.setId("1");
        product1.setName("Product 1");
        mockedTopProducts.add(product1);

        TopSoldDetailedProductDTO product2 = new TopSoldDetailedProductDTO();
        product2.setId("2");
        product2.setName("Product 2");
        mockedTopProducts.add(product2);

        return mockedTopProducts;
    }

    private List<GeneralCategory> getMockedSavedCategories() {
        // Crea y devuelve una lista de categorías que se guardarían
        List<GeneralCategory> mockedSavedCategories = new ArrayList<>();

        // Puedes agregar tantas categorías como necesites para tus pruebas
        GeneralCategory savedCategory1 = new GeneralCategory();
        savedCategory1.setId("1");
        savedCategory1.setName("Saved Category 1");
        mockedSavedCategories.add(savedCategory1);

        GeneralCategory savedCategory2 = new GeneralCategory();
        savedCategory2.setId("2");
        savedCategory2.setName("Saved Category 2");
        mockedSavedCategories.add(savedCategory2);

        return mockedSavedCategories;
    }}