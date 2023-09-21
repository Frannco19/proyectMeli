package com.msmeli.service.feignService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msmeli.dto.response.*;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Item;
import com.msmeli.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class MeliService {

    private final MeliFeignClient meliFeignClient;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;
    private final SellerRatingRepository sellerRatingRepository;
    private final SellerReputationRepository sellerReputationRepository;
    private final SellerTransactionRepository sellerTransactionRepository;

    private final ModelMapper modelMapper;

    public MeliService(MeliFeignClient meliFeignClient, ItemRepository itemRepository, CategoryRepository categoryRepository, SellerRepository sellerRepository, SellerRatingRepository sellerRatingRepository, SellerReputationRepository sellerReputationRepository, SellerTransactionRepository sellerTransactionRepository, ObjectMapper objectMapper, ModelMapper modelMapper) {
        this.meliFeignClient = meliFeignClient;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.sellerRepository = sellerRepository;
        this.sellerRatingRepository = sellerRatingRepository;
        this.sellerReputationRepository = sellerReputationRepository;
        this.sellerTransactionRepository = sellerTransactionRepository;
        this.modelMapper = new ModelMapper();
    }


//    public Seller saveSeller(Integer seller_id) {
//        DocumentContext json = JsonPath.parse(meliFeignClient.getSellerBySellerId(seller_id));
//
//        DocumentContext sellerJson = JsonPath.parse((Object) json.read("$.seller"));
//
//        return sellerRepository.save(
//                Seller
//                        .builder()
//                        .sellerId(sellerJson.read("$.id"))
//                        .nickname(sellerJson.read("$.nickname"))
//                        .build());
//    }




//    private Item filterJsonData(DocumentContext itemContext) throws JsonProcessingException, ParseException {
//        Number price = itemContext.read("$.price");
//
//        ItemAttributesDTO imageAndSku = getItemImageAndSku(itemContext.read("$.id"));
//
//        String categoryId = itemContext.read("$.category_id");
//        String itemId = itemContext.read("$.id");
//
//        return Item
//                .builder()
//                .item_id(itemId)
//                .catalog_product_id(itemContext.read("$.catalog_product_id"))
//                .title(itemContext.read("$.title"))
//                .category_id(categoryId)
//                .price(price.doubleValue())
//                .sold_quantity(itemContext.read("$.sold_quantity"))
//                .available_quantity(itemContext.read("$.available_quantity"))
//                .sellerId(itemContext.read("$.seller.id"))
//                .update_date_db(LocalDateTime.now())
//                .listing_type_id(itemContext.read("$.listing_type_id"))
//                .catalog_position(0)
//                .statusCondition(imageAndSku.getStatus_condition())
//                .urlImage(imageAndSku.getImage_url())
//                .sku(imageAndSku.getSku())
//                .created_date_item(imageAndSku.getCreated_date_item())
//                .updated_date_item(imageAndSku.getUpdated_date_item())
//                .build();
//    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(1)
    public void saveSellerItems() {
         SellerResponseDTO responseDTO = meliFeignClient.getSellerByNickname("MORO TECH");

         List<Item> items = responseDTO.getResults().parallelStream().map(e ->{
             ItemAttributesDTO attributesDTO = meliFeignClient.getItemAtributtes(e.getId());
             e.setImage_url(attributesDTO.getPictures().get(0).getUrl());
             e.setCreated_date_item(attributesDTO.getDate_created());
             e.setUpdated_date_item(attributesDTO.getLast_updated());
             e.setSku(attributesDTO.getAttributes().parallelStream().filter(att-> att.getName().equals("SKU")).toList().get(0).getValue_name());
             Item item = modelMapper.map(e,Item.class);
             item.setUpdate_date_db(LocalDateTime.now());
             item.setSellerId(responseDTO.getSeller().getId());
             return item;
         }).toList();
         itemRepository.saveAll(items);
    }


    public List<CatalogItemResponseDTO> getSellerItemCatalog(String product_catalog_id) {

        ItemCatalogResponseDTO responseDTO = meliFeignClient.getProductSearch(product_catalog_id);


        return responseDTO.getResults().parallelStream().peek(e ->{

            ItemAttributesDTO attributesDTO = meliFeignClient.getItemAtributtes(e.getItem_id());
            SellerResponseDTO sellerResponseDTO = meliFeignClient.getSellerBySellerId(e.getSeller_id());

            e.setCreated_date_item(attributesDTO.getDate_created());
            e.setUpdated_date_item(attributesDTO.getLast_updated());
            e.setSeller_nickname(sellerResponseDTO.getSeller().getNickname());

            System.out.println(e.getSeller_id());

        }).toList();
    }
}