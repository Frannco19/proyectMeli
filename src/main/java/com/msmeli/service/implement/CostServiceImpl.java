package com.msmeli.service.implement;

import com.msmeli.dto.request.FeeRequestDTO;
import com.msmeli.model.Cost;
import com.msmeli.model.Item;
import com.msmeli.repository.CostRepository;
import com.msmeli.service.feignService.MeliService;
import com.msmeli.service.services.CostService;
import com.msmeli.service.services.ItemService;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CostServiceImpl implements CostService {

    private final CostRepository costRepository;
    private final MeliService meliService;
    private final ModelMapper mapper;
    private final ItemService itemService;
    private static final double IIB = .045;


    public CostServiceImpl(CostRepository costRepository, MeliService meliService, ModelMapper mapper, ItemService itemService, StockServiceImpl stockService) {
        this.costRepository = costRepository;
        this.meliService = meliService;
        this.mapper = mapper;
        this.itemService = itemService;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(5)
    public void createProductsCosts() {
        List<Item> items = itemService.findAll();
        items.parallelStream().forEach((item -> {
            Cost cost = mapper.map(item, Cost.class);
            FeeRequestDTO feeRequestDTO = new FeeRequestDTO();
            try {
                Double shippingCost = meliService.getShippingCostDTO(item.getId()).getOptions().stream().filter(option -> option.getName().equals("Estándar a sucursal de correo")).findFirst().get().getBase_cost();
                cost.setShipping(shippingCost);
                feeRequestDTO.setCategoryId(item.getCategory_id());
                feeRequestDTO.setListingTypeId(item.getListing_type_id());
                feeRequestDTO.setPrice(item.getPrice());
                cost.setComision_fee(meliService.getItemFee(feeRequestDTO).getDetails().getPercentage_fee());
                cost.setComision_discount(meliService.getItemFee(feeRequestDTO).getDetails().getGross_amount());
                double profit = item.getPrice() - (item.getPrice() * IIB + (cost.getComision_discount() + cost.getShipping()));
                cost.setProfit(profit);
                costRepository.save(cost);
            } catch (FeignException.NotFound ignored) {

            }
        }));
    }
}
