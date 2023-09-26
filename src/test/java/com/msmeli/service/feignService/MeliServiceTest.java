package com.msmeli.service.feignService;

import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Seller;
import com.msmeli.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MeliServiceTest {

    @InjectMocks
    private MeliService meliService;

    @Mock
    private MeliFeignClient meliFeignClient;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    SellerRatingRepository sellerRatingRepository;

    @Mock
    SellerReputationRepository sellerReputationRepository;

    @Mock
    SellerTransactionRepository sellerTransactionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        meliService = new MeliService(
                meliFeignClient,
                productRepository,
                itemRepository, // Asegúrate de inyectar el repositorio en la instancia de MeliService
                categoryRepository,
                sellerRepository,
                sellerRatingRepository,
                sellerReputationRepository,
                sellerTransactionRepository
        );
    }

    @Test
    public void testSaveSeller() {
        // Simular el comportamiento de meliFeignClient
        String sellerJsonResponse = "{\"seller\": {\"id\": 123, \"nickname\": \"MORO TECH\"}}";
        when(meliFeignClient.getSellerByNickname("MORO TECH")).thenReturn(sellerJsonResponse);

        // Llamar al método saveSeller
        meliService.saveSeller();

        // Verificar que sellerRepository.save se llamó con el Seller esperado
        verify(sellerRepository).save(
                argThat(seller -> {
                    assertEquals(123, seller.getSellerId());
                    assertEquals("MORO TECH", seller.getNickname());
                    return true;
                })
        );
    }

    @Test
    public void testSaveSellerItems() {
        // Configura el comportamiento simulado del cliente Feign
        when(meliFeignClient.getSellerByNickname("MORO TECH")).thenReturn("{\"results\":[{\"id\":\"123\",\"title\":\"Item 1\",\"condition\":\"New\",\"price\":10.0,\"sold_quantity\":2,\"available_quantity\":5}]}");

        // Llama al método que deseas probar
        meliService.saveSellerItems();

        // Verifica que el método saveAll del repositorio se haya llamado con la lista de ítems correcta
        verify(itemRepository, times(1)).saveAll(anyList());
    }
}
