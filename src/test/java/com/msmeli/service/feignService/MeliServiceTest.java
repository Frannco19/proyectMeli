package com.msmeli.service.feignService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.msmeli.dto.BoxWinnerDTO;
import com.msmeli.dto.SellerDTO;
import com.msmeli.dto.response.BuyBoxWinnerResponseDTO;
import com.msmeli.dto.response.CatalogItemResponseDTO;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Category;
import com.msmeli.model.Item;
import com.msmeli.model.ListingType;
import com.msmeli.model.Seller;
import com.msmeli.repository.*;
import com.msmeli.service.services.SellerService;
import com.msmeli.service.services.UserEntityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
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
    private UserEntityService userEntityService;
    @Mock
    private SellerService sellerService;

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ListingTypeRepository listingTypeRepository;


    @InjectMocks
    private MeliService meliService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }




    @Test
    public void testGetItemById() {
        Item expectedItem = new Item();
        Mockito.when(itemRepository.findById("itemId")).thenReturn(Optional.of(expectedItem));

        try {
            MeliService meliService = new MeliService(
                    meliFeignClient,
                    itemRepository,
                    categoryRepository,
                    sellerRepository,
                    listingTypeRepository,
                    userEntityService,
                    sellerService,
                    objectMapper
            );

            Item actualItem = meliService.getItemById("itemId");

            assertNotNull(actualItem);
            assertEquals(expectedItem, actualItem);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetCategory() {
        Category expectedCategory = new Category();
        Mockito.when(categoryRepository.findById("categoryId")).thenReturn(Optional.of(expectedCategory));

        try {
            MeliService meliService = new MeliService(
                    meliFeignClient,
                    itemRepository,
                    categoryRepository,
                    sellerRepository,
                    listingTypeRepository,
                    userEntityService,
                    sellerService,
                    objectMapper
            );

            Category actualCategory = meliService.getCategory("categoryId");

            assertNotNull(actualCategory);
            assertEquals(expectedCategory, actualCategory);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Prueba unitaria para verificar el comportamiento del método getSeller en MeliService.
     * Se establece un identificador de vendedor (sellerId) y se crea un objeto Seller esperado.
     * Se configura el mock del sellerRepository para devolver el objeto esperado cuando se llama a findById.
     * Se instancia un servicio de Meli con los mocks y objetos simulados necesarios para la prueba.
     * Se llama al método que se está probando, en este caso, getSeller.
     * Se verifica que el resultado no sea nulo y que sea igual al vendedor esperado.
     * Se manejan las excepciones inesperadas para evitar que la prueba falle de manera incontrolada.
     */
    @Test
    public void testGetSeller() {
        int sellerId = 123;
        Seller expectedSeller = new Seller();
        Mockito.when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(expectedSeller));

        try {
            MeliService meliService = new MeliService(
                    meliFeignClient,
                    itemRepository,
                    categoryRepository,
                    sellerRepository,
                    listingTypeRepository,
                    userEntityService,
                    sellerService,
                    objectMapper
            );

            Seller actualSeller = meliService.getSeller(sellerId);

            assertNotNull(actualSeller);
            assertEquals(expectedSeller, actualSeller);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSellerWhenSellerNotFound() {
        int sellerId = 456;
        Mockito.when(sellerRepository.findById(sellerId)).thenReturn(Optional.empty());


        Assertions.assertThrows(Exception.class, () -> {
            meliService.getSeller(sellerId);
        }, "Seller not found");
    }

    /**
     * Prueba unitaria para verificar el comportamiento del método getPositionMethod en MeliService.
     * Se crea un contexto de documento JSON utilizando JsonPath con un objeto que contiene la propiedad "position" con el valor 42.
     * Se llama al método que se está probando, en este caso, getPositionMethod.
     * Se verifica que el resultado del método sea igual a 42.
     */
    @Test
    public void testGetPositionMethod() {
        DocumentContext documentContext = JsonPath.parse("{\"position\": 42}");

        Integer result = meliService.getPositionMethod(documentContext);

        Assertions.assertEquals(42, result);
    }

    /**
     * Prueba unitaria para verificar el comportamiento del método getListingTypeNameFromBd en MeliService.
     * Se establece un comportamiento simulado utilizando mocks para el listingTypeRepository,
     * donde se devuelve un objeto ListingType cuando se llama al método findById con el identificador "listingTypeId".
     * Se llama al método que se está probando, en este caso, getListingTypeNameFromBd.
     * Se verifica que el resultado del método sea igual al objeto ListingType esperado.
     *
     * @throws ResourceNotFoundException Excepción esperada si el recurso no es encontrado.
     */
    @Test
    void testGetListingTypeNameFromBd() throws ResourceNotFoundException {
        // Mocking behavior
        ListingType listingType = new ListingType();
        when(listingTypeRepository.findById("listingTypeId")).thenReturn(Optional.of(listingType));

        ListingType result = new MeliService(null, null, null, null, listingTypeRepository, null, null, null)
                .getListingTypeNameFromBd("listingTypeId");

        assertEquals(listingType, result);
    }

    /**
     * Prueba unitaria para verificar el comportamiento del método getListingTypeNameFromBd en MeliService
     * cuando el tipo de listado no se encuentra en el repositorio.
     * Se configura un mock para el listingTypeRepository para devolver un Optional vacío
     * cuando se llama al método findById con cualquier identificador de listado.
     * Se espera que la prueba arroje una excepción de tipo ResourceNotFoundException.
     */
    @Test
    void testGetListingTypeNameFromBd_ListingTypeNotFound() {
        when(listingTypeRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> new MeliService(null, null, null, null, listingTypeRepository, null, null, null)
                        .getListingTypeNameFromBd("nonExistentListingTypeId"));
    }

    @Test
    void testGetBuyBoxWinner() throws ResourceNotFoundException, AppException {
        BoxWinnerDTO mockResultDTO = createMockBoxWinnerDTO();
        when(meliFeignClient.getProductWinnerSearch(anyString(), anyString())).thenReturn(mockResultDTO);

        SellerDTO mockSellerDTO = createMockSellerDTO();
        when(meliFeignClient.getSellerBySellerId(Integer.valueOf(anyString()))).thenReturn(mockSellerDTO);

        MeliService meliService = new MeliService(meliFeignClient, null, null, null, null, null, null, null);

        BuyBoxWinnerResponseDTO resultDTO = meliService.getBuyBoxWinner("productId");

        assertEquals(mockResultDTO.getBuy_box_winner().getListing_type_id(),
                resultDTO.getListing_type_id());
        assertEquals(mockSellerDTO.getSeller().getNickname(),
                resultDTO.getSeller_nickname());
    }


    private BoxWinnerDTO createMockBoxWinnerDTO() {
        BoxWinnerDTO boxWinnerDTO = new BoxWinnerDTO();
        return boxWinnerDTO;
    }

    private SellerDTO createMockSellerDTO() {
        SellerDTO sellerDTO = new SellerDTO();
        return sellerDTO;
    }

    @Test
    void testGetBuyBoxWinnerCatalog() throws ResourceNotFoundException {
        BoxWinnerDTO mockResultDTO = createMockBoxWinnerDTO();
        when(meliFeignClient.getProductWinnerSearch(anyString(), anyString())).thenReturn(mockResultDTO);

        MeliService meliService = new MeliService(meliFeignClient, null, null, null, null, null, null, null);

        BuyBoxWinnerResponseDTO resultDTO = meliService.getBuyBoxWinnerCatalog("productId");

        assertEquals(mockResultDTO.getBuy_box_winner().getListing_type_id(),
                resultDTO.getListing_type_id());
    }

}