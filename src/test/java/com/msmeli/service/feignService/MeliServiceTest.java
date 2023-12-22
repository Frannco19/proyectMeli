package com.msmeli.service.feignService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.msmeli.dto.BoxWinnerDTO;
import com.msmeli.dto.ItemAttributesDTO;
import com.msmeli.dto.ItemCatalogDTO;
import com.msmeli.dto.SellerDTO;
import com.msmeli.dto.response.BuyBoxWinnerResponseDTO;
import com.msmeli.dto.response.CatalogItemResponseDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Category;
import com.msmeli.model.Item;
import com.msmeli.model.ListingType;
import com.msmeli.model.Seller;
import com.msmeli.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MeliServiceTest {

    @Mock
    private MeliFeignClient meliFeignClient;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private CatalogItemResponseDTO catalogItemResponseDTO;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ListingTypeRepository listingTypeRepository;


    @InjectMocks
    private MeliService meliService;

    @BeforeEach
    void setUp() {
        meliFeignClient = mock(MeliFeignClient.class);
        itemRepository = mock(ItemRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        sellerRepository = mock(SellerRepository.class);
        listingTypeRepository = mock(ListingTypeRepository.class);
        objectMapper = mock(ObjectMapper.class);
        modelMapper = new ModelMapper();

        meliService = new MeliService(
                meliFeignClient,
                itemRepository,
                categoryRepository,
                sellerRepository,
                listingTypeRepository,
                objectMapper
        );
    }

    @Test
    public void testGetItemById() {
        // Configura el comportamiento simulado del repositorio de ítems
        when(itemRepository.findById("itemId")).thenReturn(Optional.of(new Item(/* Datos del ítem */)));

        // Llama al método que deseas probar
        try {
            Item item = meliService.getItemById("itemId");

            // Realiza afirmaciones sobre el resultado
            assertNotNull(item);
            // Agrega más afirmaciones según sea necesario
        } catch (Exception e) {
            fail("Excepción no esperada: " + e.getMessage());
        }
    }

    @Test
    public void testGetCategory() {
        // Configura el comportamiento simulado del repositorio de categorías
        when(categoryRepository.findById("categoryId")).thenReturn(Optional.of(new Category(/* Datos de la categoría */)));

        // Llama al método que deseas probar
        try {
            Category category = meliService.getCategory("categoryId");

            // Realiza afirmaciones sobre el resultado
            assertNotNull(category);
            // Agrega más afirmaciones según sea necesario
        } catch (Exception e) {
            fail("Excepción no esperada: " + e.getMessage());
        }
    }

    @Test
    public void testGetSeller() {
        // Configura el comportamiento simulado del repositorio de vendedores
        int sellerId = 123; // El ID del vendedor que deseas buscar
        Seller seller = new Seller(/* Datos del vendedor simulado */);
        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(seller));

        // Llama al método que deseas probar
        try {
            Seller resultSeller = meliService.getSeller(sellerId);

            // Realiza afirmaciones sobre el resultado
            assertNotNull(resultSeller);
            // Agrega más afirmaciones según sea necesario
        } catch (Exception e) {
            fail("Excepción no esperada: " + e.getMessage());
        }
    }

    @Test
    public void testGetSellerWhenSellerNotFound() {
        // Configura el comportamiento simulado del repositorio de vendedores cuando no se encuentra el vendedor
        int sellerId = 456; // Un ID de vendedor que no existe en el repositorio
        when(sellerRepository.findById(sellerId)).thenReturn(Optional.empty());

        // Llama al método que deseas probar y espera una excepción
        assertThrows(Exception.class, () -> {
            meliService.getSeller(sellerId);
        }, "Seller not found");
    }

    @Test
    public void testGetPositionMethod() {
        // Arrange
        DocumentContext documentContext = JsonPath.parse("{\"position\": 42}");

        // Act
        Integer result = meliService.getPositionMethod(documentContext);

        // Assert
        assertEquals(42, result);
    }

    @Test
    void testGetListingTypeNameFromBd() throws ResourceNotFoundException {
        // Mocking behavior
        ListingType listingType = new ListingType();
        when(listingTypeRepository.findById("listingTypeId")).thenReturn(Optional.of(listingType));

        // Call the method to be tested
        ListingType result = new MeliService(null, null, null, null, listingTypeRepository, null)
                .getListingTypeNameFromBd("listingTypeId");

        // Assert the result
        assertEquals(listingType, result);
    }

    @Test
    void testGetListingTypeNameFromBd_ListingTypeNotFound() {
        // Mocking behavior
        when(listingTypeRepository.findById(anyString())).thenReturn(Optional.empty());

        // Call the method and assert the exception
        assertThrows(ResourceNotFoundException.class,
                () -> new MeliService(null, null, null, null, listingTypeRepository, null)
                        .getListingTypeNameFromBd("nonExistentListingTypeId"));
    }

    @Test
    void testGetBuyBoxWinner() throws ResourceNotFoundException {
        // Mocking behavior
        BoxWinnerDTO mockResultDTO = createMockBoxWinnerDTO();
        when(meliFeignClient.getProductWinnerSearch(anyString())).thenReturn(mockResultDTO);

        SellerDTO mockSellerDTO = createMockSellerDTO();
        when(meliFeignClient.getSellerBySellerId(Integer.valueOf(anyString()))).thenReturn(mockSellerDTO);

        MeliService meliService = new MeliService(meliFeignClient, null, null, null, null, null);

        // Call the method to be tested
        BuyBoxWinnerResponseDTO resultDTO = meliService.getBuyBoxWinner("productId");

        // Assert the result
        assertEquals(mockResultDTO.getBuy_box_winner().getListing_type_id(),
                resultDTO.getListing_type_id());
        assertEquals(mockSellerDTO.getSeller().getNickname(),
                resultDTO.getSeller_nickname());
        // Add more assertions based on your actual mapping logic
    }

    // Utility methods to create mock data
    private BoxWinnerDTO createMockBoxWinnerDTO() {
        BoxWinnerDTO boxWinnerDTO = new BoxWinnerDTO();
        // Set mock data
        return boxWinnerDTO;
    }

    private SellerDTO createMockSellerDTO() {
        SellerDTO sellerDTO = new SellerDTO();
        // Set mock data
        return sellerDTO;
    }

    @Test
    void testGetBuyBoxWinnerCatalog() {
        // Mocking behavior
        BoxWinnerDTO mockResultDTO = createMockBoxWinnerDTO();
        when(meliFeignClient.getProductWinnerSearch(anyString())).thenReturn(mockResultDTO);

        MeliService meliService = new MeliService(meliFeignClient, null, null, null, null, objectMapper);

        // Call the method to be tested
        BuyBoxWinnerResponseDTO resultDTO = meliService.getBuyBoxWinnerCatalog("productId");

        // Assert the result
        assertEquals(mockResultDTO.getBuy_box_winner().getListing_type_id(),
                resultDTO.getListing_type_id());
        // Add more assertions based on your actual mapping logic
    }



}
