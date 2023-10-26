package com.msmeli.service.implement;

import com.msmeli.dto.FeeDetailsDTO;
import com.msmeli.dto.request.FeeRequestDTO;
import com.msmeli.model.Cost;
import com.msmeli.model.Item;
import com.msmeli.model.Stock;
import com.msmeli.repository.CostRepository;
import com.msmeli.repository.ItemRepository;
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
    private final ItemRepository itemRepository;
    private final StockServiceImpl stockService;
    private static final double IIB = .045;


    public CostServiceImpl(CostRepository costRepository, MeliService meliService, ModelMapper mapper, ItemService itemService, StockServiceImpl stockService, ItemRepository itemRepository, StockServiceImpl stockService1) {
        this.costRepository = costRepository;
        this.meliService = meliService;
        this.mapper = mapper;
        this.itemService = itemService;
        this.itemRepository = itemRepository;
        this.stockService = stockService1;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(5)
    public void createProductsCosts() {
        List<Item> items = itemService.findAll();
        items.parallelStream().forEach((item -> {
            Cost cost = mapper.map(item, Cost.class);
            FeeRequestDTO feeRequestDTO = new FeeRequestDTO();
            try {
                cost.setId(item.getId());
                feeRequestDTO.setCategoryId(item.getCategory_id());
                feeRequestDTO.setListingTypeId(item.getListing_type_id());
                feeRequestDTO.setPrice(item.getPrice());
                FeeDetailsDTO feeDetails = meliService.getItemFee(item.getPrice(), item.getCategory_id(), item.getListing_type_id()).getSale_fee_details();
                cost.setComision_fee(feeDetails.getPercentage_fee());
                cost.setComision_discount(feeDetails.getGross_amount());
                if (item.getSku() != null) {
                    Stock stock = stockService.getBySku(item.getSku());
                    if (stock != null) cost.setReplacement_cost(stock.getPrice());
                }
                Double shippingCost = meliService.getShippingCostDTO(item.getId()).getOptions().stream().filter(option -> option.getName().equals("Estándar a sucursal de correo")).findFirst().get().getBase_cost();
                cost.setShipping(shippingCost);
                double profit = item.getPrice() - (item.getPrice() * IIB + (cost.getComision_discount() + cost.getShipping() + cost.getReplacement_cost()));
                cost.setProfit(profit);
            } catch (FeignException.NotFound | FeignException.InternalServerError ignored) {
                cost.setShipping(0);
            } finally {
                item.setCost_id(costRepository.save(cost));
                itemRepository.save(item);
            }
        }));
    }
}
