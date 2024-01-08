package com.msmeli.service.implement;

import com.msmeli.dto.FeeDetailsDTO;
import com.msmeli.dto.request.FeeRequestDTO;
import com.msmeli.exception.AppException;
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
//TODO UNA VEZ SEGURO BORRAR EL METODO CREATEPRODUCTSCOSTS
    @Override
    public Item createProductsCosts(Item item, Stock stock) {
        //step 1 creacion datos
        Cost cost = mapper.map(item, Cost.class);
        FeeRequestDTO feeRequestDTO = new FeeRequestDTO();
        Double shippingCost = 0.0;
        FeeDetailsDTO feeDetails = null;
        //step 2
        try {
            cost.setId(item.getId());
            feeRequestDTO.setCategoryId(item.getCategory_id());
            feeRequestDTO.setListingTypeId(item.getListing_type_id());
            feeRequestDTO.setPrice(item.getPrice());
            feeDetails = meliService.getItemFee(item.getPrice(), item.getCategory_id(), item.getListing_type_id()).getSale_fee_details();
            //shippingCost = meliService.getShippingCostDTO(item.getId()).getOptions().stream().filter(option -> option.getName().equals("Estándar a sucursal de correo")).findFirst().get().getBase_cost();
        } catch (FeignException.NotFound | FeignException.InternalServerError ignored) {
        } finally {

            //step 3
            if (item.getSku() != null && stock != null) cost.setReplacement_cost(stock.getPrice());
            if (feeDetails != null) {
                cost.setComision_fee(feeDetails.getPercentage_fee());
                cost.setComision_discount(feeDetails.getGross_amount());
            }
            //step 4
            cost.setShipping(shippingCost);
            double total_cost = item.getPrice() * GrossIncome.IIBB.iibPercentage + (cost.getComision_discount() + cost.getShipping() + cost.getReplacement_cost());
            cost.setTotal_cost(Math.round(total_cost * 100) / 100d);
            double margin = (item.getPrice() - total_cost) * 100 / item.getPrice();
            cost.setMargin(Math.round(margin * 100) / 100d);
            double profit = item.getPrice() - total_cost;
            cost.setProfit(Math.round(profit * 100) / 100d);
            item.setCost(costRepository.save(cost));
            return item;
        }
    }

    /**
     * metodo que se encarga en solicitar los datos de Fee desde la api de mercado Libre en relacion al item que entra
     * como parametro
     * @param item
     * @return FeeDetailsDTO devuelve los datos solcitados a la api de mercadolibre
     * @throws AppException puede lanzar excepciones de tipo feign
     */
   private FeeDetailsDTO getFeeForItem(Item item) throws AppException {
       FeeDetailsDTO feeDetails = meliService.getItemFee(item.getPrice(), item.getCategory_id(), item.getListing_type_id()).getSale_fee_details();
       return feeDetails;
   }

    /**
     * Metodo que que se encarga de comprobar si existe un stock en relacion al item
     * @param item
     * @param stock
     * @return
     */
    private boolean hasStock(Item item, Stock stock){
       if(item.getSku() != null && stock != null){
           return true;
       }
       return false;
   }

    /**
     * Metodo para controlar si existen las comisiones de mercadolibre
     * @param feeDetails
     * @return
     */
    private boolean hasFee(FeeDetailsDTO feeDetails){
       if (feeDetails != null){
           return true;
       }
       return false;
   }
    public Item createProductsCostsV2(Item item, Stock stock) throws AppException {
        Cost cost = mapper.map(item, Cost.class);
        FeeDetailsDTO feeDetails = getFeeForItem(item);
        if(hasStock(item,stock)){
            cost.setReplacement_cost(stock.getPrice());
        }
        item = setCostItem(item,feeDetails,cost);
        return item;

    }

    /**
     * El metodo se encarga en setear los datos al objeto costo en relacion al item que entra por parametro
     * @param item
     * @param feeDetails Comisiones mercadoLibre
     * @param cost
     * @return
     * @throws AppException
     */
     private Item setCostItem(Item item, FeeDetailsDTO feeDetails, Cost cost) throws AppException {
        try {
            Double shippingCost = getShippingCost(item);
            if(hasFee(feeDetails)){
                cost.setComision_fee(feeDetails.getPercentage_fee());
                cost.setComision_discount(feeDetails.getGross_amount());
            }
            cost.setShipping(shippingCost);
            double total_cost = item.getPrice() * GrossIncome.IIBB.iibPercentage + (cost.getComision_discount() + cost.getShipping() + cost.getReplacement_cost());
            cost.setTotal_cost(Math.round(total_cost * 100) / 100d);
            double margin = (item.getPrice() - total_cost) * 100 / item.getPrice();
            cost.setMargin(Math.round(margin * 100) / 100d);
            double profit = item.getPrice() - total_cost;
            cost.setProfit(Math.round(profit * 100) / 100d);
            item.setCost(costRepository.save(cost));
        }catch (Exception ex){
            throw new AppException(ex.getMessage(),"CostServiceImple->setCostItem",000,404);
        }
        return item;
    }
    private double getShippingCost(Item item){
        double shippingCost = 0.0;
        try{
            shippingCost = meliService.getShippingCostDTO(item.getId()).getOptions().stream().filter(option -> option.getName().equals("Estándar a domicilio")).findFirst().get().getList_cost();
            return shippingCost;
        }catch (Exception e){
            return shippingCost;
        }

    }

}
