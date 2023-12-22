package com.msmeli.service.implement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.msmeli.dto.request.SupplierStockRequestDTO;
import com.msmeli.model.Supplier;
import com.msmeli.model.SupplierStock;
import com.msmeli.repository.SupplierStockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

public class SupplierStockServiceImplTest {

    private SupplierStockRepository supplierStockRepository;
    private ModelMapper modelMapper;
    private SupplierStockServiceImpl supplierStockService;

    @BeforeEach
    void setUp() {
        supplierStockRepository = mock(SupplierStockRepository.class);
        modelMapper = mock(ModelMapper.class);
        supplierStockService = new SupplierStockServiceImpl(supplierStockRepository, modelMapper);
    }

    @Test
    void testCreateOrUpdateSupplierStock() {
        SupplierStock supplierStock = new SupplierStock();
        supplierStock.setPrice(10.123); // Set an arbitrary price

        // Mocking behavior
        when(supplierStockRepository.save(supplierStock)).thenReturn(supplierStock);

        // Call the method to be tested
        SupplierStock result = supplierStockService.createOrUpdateSupplierStock(supplierStock);

        // Assert the result
        assertEquals(Math.round(10.123 * 100.0) / 100.0, result.getPrice());
        verify(supplierStockRepository, times(1)).save(supplierStock);
    }

    @Test
    void testCreate() {
        Supplier supplier = new Supplier();
        List<SupplierStockRequestDTO> stockDTO = Collections.singletonList(new SupplierStockRequestDTO());

        // Mocking behavior
        when(modelMapper.map(any(), eq(SupplierStock.class))).thenReturn(new SupplierStock());
        when(supplierStockRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

        // Call the method to be tested
        List<SupplierStock> result = supplierStockService.create(supplier, stockDTO);

        // Assert the result
        assertEquals(Collections.emptyList(), result);
        verify(supplierStockRepository, times(1)).saveAll(anyList());
    }
}
