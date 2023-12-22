package com.msmeli.service.implement;


import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Item;
import com.msmeli.model.SellerRefactor;
import com.msmeli.repository.ItemRepository;
import com.msmeli.repository.SellerRefactorRepository;
import com.msmeli.service.feignService.MeliService;
import com.msmeli.service.services.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private MeliFeignClient meliFeignClient;

    @Mock
    private ListingTypeService listingTypeService;

    @Mock
    private MeliService meliService;

    @Mock
    private ModelMapper mapper;

    @Mock
    private StockService stockService;

    @Mock
    private CostService costService;

    @Mock
    private SellerService sellerService;

    @Mock
    private UserEntityService userEntityService;

    @Mock
    private SellerRefactorRepository sellerRefactorRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    public void testGetSellerItems() {
        // Arrange
        Integer sellerId = 1;
        int offset = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(offset, pageSize);
        List<Item> items = new ArrayList<>(); // create a list of items for the test
        Page<Item> itemPage = new PageImpl<>(items, pageable, items.size());
        Mockito.when(itemRepository.getItemsBySellerId(ArgumentMatchers.eq(sellerId), ArgumentMatchers.eq(pageable))).thenReturn(itemPage);

        // Act
        Page<ItemResponseDTO> result = itemService.getSellerItems(sellerId, offset, pageSize);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(items.size(), result.getContent().size());
        // Add more assertions based on the behavior of your method
    }

    // Similar tests for other methods...

    @Test
    public void testSearchProducts() throws ResourceNotFoundException, AppException {
        // Arrange
        String searchType = "sku";
        String searchInput = "123";
        int offset = 0;
        int pageSize = 10;
        boolean isCatalogue = true;
        String isActive = "active";
        Long idSeller = 1L;
        Pageable pageable = PageRequest.of(offset, pageSize);
        SellerRefactor seller = new SellerRefactor(); // create a seller for the test
        Mockito.when(userEntityService.getAuthenticatedUserId()).thenReturn(idSeller);
        Mockito.when(sellerService.findById(ArgumentMatchers.eq(idSeller))).thenReturn(seller);

        // ... Mock the behavior for other dependencies ...

        Page<Item> results = new PageImpl<>(new ArrayList<>()); // create a page of items for the test
        Mockito.when(itemRepository.findByFilters(ArgumentMatchers.eq("%" + searchInput.toUpperCase() + "%"), ArgumentMatchers.eq(searchType), ArgumentMatchers.eq(-1), ArgumentMatchers.eq(isActive), ArgumentMatchers.eq(seller), ArgumentMatchers.eq(pageable)))
                .thenReturn(results);

        // Act
        Page<ItemResponseDTO> result = itemService.searchProducts(searchType, searchInput, offset, pageSize, isCatalogue, isActive);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(results.getContent().size(), result.getContent().size());
        // Add more assertions based on the behavior of your method
    }

}
