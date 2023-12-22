package com.msmeli.service.implement;

import com.msmeli.dto.StockDTO;
import com.msmeli.dto.request.StockBySupplierRequestDTO;
import com.msmeli.dto.response.SupplierStockResponseDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.*;
import com.msmeli.repository.SuppliersSellersRepository;
import com.msmeli.service.services.StockService;
import com.msmeli.service.services.SupplierService;
import com.msmeli.service.services.SupplierStockService;
import com.msmeli.service.services.SellerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class SuppliersSellersServiceImplTest {

    @Mock
    private SuppliersSellersRepository suppliersSellersRepository;

    @Mock
    private SupplierService supplierService;

    @Mock
    private SellerService sellerService;

    @Mock
    private SupplierStockService supplierStockService;

    @Mock
    private ModelMapper mapper;

    @Mock
    private StockService stockService;

    @InjectMocks
    private SuppliersSellersServiceImpl suppliersSellersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() throws ResourceNotFoundException {
        // Arrange
        StockBySupplierRequestDTO requestDTO = new StockBySupplierRequestDTO();
        requestDTO.setSupplierId(1L);
        Supplier mockSupplier = new Supplier();
        when(supplierService.findById(1L)).thenReturn(mockSupplier);
        when(sellerService.findById(1)).thenReturn(new Seller());
        when(suppliersSellersRepository.filterSupplierSeller(anyLong(), anyLong(), any())).thenReturn(Optional.empty());
        when(mapper.map(any(), eq(SupplierStock.class))).thenReturn(new SupplierStock());
        when(supplierStockService.createOrUpdateSupplierStock(any())).thenReturn(new SupplierStock());

        // Act
        List<SuppliersSellers> result = suppliersSellersService.create(requestDTO);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty()); // Modify as needed based on your business logic
        verify(suppliersSellersRepository, times(requestDTO.getContent().size())).save(any());
    }

    @Test
    void testFindAllBySellerId() throws ResourceNotFoundException {
        // Arrange
        Long sellerId = 1L;
        when(suppliersSellersRepository.findAllBySellerId(sellerId)).thenReturn(Collections.emptyList());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> suppliersSellersService.findAllBySellerId(sellerId));

        // Verify that mapper.map and other methods are not called when the list is empty
        verify(mapper, never()).map(any(), any());
    }

    @Test
    void testFindAllBySellerIdWithStock() throws ResourceNotFoundException {
        // Arrange
        Long sellerId = 1L;
        SuppliersSellers mockSupplierSeller = new SuppliersSellers();
        when(suppliersSellersRepository.findAllBySellerId(sellerId)).thenReturn(Collections.singletonList(mockSupplierSeller));
        when(mapper.map(any(), eq(SupplierStockResponseDTO.class))).thenReturn(new SupplierStockResponseDTO());

        // Act
        List<SupplierStockResponseDTO> result = suppliersSellersService.findAllBySellerId(sellerId);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(mapper, times(1)).map(any(), eq(SupplierStockResponseDTO.class));
    }

    @Test
    void testFindAllBySellerPaged() throws ResourceNotFoundException {
        // Arrange
        Long sellerId = 1L;
        int offset = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(offset, pageSize);
        Page<SuppliersSellers> mockPage = new PageImpl<>(Collections.emptyList());
        when(suppliersSellersRepository.getSuppliersSellersBySellerId(sellerId, pageable)).thenReturn(mockPage);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> suppliersSellersService.findAllBySellerPaged(sellerId, offset, pageSize));

        // Verify that the repository method is called
        verify(suppliersSellersRepository, times(1)).getSuppliersSellersBySellerId(sellerId, pageable);
    }

    @Test
    void testFindAllBySellerPagedWithContent() throws ResourceNotFoundException {
        // Arrange
        Long sellerId = 1L;
        int offset = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(offset, pageSize);
        SuppliersSellers mockSupplierSeller = new SuppliersSellers();
        Page<SuppliersSellers> mockPage = new PageImpl<>(Collections.singletonList(mockSupplierSeller));
        when(suppliersSellersRepository.getSuppliersSellersBySellerId(sellerId, pageable)).thenReturn(mockPage);

        // Act
        Page<SuppliersSellers> result = suppliersSellersService.findAllBySellerPaged(sellerId, offset, pageSize);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(mockPage, result);

        // Verify that the repository method is called
        verify(suppliersSellersRepository, times(1)).getSuppliersSellersBySellerId(sellerId, pageable);
    }

    @Test
    void testGetStockAndSupplierStock() throws ResourceNotFoundException {
        // Arrange
        Long sellerId = 1L;
        Stock mockStock = new Stock();
        mockStock.setSku("SKU123");
        when(stockService.findAll(sellerId)).thenReturn(Collections.singletonList(mockStock));
        when(mapper.map(mockStock, StockDTO.class)).thenReturn(new StockDTO());
        when(suppliersSellersRepository.findBySkuAndSellerId(mockStock.getSku(), sellerId)).thenReturn(Optional.empty());

        // Act
        List<StockDTO> result = suppliersSellersService.getStockAndSupplierStock(sellerId);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        StockDTO stockDTO = result.get(0);
        assertNotNull(stockDTO);
        assertEquals(mockStock.getSku(), stockDTO.getSku());

        // Verify that mapper.map and other methods are called
        verify(mapper, times(1)).map(mockStock, StockDTO.class);
        verify(suppliersSellersRepository, times(1)).findBySkuAndSellerId(mockStock.getSku(), sellerId);
    }

    @Test
    void testGetStockAndSupplierStockPaged() throws ResourceNotFoundException {
        // Arrange
        Long sellerId = 1L;
        int offset = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(offset, pageSize);
        Page<Stock> mockStockPage = new PageImpl<>(Collections.emptyList());

        // Mock the behavior of the dependent methods
        when(stockService.findAllPaged(sellerId, pageable)).thenReturn(mockStockPage);
        when(mapper.map(any(), eq(StockDTO.class))).thenReturn(new StockDTO());
        when(suppliersSellersRepository.findBySkuAndSellerId(any(), eq(sellerId))).thenReturn(Optional.empty());

        // Act
        Page<StockDTO> result = suppliersSellersService.getStockAndSupplierStock(sellerId, offset, pageSize);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify that stockService.findAllPaged and other methods are called
        verify(stockService, times(1)).findAllPaged(sellerId, pageable);
        verify(mapper, times(0)).map(any(), eq(StockDTO.class)); // Verify indirectly through the public method
        verify(suppliersSellersRepository, times(0)).findBySkuAndSellerId(any(), eq(sellerId)); // Verify indirectly through the public method
    }





    }
