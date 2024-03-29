package com.msmeli.service.implement;

import com.msmeli.model.CategoryName;
import com.msmeli.repository.CategoryNameRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryNameImplTest {

    @Mock
    private CategoryNameRepository categoryNameRepository;

    @InjectMocks
    private CategoryNameImpl categoryNameService;

    /**
     * Prueba unitaria para verificar el comportamiento del método findAll en CategoryNameService.
     * Se crean nombres de categoría simulados para realizar la prueba.
     * Se inicializan los objetos simulados necesarios para la prueba.
     * Se simula el comportamiento del método categoryNameRepository.findAll().
     * Se llama al método findAll en CategoryNameService.
     * Se verifica que el resultado coincida con los nombres de categoría simulados esperados.
     */
    @Test
    void testFindAll() {
        List<CategoryName> mockCategoryNames = Arrays.asList(
                new CategoryName("1L", "Categoría1"),
                new CategoryName("2L", "Categoría2")
        );

        MockitoAnnotations.initMocks(this);

        when(categoryNameRepository.findAll()).thenReturn(mockCategoryNames);

        List<CategoryName> result = categoryNameService.findAll();

        assertEquals(mockCategoryNames, result);
    }

}
