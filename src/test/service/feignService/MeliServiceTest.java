package com.msmeli.service.feignService;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.msmeli.controller.AuthController;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Category;
import com.msmeli.model.Employee;
import com.msmeli.model.Item;
import com.msmeli.model.Seller;
import com.msmeli.repository.*;
import com.msmeli.service.implement.UserEntityServiceImpl;
import com.msmeli.service.services.UserEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private SellerRatingRepository sellerRatingRepository;

    @Mock
    private SellerReputationRepository sellerReputationRepository;

    @Mock
    private SellerTransactionRepository sellerTransactionRepository;

    @InjectMocks
    private MeliService meliService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testGetItemById() {
        // Configura el comportamiento simulado del repositorio de ítems
        Mockito.when(itemRepository.findById("itemId")).thenReturn(Optional.of(new Item(/* Datos del ítem */)));

        // Llama al método que deseas probar
        try {
            Item item = meliService.getItemById("itemId");

            // Realiza afirmaciones sobre el resultado
            Assertions.assertNotNull(item);
            // Agrega más afirmaciones según sea necesario
        } catch (Exception e) {
            Assertions.fail("Excepción no esperada: " + e.getMessage());
        }
    }

    @Test
    public void testGetCategory() {
        // Configura el comportamiento simulado del repositorio de categorías
        Mockito.when(categoryRepository.findById("categoryId")).thenReturn(Optional.of(new Category(/* Datos de la categoría */)));

        // Llama al método que deseas probar
        try {
            Category category = meliService.getCategory("categoryId");

            // Realiza afirmaciones sobre el resultado
            Assertions.assertNotNull(category);
            // Agrega más afirmaciones según sea necesario
        } catch (Exception e) {
            Assertions.fail("Excepción no esperada: " + e.getMessage());
        }
    }

    @Test
    public void testGetSeller() {
        // Configura el comportamiento simulado del repositorio de vendedores
        int sellerId = 123; // El ID del vendedor que deseas buscar
        Seller seller = new Seller(/* Datos del vendedor simulado */);
        Mockito.when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(seller));

        // Llama al método que deseas probar
        try {
            Seller resultSeller = meliService.getSeller(sellerId);

            // Realiza afirmaciones sobre el resultado
            Assertions.assertNotNull(resultSeller);
            // Agrega más afirmaciones según sea necesario
        } catch (Exception e) {
            Assertions.fail("Excepción no esperada: " + e.getMessage());
        }
    }

    @Test
    public void testGetSellerWhenSellerNotFound() {
        // Configura el comportamiento simulado del repositorio de vendedores cuando no se encuentra el vendedor
        int sellerId = 456; // Un ID de vendedor que no existe en el repositorio
        Mockito.when(sellerRepository.findById(sellerId)).thenReturn(Optional.empty());

        // Llama al método que deseas probar y espera una excepción
        Assertions.assertThrows(Exception.class, () -> {
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
        Assertions.assertEquals(42, result);
    }

}
