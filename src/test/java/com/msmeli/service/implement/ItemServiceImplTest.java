package com.msmeli.service.implement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.msmeli.exception.AppException;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Item;
import com.msmeli.repository.ItemRepository;
import com.msmeli.repository.SellerRefactorRepository;
import com.msmeli.service.feignService.MeliService;
import com.msmeli.service.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import java.util.Arrays;
import java.util.List;

public class ItemServiceImplTest {

    private ItemRepository itemRepository;
    private MeliFeignClient meliFeignClient;
    private ListingTypeService listingTypeService;
    private MeliService meliService;
    private ModelMapper mapper;
    private StockServiceImpl stockService;
    private CostService costService;
    private SellerService sellerService;
    private UserEntityService userEntityService;
    private SellerRefactorRepository sellerRefactorRepository;

    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        itemRepository = mock(ItemRepository.class);
        meliFeignClient = mock(MeliFeignClient.class);
        listingTypeService = mock(ListingTypeService.class);
        meliService = mock(MeliService.class);
        mapper = mock(ModelMapper.class);
        stockService = mock(StockServiceImpl.class);
        costService = mock(CostService.class);
        sellerService = mock(SellerService.class);
        userEntityService = mock(UserEntityService.class);
        sellerRefactorRepository = mock(SellerRefactorRepository.class);

        // Create the service instance with mocked dependencies
        itemService = new ItemServiceImpl(
                itemRepository, meliFeignClient, listingTypeService, meliService, mapper,
                stockService, costService, sellerService, userEntityService, sellerRefactorRepository
        );
    }

    /**
     * Prueba unitaria para el método findAllidSeller en la clase ItemService.
     *
     * Esta prueba verifica el correcto funcionamiento del método findAllidSeller en ItemService.
     * Se asegura de que, al proporcionar un ID de vendedor, se obtengan todos los elementos asociados
     * al vendedor utilizando el repositorio y que la lista resultante sea la esperada.
     * @throws AppException Si se produce una excepción no esperada durante la ejecución del método.
     */
    @Test
    void testFindAllidSeller() {
        Long idSeller = 1L;
        List<Item> mockItems = Arrays.asList(new Item(), new Item());

        when(itemRepository.findAllBySellerRefactorId(idSeller)).thenReturn(mockItems);

        List<Item> result = null;
        try {
            result = itemService.findAllidSeller(idSeller);
        } catch (AppException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertNotNull(result);
        assertEquals(mockItems.size(), result.size());

    }

    /**
     * Prueba unitaria para el método findAllidSeller en la clase ItemService cuando la lista está vacía.
     *
     * Esta prueba verifica el comportamiento del método findAllidSeller en ItemService cuando el
     * repositorio devuelve una lista vacía. Se espera que el método lance una excepción de tipo
     * AppException con el mensaje "No Content" en este escenario.
     *
     * @throws AppException Si se produce una excepción inesperada durante la ejecución del método.
     */
    @Test
    void testFindAllidSellerEmptyList() {
        Long idSeller = 1L;

        when(itemRepository.findAllBySellerRefactorId(idSeller)).thenReturn(Arrays.asList());

        AppException exception = assertThrows(AppException.class, () -> itemService.findAllidSeller(idSeller));
        assertEquals("No Content", exception.getMessage());

    }

    /**
     * Prueba unitaria para el método save en la clase ItemService.
     *
     * Esta prueba verifica el correcto funcionamiento del método save en ItemService.
     * Se asegura de que, al proporcionar un elemento, este sea guardado correctamente
     * utilizando el repositorio y que el resultado sea el mismo elemento.
     */
    @Test
    void testSave() {
        Item mockItem = new Item();

        when(itemRepository.save(mockItem)).thenReturn(mockItem);

        Item result = itemService.save(mockItem);

        assertNotNull(result);
        assertEquals(mockItem, result);

        verify(itemRepository, times(1)).save(eq(mockItem));
    }

}
