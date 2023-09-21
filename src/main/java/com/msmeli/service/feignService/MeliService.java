package com.msmeli.service.feignService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.msmeli.dto.response.*;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.msmeli.dto.response.CatalogItemResponseDTO;
import com.msmeli.dto.response.ImageAndSkuDTO;
import com.msmeli.dto.response.ListingTypeResponseDTO;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Item;
import com.msmeli.repository.*;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import feign.FeignException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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


    public Item getItemById(String itemId) throws Exception {
        return itemRepository.findById(itemId).orElseThrow(() -> new Exception("Item not found"));
    }

    public Category getCategory(String categoryId) throws Exception {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new Exception("Category not found"));
    }

    public Seller getSeller(Integer sellerId) throws Exception {
        return sellerRepository.findById(sellerId).orElseThrow(() -> new Exception("Seller not found"));
    }

//    public Integer getPosition(String categoryId, String itemId) throws JsonProcessingException, ResourceNotFoundException {
//
//        try {
//            DocumentContext json = JsonPath.parse(meliFeignClient.getItemPositionByCategory(categoryId));
//
//            String content = json.read("$.content[*]").toString();
//            List<PositionResponseDTO> positionList = objectMapper.readValue(content, new TypeReference<>(){});
//
//            AtomicReference<Integer> position = new AtomicReference<>(0);
//
//            positionList.forEach( e -> {
//                if(e.getId().equals(itemId)){
//                    position.set(e.getPosition());
//                };
//            });
//            return position.get();
//        } catch (ResourceNotFoundException e) {
//            return null;
//        }
//
//    }


    public Category saveCategory(String categoryId){
        DocumentContext json = JsonPath.parse(meliFeignClient.getCategory(categoryId));
        return categoryRepository.save(
                Category
                    .builder()
                    .categoryId(json.read("$.id"))
                    .categoryName(json.read("$.name"))
                    .build());
    }

    public String getSellerNickname(Integer sellerId){
        DocumentContext json = JsonPath.parse(meliFeignClient.getSellerBySellerId(sellerId));
        return json.read("$.seller.nickname");
    }

    public Seller saveSeller(Integer seller_id){
        DocumentContext json = JsonPath.parse(meliFeignClient.getSellerBySellerId(seller_id));

        DocumentContext sellerJson = JsonPath.parse((Object) json.read("$.seller"));

        return sellerRepository.save(
                Seller
                        .builder()
                        .sellerId(sellerJson.read("$.id"))
                        .nickname(sellerJson.read("$.nickname"))
                        .build());
    }

    public String getListingTypeName(String listingTypeId) throws JsonProcessingException {
        DocumentContext jsonType = JsonPath.parse(meliFeignClient.getTypeName());
        String content = jsonType.read("$.[*]").toString();

        List<ListingTypeResponseDTO> typesList = objectMapper.readValue(content, new TypeReference<>(){});

        String typeName;

        for (ListingTypeResponseDTO e : typesList) {
            if (e.getId().equals(listingTypeId)) {
                typeName = e.getName();
                return typeName;
            }
        }

        return null;

    }

    public String getSellerNickname(Integer sellerId){
        DocumentContext json = JsonPath.parse(meliFeignClient.getSellerBySellerId(sellerId));
//        DocumentContext sellerJson = JsonPath.parse((Object) json.read("$.seller.nickname"));
        return json.read("$.seller.nickname");
    }

    public Integer getPositionMethod(DocumentContext positionByItemId){
        return positionByItemId.read("$.position");
    }

    public Integer getPosition(String itemId, String productId) {

        try {
            DocumentContext positionByItemId = JsonPath.parse(meliFeignClient.getItemPositionByItemId(itemId));
            return getPositionMethod(positionByItemId);

        } catch (FeignException.NotFound ignored) {

        }

        try {
            DocumentContext positionByProductId = JsonPath.parse(meliFeignClient.getItemPositionByProductId(productId));
            return getPositionMethod(positionByProductId);
        } catch (FeignException.NotFound ignored) {

        }

        return 0;

    }


    private Item filterJsonData(DocumentContext itemContext) throws JsonProcessingException, ParseException {
        Number price = itemContext.read("$.price");

        ImageAndSkuDTO imageAndSku = getItemImageAndSku(itemContext.read("$.id"));

        String categoryId = itemContext.read("$.category_id");
        String itemId = itemContext.read("$.id");

        return Item
                .builder()
                .item_id(itemId)
                .catalog_product_id(itemContext.read("$.catalog_product_id"))
                .title(itemContext.read("$.title"))
                .category_id(categoryId)
                .price(price.doubleValue())
                .sold_quantity(itemContext.read("$.sold_quantity"))
                .available_quantity(itemContext.read("$.available_quantity"))
                .sellerId(itemContext.read("$.seller.id"))
                .update_date_db(LocalDateTime.now())
                .listing_type_id(itemContext.read("$.listing_type_id"))
                .catalog_position(0)
                .statusCondition(imageAndSku.getStatus_condition())
                .urlImage(imageAndSku.getImage_url())
                .sku(imageAndSku.getSku())
                .created_date_item(imageAndSku.getCreated_date_item())
                .updated_date_item(imageAndSku.getUpdated_date_item())
                .build();
    }

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
