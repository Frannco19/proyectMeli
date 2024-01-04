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
    void createAll_shouldSaveGeneralCategories() {
        // Arrange
        List<GeneralCategory> mockCategories = createMockCategories();
        when(meliService.findGeneralCategories()).thenReturn(mockCategories);

        // Act
        generalCategoryService.createAll();

        // Assert
        verify(generalCategoryRepository, times(1)).saveAll(mockCategories);
    }

    private List<GeneralCategory> createMockCategories() {
        // Create and return a list of mock GeneralCategory objects
        // You can customize this method based on your specific needs
        // For example:
        List<GeneralCategory> categories = new ArrayList<>();
        categories.add(new GeneralCategory(/* specify constructor arguments */));
        // Add more categories as needed
        return categories;
    }

    @Test
    public void testFindAll() throws ResourceNotFoundException {
        // Crear objetos mock
        GeneralCategory category1 = new GeneralCategory();
        GeneralCategory category2 = new GeneralCategory();
        List<GeneralCategory> mockCategories = Arrays.asList(category1, category2);

        // Configurar los mocks
        Mockito.when(generalCategoryRepository.findAll()).thenReturn(mockCategories);

        // Llamar al método que se está probando
        List<GeneralCategory> result = generalCategoryService.findAll();

        // Verificar el resultado
        Assertions.assertEquals(mockCategories, result);
    }

    @Test
    void testFindAllThrowsResourceNotFoundException() {
        // Arrange
        when(generalCategoryRepository.findAll()).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> generalCategoryService.findAll());
    }

}
