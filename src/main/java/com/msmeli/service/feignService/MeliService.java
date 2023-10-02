package com.msmeli.service.feignService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.msmeli.dto.*;
import com.msmeli.dto.response.BuyBoxWinnerResponseDTO;
import com.msmeli.dto.response.CatalogItemResponseDTO;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Category;
import com.msmeli.model.Item;
import com.msmeli.model.Seller;
import com.msmeli.repository.*;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private final ObjectMapper objectMapper;

    private final ModelMapper modelMapper;

    public MeliService(MeliFeignClient meliFeignClient, ItemRepository itemRepository,
                       CategoryRepository categoryRepository, SellerRepository sellerRepository,
                       SellerRatingRepository sellerRatingRepository, SellerReputationRepository sellerReputationRepository,
                       SellerTransactionRepository sellerTransactionRepository, ObjectMapper objectMapper,
                       ModelMapper modelMapper, ObjectMapper objectMapper1) {
        this.meliFeignClient = meliFeignClient;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.sellerRepository = sellerRepository;
        this.sellerRatingRepository = sellerRatingRepository;
        this.sellerReputationRepository = sellerReputationRepository;
        this.sellerTransactionRepository = sellerTransactionRepository;
        this.objectMapper = objectMapper1;
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

    public String getListingTypeName(String listingTypeId){
        DocumentContext jsonType = JsonPath.parse(meliFeignClient.getTypeName());
        String content = jsonType.read("$.[*]").toString();

        List<ListingTypeDTO> typesList = null;
        try {
            typesList = objectMapper.readValue(content, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String typeName;

        for (ListingTypeDTO e : typesList) {
            if (e.getId().equals(listingTypeId)) {
                typeName = e.getName();
                return typeName;
            }
        }

        return null;

    }


    public Integer getPositionMethod(DocumentContext positionByItemId){
        return positionByItemId.read("$.position");
    }

    public Integer getBestSellerPosition(String itemId, String productId) {

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

    private String getItemSku(ItemAttributesDTO attributes){
        List<AttributesDTO> sku = attributes.getAttributes()
                .parallelStream()
                .filter(att -> att.getName().equals("SKU"))
                .toList();
        if (!sku.isEmpty()) return sku.get(0).getValue_name();
        return null;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(3)
    public void saveSellerItems() {
        int offset = 0;
        SellerDTO responseDTO;
        List<Item> items = new ArrayList<>();

        do {
            items.clear();

            responseDTO = meliFeignClient.getSellerByNickname("MORO TECH", offset);

            SellerDTO finalResponseDTO = responseDTO;

            responseDTO.getResults().parallelStream().forEach(e -> {

                ItemAttributesDTO attributesDTO = meliFeignClient.getItemAtributtes(e.getId());

                e.setImage_url(attributesDTO.getPictures().get(0).getUrl());
                e.setCreated_date_item(attributesDTO.getDate_created());
                e.setUpdated_date_item(attributesDTO.getLast_updated());

                e.setSku(getItemSku(attributesDTO));

                e.setListing_type_id(getListingTypeName(e.getListing_type_id()));

                Item item = modelMapper.map(e, Item.class);
                item.setUpdate_date_db(LocalDateTime.now());
                item.setSellerId(finalResponseDTO.getSeller().getId());

                items.add(item);
            });

            itemRepository.saveAll(items);

            offset = offset + 50;
        } while (!responseDTO.getResults().isEmpty());
    }

    public List<CatalogItemResponseDTO> getSellerItemCatalog(String product_catalog_id) {

        ItemCatalogDTO responseDTO = meliFeignClient.getProductSearch(product_catalog_id);

        return responseDTO.getResults().parallelStream().peek(e ->{

            ItemAttributesDTO attributesDTO = meliFeignClient.getItemAtributtes(e.getItem_id());
            SellerDTO sellerDTO = meliFeignClient.getSellerBySellerId(e.getSeller_id());

            e.setCreated_date_item(attributesDTO.getDate_created());
            e.setUpdated_date_item(attributesDTO.getLast_updated());
            e.setSeller_nickname(sellerDTO.getSeller().getNickname());

            //System.out.println(e.getSeller_id());

        }).toList();
    }

    public BuyBoxWinnerResponseDTO getBuyBoxWinner(String productId){

        BoxWinnerDTO result = meliFeignClient.getProductWinnerSearch(productId);
        SellerDTO seller = meliFeignClient.getSellerBySellerId(result.getBuy_box_winner().getSeller_id());

        BuyBoxWinnerResponseDTO responseDTO = modelMapper.map(result.getBuy_box_winner(), BuyBoxWinnerResponseDTO.class);
        responseDTO.setSeller_nickname(seller.getSeller().getNickname());
        responseDTO.setListing_type_id(getListingTypeName(responseDTO.getListing_type_id()));

        return responseDTO;
    }


}
