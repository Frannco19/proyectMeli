package com.msmeli.service.implement;

import com.msmeli.dto.TopSoldDetailedProductDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.GeneralCategory;
import com.msmeli.repository.GeneralCategoryRepository;
import com.msmeli.service.feignService.MeliService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GeneralCategoryServiceImplTest {

    @InjectMocks
    private GeneralCategoryServiceImpl generalCategoryService;

    @Mock
    private GeneralCategoryRepository generalCategoryRepository;

    @Mock
    private MeliService meliService;


    @BeforeEach
    void setUp() {
        generalCategoryRepository = mock(GeneralCategoryRepository.class);
        meliService = mock(MeliService.class);
        generalCategoryService = new GeneralCategoryServiceImpl(generalCategoryRepository, meliService);
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void createAll_shouldSaveGeneralCategories() {
        List<GeneralCategory> mockCategories = createMockCategories();
        when(meliService.findGeneralCategories()).thenReturn(mockCategories);

        generalCategoryService.createAll();

        verify(generalCategoryRepository, times(1)).saveAll(mockCategories);
    }

    private List<GeneralCategory> createMockCategories() {
               List<GeneralCategory> categories = new ArrayList<>();
        categories.add(new GeneralCategory());
        return categories;
    }

    @Test
    public void testFindAll() throws ResourceNotFoundException {
        GeneralCategory category1 = new GeneralCategory();
        GeneralCategory category2 = new GeneralCategory();
        List<GeneralCategory> mockCategories = Arrays.asList(category1, category2);

        Mockito.when(generalCategoryRepository.findAll()).thenReturn(mockCategories);

        List<GeneralCategory> result = generalCategoryService.findAll();

        Assertions.assertEquals(mockCategories, result);
    }

    /**
     * Prueba unitaria para el método findAll en la clase GeneralCategoryService
     * cuando el repositorio devuelve una lista vacía.
     *
     * Esta prueba verifica el comportamiento del método findAll en GeneralCategoryService cuando
     * generalCategoryRepository.findAll() devuelve una lista vacía. Se espera que el método lance
     * una excepción de tipo ResourceNotFoundException en este escenario.
     */
    @Test
    void testFindAllThrowsResourceNotFoundException() {
        when(generalCategoryRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> generalCategoryService.findAll());
    }

}
