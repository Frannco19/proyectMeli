package com.msmeli.service.implement;
import com.msmeli.dto.response.FeeResponseDTO;
import com.msmeli.dto.response.ShippingCostDTO;
import com.msmeli.model.Cost;
import com.msmeli.model.Item;
import com.msmeli.model.Stock;
import com.msmeli.repository.CostRepository;
import com.msmeli.service.feignService.MeliService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        mockShippingCostDTO.getBase_cost();

        // Configurar los mocks
        Mockito.when(mapper.map(Mockito.any(), Mockito.any())).thenReturn(new Cost());
        Mockito.when(meliService.getItemFee(Mockito.anyDouble(), Mockito.anyString(), Mockito.anyString())).thenReturn(new FeeResponseDTO());
        // Llamar al método que se está probando
        Item result = costService.createProductsCosts(item, stock);

        // Verificar el resultado
        Mockito.verify(costRepository, Mockito.times(1)).save(Mockito.any());
        Assertions.assertNotNull(result);
    }
}