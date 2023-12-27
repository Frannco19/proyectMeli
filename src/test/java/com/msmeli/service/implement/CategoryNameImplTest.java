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

    @Test
    void testFindAll() {
        List<CategoryName> mockCategoryNames = Arrays.asList(
                new CategoryName("1L", "Category1"),
                new CategoryName("2L", "Category2")
        );

        MockitoAnnotations.initMocks(this);

        when(categoryNameRepository.findAll()).thenReturn(mockCategoryNames);

        List<CategoryName> result = categoryNameService.findAll();

        assertEquals(mockCategoryNames, result);
    }
}
