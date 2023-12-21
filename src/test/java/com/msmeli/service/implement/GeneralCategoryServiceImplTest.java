package com.msmeli.service.implement;

import com.msmeli.dto.TopSoldDetailedProductDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.GeneralCategory;
import com.msmeli.repository.GeneralCategoryRepository;
import com.msmeli.service.feignService.MeliService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GeneralCategoryServiceImplTest {

    @InjectMocks
    private GeneralCategoryServiceImpl generalCategoryService;

    @Mock
    private GeneralCategoryRepository generalCategoryRepository;

    @Mock
    private MeliService meliService;

    @Test
    public void testFindAll() throws ResourceNotFoundException {
        // Crear objetos mock
        GeneralCategory category1 = new GeneralCategory();
        GeneralCategory category2 = new GeneralCategory();
        List<GeneralCategory> mockCategories = Arrays.asList(category1, category2);

        // Configurar los mocks
        when(generalCategoryRepository.findAll()).thenReturn(mockCategories);

        // Llamar al método que se está probando
        List<GeneralCategory> result = generalCategoryService.findAll();

        // Verificar el resultado
        assertEquals(mockCategories, result);
    }

}
