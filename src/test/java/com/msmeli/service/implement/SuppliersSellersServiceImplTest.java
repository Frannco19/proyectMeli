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

    /**
     * Pruebas para el método create de SuppliersSellersServiceImpl.
     * Se asegura de que el método create maneje correctamente el caso en el que no se
     * encuentre una relación SuppliersSellers existente, y que se realice la creación
     * adecuada de instancias y llamadas a métodos necesarios.
     */
    @Test
    void testCreate() throws ResourceNotFoundException {
        StockBySupplierRequestDTO requestDTO = new StockBySupplierRequestDTO();
        requestDTO.setSupplierId(1L);
        Supplier mockSupplier = new Supplier();
        when(supplierService.findById(1L)).thenReturn(mockSupplier);
        when(sellerService.findById(1)).thenReturn(new Seller());
        when(suppliersSellersRepository.filterSupplierSeller(anyLong(), anyLong(), any())).thenReturn(Optional.empty());
        when(mapper.map(any(), eq(SupplierStock.class))).thenReturn(new SupplierStock());
        when(supplierStockService.createOrUpdateSupplierStock(any())).thenReturn(new SupplierStock());

        List<SuppliersSellers> result = suppliersSellersService.create(requestDTO);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(suppliersSellersRepository, times(requestDTO.getContent().size())).save(any());
    }

    /**
     * Pruebas para el método findAllBySellerId de SuppliersSellersServiceImpl.
     * Verifica que el método maneje correctamente el caso en el que no se encuentren
     * SuppliersSellers para un Seller específico, lanzando una excepción
     * ResourceNotFoundException según lo esperado.
     */
    @Test
    void testFindAllBySellerId() throws ResourceNotFoundException {
        Long sellerId = 1L;
        when(suppliersSellersRepository.findAllBySellerId(sellerId)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> suppliersSellersService.findAllBySellerId(sellerId));

        verify(mapper, never()).map(any(), any());
    }

    /**
     * Pruebas para el método findAllBySellerPaged de SuppliersSellersServiceImpl.
     * Verifica que el método maneje correctamente el caso en el que no se encuentren
     * SuppliersSellers paginados para un Seller específico, lanzando una excepción
     * ResourceNotFoundException según lo esperado.
     */
    @Test
    void testFindAllBySellerPaged() throws ResourceNotFoundException {
        Long sellerId = 1L;
        int offset = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(offset, pageSize);
        Page<SuppliersSellers> mockPage = new PageImpl<>(Collections.emptyList());
        when(suppliersSellersRepository.getSuppliersSellersBySellerId(sellerId, pageable)).thenReturn(mockPage);

        assertThrows(ResourceNotFoundException.class, () -> suppliersSellersService.findAllBySellerPaged(sellerId, offset, pageSize));

        verify(suppliersSellersRepository, times(1)).getSuppliersSellersBySellerId(sellerId, pageable);
    }

    /**
     * Pruebas para el método findAllBySellerPaged de SuppliersSellersServiceImpl cuando hay contenido.
     * Se asegura de que el método devuelva correctamente una página no vacía cuando hay
     * SuppliersSellers paginados para un Seller específico, y que la función de mapeo se llame
     * la cantidad de veces esperada.
     */
    @Test
    void testFindAllBySellerPagedWithContent() throws ResourceNotFoundException {
        Long sellerId = 1L;
        int offset = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(offset, pageSize);
        SuppliersSellers mockSupplierSeller = new SuppliersSellers();
        Page<SuppliersSellers> mockPage = new PageImpl<>(Collections.singletonList(mockSupplierSeller));
        when(suppliersSellersRepository.getSuppliersSellersBySellerId(sellerId, pageable)).thenReturn(mockPage);

        Page<SuppliersSellers> result = suppliersSellersService.findAllBySellerPaged(sellerId, offset, pageSize);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(mockPage, result);

        verify(suppliersSellersRepository, times(1)).getSuppliersSellersBySellerId(sellerId, pageable);
    }

    /**
     * Pruebas para el método getStockAndSupplierStock de SuppliersSellersServiceImpl.
     * Verifica que el método devuelva correctamente una lista no vacía de StockDTO y
     * que las funciones de mapeo y búsqueda en el repositorio se llamen la cantidad
     * de veces esperada.
     */
    @Test
    void testGetStockAndSupplierStock() throws ResourceNotFoundException {
        Long sellerId = 1L;
        Stock mockStock = new Stock();
        mockStock.setSku("SKU123");
        when(stockService.findAll(sellerId)).thenReturn(Collections.singletonList(mockStock));
        when(mapper.map(mockStock, StockDTO.class)).thenReturn(new StockDTO());
        when(suppliersSellersRepository.findBySkuAndSellerId(mockStock.getSku(), sellerId)).thenReturn(Optional.empty());

        List<StockDTO> result = suppliersSellersService.getStockAndSupplierStock(sellerId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        StockDTO stockDTO = result.get(0);
        assertNotNull(stockDTO);

        verify(mapper, times(1)).map(mockStock, StockDTO.class);
        verify(suppliersSellersRepository, times(1)).findBySkuAndSellerId(mockStock.getSku(), sellerId);
    }

    /**
     * Pruebas para el método getStockAndSupplierStockPaged de SuppliersSellersServiceImpl
     * cuando la página de stock está vacía.
     * Asegura que el método devuelva correctamente una página vacía y que no se realice
     * llamada a funciones de mapeo o búsqueda en el repositorio.
     */
    @Test
    void testGetStockAndSupplierStockPaged() throws ResourceNotFoundException {
        Long sellerId = 1L;
        int offset = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(offset, pageSize);
        Page<Stock> mockStockPage = new PageImpl<>(Collections.emptyList());

        when(stockService.findAllPaged(sellerId, pageable)).thenReturn(mockStockPage);
        when(mapper.map(any(), eq(StockDTO.class))).thenReturn(new StockDTO());
        when(suppliersSellersRepository.findBySkuAndSellerId(any(), eq(sellerId))).thenReturn(Optional.empty());

        Page<StockDTO> result = suppliersSellersService.getStockAndSupplierStock(sellerId, offset, pageSize);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(stockService, times(1)).findAllPaged(sellerId, pageable);
        verify(mapper, times(0)).map(any(), eq(StockDTO.class));
        verify(suppliersSellersRepository, times(0)).findBySkuAndSellerId(any(), eq(sellerId));
    }
    }
