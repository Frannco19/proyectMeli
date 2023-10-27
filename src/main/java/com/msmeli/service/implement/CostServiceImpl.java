package com.msmeli.service.implement;

import com.msmeli.dto.FeeDetailsDTO;
import com.msmeli.dto.request.FeeRequestDTO;
import com.msmeli.model.Cost;
import com.msmeli.model.Item;
import com.msmeli.model.Stock;
import com.msmeli.repository.CostRepository;
import com.msmeli.service.feignService.MeliService;
import com.msmeli.service.services.CostService;
import com.msmeli.util.GrossIncome;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CostServiceImpl implements CostService {

    private final CostRepository costRepository;
    private final MeliService meliService;
    private final ModelMapper mapper;

    public CostServiceImpl(CostRepository costRepository, MeliService meliService, ModelMapper mapper) {
        this.costRepository = costRepository;
        this.meliService = meliService;
        this.mapper = mapper;
    }

    @Override
    public Item createProductsCosts(Item item, Stock stock) {
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
            if (item.getSku() != null && stock != null) cost.setReplacement_cost(stock.getPrice());
            Double shippingCost = meliService.getShippingCostDTO(item.getId()).getOptions().stream().filter(option -> option.getName().equals("Estándar a sucursal de correo")).findFirst().get().getBase_cost();
            cost.setShipping(shippingCost);
            double total_cost = item.getPrice() * GrossIncome.IIBB.iibPercentage + (cost.getComision_discount() + cost.getShipping() + cost.getReplacement_cost());
            cost.setTotal_cost(total_cost);
            double margin = (item.getPrice() - total_cost) *100/item.getPrice();
            cost.setMargin(margin);
            double profit = item.getPrice() - total_cost;
            cost.setProfit(profit);
        } catch (FeignException.NotFound | FeignException.InternalServerError ignored) {
            cost.setShipping(0);
        } finally {
            item.setCost(costRepository.save(cost));
            return item;
        }
    }
}
