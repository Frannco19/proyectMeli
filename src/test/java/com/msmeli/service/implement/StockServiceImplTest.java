package com.msmeli.service.implement;

import com.msmeli.dto.StockDTO;
import com.msmeli.dto.request.StockRequestDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Stock;
import com.msmeli.model.UserEntity;
import com.msmeli.repository.StockRepository;
import com.msmeli.repository.UserEntityRepository;
import com.msmeli.service.services.StockService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class StockServiceImplTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private UserEntityRepository userEntityRepository;

    @InjectMocks
    private StockServiceImpl stockService;

    @Test
    public void testSaveUserStock() {
        StockRequestDTO requestDTO = new StockRequestDTO();
        requestDTO.setUser_id(1L);
        requestDTO.setContent(Collections.singletonList(new StockDTO()));

        UserEntity userEntity = new UserEntity();
        Mockito.when(userEntityRepository.findById(1L)).thenReturn(java.util.Optional.of(userEntity));

        stockService.saveSellerStock(requestDTO);

        Mockito.verify(stockRepository, Mockito.times(1)).saveAll(Mockito.anyList());
    }

    @Test
    public void testGetUserById_UserFound() {
        Long userId = 1L;
        UserEntity expectedUser = new UserEntity();
        Mockito.when(userEntityRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        UserEntity actualUser = stockService.getUserById(userId);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testGetUserById_UserNotFound() {
        Long userId = 2L;
        Mockito.when(userEntityRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> stockService.getUserById(userId));
    }

    @Test
    public void testFindLastBySku_StockFound() {
        String sku = "ABC123";
        Stock expectedStock = new Stock();
        Mockito.when(stockRepository.findBySku(sku)).thenReturn(expectedStock);

        Stock actualStock = stockService.findLastBySku(sku);

        assertEquals(expectedStock, actualStock);
    }

    @Test
    public void testFindLastBySku_StockNotFound() {
        String sku = "XYZ789";
        Mockito.when(stockRepository.findBySku(sku)).thenReturn(null);

        Stock actualStock = stockService.findLastBySku(sku);

        assertNull(actualStock);
    }

    @Test
    public void testGetTotalStockBySku_StockFound() {
        String sku = "ABC123";
        Integer expectedTotalStock = 10;
        Mockito.when(stockRepository.getTotalBySku(sku)).thenReturn(expectedTotalStock);

        Integer actualTotalStock = stockService.getTotalStockBySku(sku);

        assertEquals(expectedTotalStock, actualTotalStock);
    }

    @Test
    public void testGetTotalStockBySku_StockNotFound() {
        String sku = "XYZ789";
        Mockito.when(stockRepository.getTotalBySku(sku)).thenReturn(null);

        Integer actualTotalStock = stockService.getTotalStockBySku(sku);

        assertNull(actualTotalStock);
    }

    @Test
    public void testFindAllMapped_StocksFound() {
        Long sellerId = 1L;

        List<Stock> mockStockList = Arrays.asList(new Stock(), new Stock());
        Mockito.when(stockRepository.findAllBySellerId(sellerId)).thenReturn(mockStockList);

        List<StockDTO> result = stockService.findAllMapped(sellerId);

        assertEquals(mockStockList.size(), result.size());
    }

    @Test
    public void testFindAllMapped_NoStocksFound() {
        Long sellerId = 2L;

        Mockito.when(stockRepository.findAllBySellerId(sellerId)).thenReturn(Collections.emptyList());

        List<StockDTO> result = stockService.findAllMapped(sellerId);

        assertEquals(0, result.size());
    }

    @Test
    public void testFindAll_StocksFound() throws ResourceNotFoundException {
        Long sellerId = 1L;

        List<Stock> mockStockList = Arrays.asList(new Stock(), new Stock());
        Mockito.when(stockRepository.findAllBySellerId(sellerId)).thenReturn(mockStockList);

        List<Stock> result = stockService.findAll(sellerId);

        assertEquals(mockStockList.size(), result.size());
    }

    @Test
    public void testFindAll_NoStocksFound() {
        Long sellerId = 2L;

        Mockito.when(stockRepository.findAllBySellerId(sellerId)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> stockService.findAll(sellerId));
    }

    @Test
    public void testFindAllPaged_StocksFound() throws ResourceNotFoundException {
        Long sellerId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        List<Stock> mockStockList = Arrays.asList(new Stock(), new Stock());
        Page<Stock> mockStockPage = new PageImpl<>(mockStockList);
        Mockito.when(stockRepository.findAllBySellerId(sellerId, pageable)).thenReturn(mockStockPage);

        Page<Stock> result = stockService.findAllPaged(sellerId, pageable);

        assertEquals(mockStockList.size(), result.getContent().size());
    }

    @Test
    public void testFindAllPaged_NoStocksFound() {
        Long sellerId = 2L;
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(stockRepository.findAllBySellerId(sellerId, pageable)).thenReturn(Page.empty());

        assertThrows(ResourceNotFoundException.class, () -> stockService.findAllPaged(sellerId, pageable));
    }

}
