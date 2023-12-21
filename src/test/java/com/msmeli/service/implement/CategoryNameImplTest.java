package com.msmeli.service.implement;

import static org.junit.jupiter.api.Assertions.*;

import com.msmeli.model.CategoryName;
import com.msmeli.repository.CategoryNameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryNameImplTest {

    @Mock
    private CategoryNameRepository categoryNameRepository;

    @InjectMocks
    private CategoryNameImpl categoryNameService;

    @Test
    public void testFindAll() {
        // Configurar comportamiento simulado para el mock
        CategoryName category1 = new CategoryName();
        category1.setName("NombreCategoría1");

        CategoryName category2 = new CategoryName();
        category2.setName("NombreCategoría2");

        List<CategoryName> expectedCategories = Arrays.asList(category1, category2);

        when(categoryNameRepository.findAll()).thenReturn(expectedCategories);

        // Llamar al método que estás probando
        List<CategoryName> result = categoryNameService.findAll();

// Verificar que los métodos necesarios se hayan llamado
        verify(categoryNameRepository, atLeastOnce()).findAll();

// Realizar aserciones
        assertEquals(expectedCategories, result);


    }

}
