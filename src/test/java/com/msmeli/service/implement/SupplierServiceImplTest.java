package com.msmeli.service.implement;

import static org.junit.jupiter.api.Assertions.*;

import com.msmeli.dto.request.StockBySupplierRequestDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Supplier;
import com.msmeli.model.SupplierStock;
import com.msmeli.repository.SupplierRepository;
import com.msmeli.service.services.SupplierStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierStockService supplierStockService;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        // Arrange
        Long supplierId = 1L;
        Supplier mockSupplier = new Supplier();
        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(mockSupplier));

        // Act
        Supplier result = null;
        try {
            result = supplierService.findById(supplierId);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Assert
        assertNotNull(result);
        assertEquals(mockSupplier, result);
    }

    @Test
    void testFindByIdWhenNotFound() {
        // Arrange
        Long supplierId = 1L;
        when(supplierRepository.findById(supplierId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> supplierService.findById(supplierId));
    }

    @Test
    void testUploadSupplierStock() {
        // Arrange
        Long supplierId = 1L;
        StockBySupplierRequestDTO requestDTO = new StockBySupplierRequestDTO();
        requestDTO.setSupplierId(supplierId);

        Supplier mockSupplier = new Supplier();
        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(mockSupplier));

        List<SupplierStock> mockStockList = Collections.singletonList(new SupplierStock());
        when(supplierStockService.create(mockSupplier, requestDTO.getContent())).thenReturn(mockStockList);

        // Act
        List<SupplierStock> result = null;
        try {
            result = supplierService.uploadSupplierStock(requestDTO);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Assert
        assertNotNull(result);
        assertEquals(mockStockList, result);
    }

    @Test
    void testUploadSupplierStockWhenSupplierNotFound() {
        // Arrange
        Long supplierId = 1L;
        StockBySupplierRequestDTO requestDTO = new StockBySupplierRequestDTO();
        requestDTO.setSupplierId(supplierId);

        when(supplierRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> supplierService.uploadSupplierStock(requestDTO));

        // Verify that supplierStockService.create is not called
        verify(supplierStockService, never()).create(any(), any());
    }
}
