package com.msmeli.service.implement;
import com.msmeli.dto.FeeDetailsDTO;
import com.msmeli.dto.response.FeeResponseDTO;
import com.msmeli.dto.response.ShippingCostDTO;
import com.msmeli.exception.AppException;
import com.msmeli.model.Cost;
import com.msmeli.model.Item;
import com.msmeli.model.Stock;
import com.msmeli.repository.CostRepository;
import com.msmeli.service.feignService.MeliService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CostServiceImplTest {

    @InjectMocks
    private CostServiceImpl costService;

    @Mock
    private CostRepository costRepository;

    @Mock
    private MeliService meliService;

    @Mock
    private ModelMapper mapper;


    /**
     * Prueba unitaria para verificar el comportamiento del método createProductsCosts en CostService.
     * Se crean instancias simuladas de Item y Stock con datos específicos para la prueba.
     * Se configuran los mocks necesarios para el test, como el mapper y meliService.
     * Se llama al método que se está probando, createProductsCosts.
     * Se verifica que el método haya llamado correctamente al costRepository y que el resultado no sea nulo.
     */
    @Test
    public void testCreateProductsCosts() {
        Item item = new Item();
        item.setId("123");
        item.setCategory_id("MLA1051");
        item.setListing_type_id("gold_pro");
        item.setPrice(100.0);

        Stock stock = new Stock();
        stock.setPrice(50.0);

        ShippingCostDTO mockShippingCostDTO = new ShippingCostDTO();
        mockShippingCostDTO.getList_cost();

        when(mapper.map(Mockito.any(), Mockito.any())).thenReturn(new Cost());
        try {
            when(meliService.getItemFee(Mockito.anyDouble(), Mockito.anyString(), Mockito.anyString())).thenReturn(new FeeResponseDTO());
        } catch (AppException e) {
            throw new RuntimeException(e);
        }
        Item result = costService.createProductsCosts(item, stock);

        Mockito.verify(costRepository, Mockito.times(1)).save(Mockito.any());
        Assertions.assertNotNull(result);
    }

    /**
     * Prueba la funcionalidad de "hasStock" en el caso en que tanto el SKU como el Stock no son nulos.
     * Se verifica si el servicio de costo (CostServiceImpl) devuelve true al llamar al método "hasStock" con un objeto
     * de tipo Item y un objeto de tipo Stock que tienen valores no nulos.
     */
        @Test
        void testHasStockWhenSkuAndStockAreNotNull() {
            Item item = new Item();
            item.setSku("ABC");

            Stock stock = new Stock();


            CostServiceImpl costService = new CostServiceImpl(null, null, null);
            boolean result = costService.hasStock(item, stock);

            assertTrue(result);
        }

    /**
     * Prueba la funcionalidad de "hasStock" en el caso en que tanto el SKU como el Stock no son nulos.
     * Se verifica si el servicio de costo (CostServiceImpl) devuelve true al llamar al método "hasStock" con un objeto
     * de tipo Item y un objeto de tipo Stock que tienen valores nulos.
     */
        @Test
        void testHasStockWhenSkuIsNull() {
            Item item = new Item();
            Stock stock = new Stock();

            CostServiceImpl costService = new CostServiceImpl(null, null, null);
            boolean result = costService.hasStock(item, stock);

            assertFalse(result);
        }

    /**
     * Prueba la funcionalidad de "hasStock" en el caso en que el SKU es nulo.
     * Se verifica si el servicio de costo (CostServiceImpl) devuelve false al llamar al método "hasStock" con un objeto
     * de tipo Item que tiene el SKU nulo y un objeto de tipo Stock con valores necesarios.
     */
        @Test
        void testHasStockWhenStockIsNull() {
            Item item = new Item();
            item.setSku("ABC");

            Stock stock = null;

            CostServiceImpl costService = new CostServiceImpl(null, null, null);
            boolean result = costService.hasStock(item, stock);

            assertFalse(result);
        }
    /**
     * Prueba la funcionalidad de "hasFee" en el caso en que los detalles de la tarifa (FeeDetailsDTO) no son nulos.
     * Se verifica si el servicio de costo (CostServiceImpl) devuelve true al llamar al método "hasFee" con un objeto
     * de tipo FeeDetailsDTO que tiene valores no nulos.
     */
    @Test
    void testHasFeeWhenFeeDetailsIsNotNull() {
        FeeDetailsDTO feeDetails = new FeeDetailsDTO();

        CostServiceImpl costService = new CostServiceImpl(null, null, null);
        boolean result = costService.hasFee(feeDetails);

        assertTrue(result);
    }

    /**
     * Prueba la funcionalidad de "hasFee" en el caso en que los detalles de la tarifa (FeeDetailsDTO) no son nulos.
     * Se verifica si el servicio de costo (CostServiceImpl) devuelve true al llamar al método "hasFee" con un objeto
     * de tipo FeeDetailsDTO que tiene valores nulos.
     */
    @Test
    void testHasFeeWhenFeeDetailsIsNull() {
        FeeDetailsDTO feeDetails = null;

        CostServiceImpl costService = new CostServiceImpl(null, null, null);
        boolean result = costService.hasFee(feeDetails);

        assertFalse(result);
    }

    /**
     * Prueba la funcionalidad de "setCostItem" en el caso en que los detalles de la tarifa (FeeDetailsDTO) y el costo son proporcionados.
     * Se configura un objeto de tipo Item con propiedades necesarias, un objeto de tipo FeeDetailsDTO con valores específicos,
     * y se simula el comportamiento del repositorio de costos al guardar un nuevo costo. Luego, se llama al método "setCostItem"
     * del servicio de costo (CostServiceImpl) y se verifican las propiedades del objeto Item resultante.
     * Además, se realizan aserciones sobre las propiedades del objeto Cost asociado al Item resultante.
     * Se maneja la excepción AppException si ocurre alguna.
     */
    @Test
    void testSetCostItemWithFeeDetails() {
        Item item = new Item();
        item.setPrice(100.0);

        FeeDetailsDTO feeDetails = new FeeDetailsDTO();
        feeDetails.setPercentage_fee(10.0);
        feeDetails.setGross_amount(5.0);

        Cost cost = new Cost();

        when(costRepository.save(Mockito.any(Cost.class))).thenReturn(cost);

        try {
            Item resultItem = costService.setCostItem(item, feeDetails, cost);


            assertNotNull(resultItem.getCost());
            assertEquals(feeDetails.getPercentage_fee(), resultItem.getCost().getComision_fee());
            assertEquals(feeDetails.getGross_amount(), resultItem.getCost().getComision_discount());
        } catch (AppException ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    /**
     * Prueba la funcionalidad de "setCostItem" en el caso en que los detalles de la tarifa (FeeDetailsDTO) son nulos.
     * Se configura un objeto de tipo Item con propiedades necesarias y se establece que los detalles de la tarifa son nulos.
     * Se simula el comportamiento del repositorio de costos al guardar un nuevo costo. Luego, se llama al método "setCostItem"
     * del servicio de costo (CostServiceImpl) y se verifican las propiedades del objeto Item resultante, incluyendo la presencia
     * del objeto Cost asociado. Se maneja la excepción AppException si ocurre alguna.
     */
    @Test
    void testSetCostItemWithoutFeeDetails() {
        Item item = new Item();
        item.setPrice(100.0);

        FeeDetailsDTO feeDetails = null;

        Cost cost = new Cost();

        when(costRepository.save(Mockito.any(Cost.class))).thenReturn(cost);

        try {
            Item resultItem = costService.setCostItem(item, feeDetails, cost);

            assertNotNull(resultItem.getCost());
        } catch (AppException ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }
}