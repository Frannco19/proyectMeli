package com.msmeli.controller;

import com.msmeli.model.CategoryName;
import com.msmeli.service.services.CategoryNameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CategoryNameControllerTest {

    @Mock
    private CategoryNameService categoryNameService;

    @InjectMocks
    private CategoryNameController categoryNameController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testFindAll() {
        // Mock data
        List<CategoryName> mockCategories = List.of(
                //new CategoryName("Category1"),
                //new CategoryName("Category2")
        );

        // Mock behavior of the service
        when(categoryNameService.findAll()).thenReturn(mockCategories);

        // Call the controller method
        List<CategoryName> result = categoryNameController.findAll();

        // Verify the result
        assertEquals(mockCategories, result);

        // Verify that the service method was called
        verify(categoryNameService, times(1)).findAll();
    }

}
