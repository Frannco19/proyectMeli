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
    void testCreateAll() {
        // Arrange
        List<GeneralCategory> mockCategories = new ArrayList<>(); // Populate with mock data
        when(meliService.findGeneralCategories()).thenReturn(mockCategories);

        // Act
        assertDoesNotThrow(() -> generalCategoryService.createAll());

        // Assert
        verify(generalCategoryRepository, times(1)).saveAll(anyList());
    }
    @Test
    void testFindAllThrowsResourceNotFoundException() {
        // Arrange
        when(generalCategoryRepository.findAll()).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> generalCategoryService.findAll());
    }

}
