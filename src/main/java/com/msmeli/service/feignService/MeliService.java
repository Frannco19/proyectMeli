package com.msmeli.service.feignService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.msmeli.dto.response.ImageAndSkuDTO;
import com.msmeli.dto.response.ListingTypeResponseDTO;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Category;
import com.msmeli.model.Item;
import com.msmeli.model.Seller;
import com.msmeli.repository.*;
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

    private final ObjectMapper objectMapper;

    public MeliService(MeliFeignClient meliFeignClient, ItemRepository itemRepository, CategoryRepository categoryRepository, SellerRepository sellerRepository, SellerRatingRepository sellerRatingRepository, SellerReputationRepository sellerReputationRepository, SellerTransactionRepository sellerTransactionRepository, ObjectMapper objectMapper) {
        this.meliFeignClient = meliFeignClient;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.sellerRepository = sellerRepository;
        this.sellerRatingRepository = sellerRatingRepository;
        this.sellerReputationRepository = sellerReputationRepository;
        this.sellerTransactionRepository = sellerTransactionRepository;
        this.objectMapper = objectMapper;
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

    public String getSellerNickname(Integer sellerId){
        DocumentContext json = JsonPath.parse(meliFeignClient.getSellerBySellerId(sellerId));
//        DocumentContext sellerJson = JsonPath.parse((Object) json.read("$.seller.nickname"));
        return json.read("$.seller.nickname");
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


    private ImageAndSkuDTO getItemImageAndSku(String itemId) throws ParseException {
        DocumentContext item = JsonPath.parse(meliFeignClient.getImageAndSku(itemId));
        List<Object> skuObj = item.read("$.attributes[?(@.id == 'SELLER_SKU')].values[0].name");

        String sku = null;
        String image = item.read("$.pictures[0].url");
        String status = item.read("$.status");
        String created_date_item = item.read("$.date_created");
        String updated_date_item = item.read("$.last_updated");

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        Date createdDate = dateFormatter.parse(created_date_item);
        Date updatedDate = dateFormatter.parse(updated_date_item);

        if (!skuObj.isEmpty()) sku = skuObj.get(0).toString();

        return ImageAndSkuDTO.builder()
                .sku(sku)
                .image_url(image)
                .status_condition(status)
                .created_date_item(createdDate)
                .updated_date_item(updatedDate)
                .build();
    }

    private Item filterJsonData(DocumentContext itemContext) throws JsonProcessingException, ParseException {
        Number price = itemContext.read("$.price");

        ImageAndSkuDTO imageAndSku = getItemImageAndSku(itemContext.read("$.id"));

        String categoryId = itemContext.read("$.category_id");
        String itemId = itemContext.read("$.id");
        String productId = itemContext.read("$.catalog_product_id");

        return Item
                .builder()
                .item_id(itemId)
                .catalog_product_id(productId)
                .title(itemContext.read("$.title"))
                .category_id(categoryId)
                .price(price.doubleValue())
                .sold_quantity(itemContext.read("$.sold_quantity"))
                .available_quantity(itemContext.read("$.available_quantity"))
                .sellerId(itemContext.read("$.seller.id"))
                .update_date_db(LocalDateTime.now())
                .listing_type_name(getListingTypeName(itemContext.read("$.listing_type_id")))
                .catalog_position(getPosition(itemId, productId))
                .statusCondition(imageAndSku.getStatus_condition())
                .urlImage(imageAndSku.getImage_url())
                .sku(imageAndSku.getSku())
                .created_date_item(imageAndSku.getCreated_date_item())
                .updated_date_item(imageAndSku.getUpdated_date_item())
                .build();
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(1)
    public void saveSellerItems(){
            DocumentContext json = JsonPath.parse(meliFeignClient.getSellerByNickname("MORO TECH"));

            List<Object> items = json.read("$.results[*]");

            itemRepository.saveAll(items
                    .stream()
                    .map((e) -> {
                        try {
                            return filterJsonData(JsonPath.parse(e));
                        } catch (JsonProcessingException | ParseException ex) {
                            throw new RuntimeException(ex);
                        }
                    })
                    .collect(Collectors.toList())
            );
    }

////    @EventListener(ApplicationReadyEvent.class)
////    @Order(2)
//    private void saveItem(){
//        try {
//            List<CreateItemDTO> itemAtribbutes = itemRepository.getItemAtribbutes(1152777827);
//
//            AtomicInteger i = new AtomicInteger(0);
//
//            int batchSize = 100;
//            List<Item> batch = new ArrayList<>(batchSize);
//
//            itemAtribbutes.forEach((e) ->{
//
//                DocumentContext jsonProduct = JsonPath.parse(meliFeignClient.getProductSearch(e.getCatalog_product_id()));
//                List<Object> products = jsonProduct.read("$.results[0:5]");
//
//                products.forEach((product) -> {
//                    DocumentContext productContext = JsonPath.parse(product);
//                    Number price = productContext.read("$.price");
//
//                    DocumentContext itemImageAndSku = JsonPath.parse(meliFeignClient.getImageAndSku(productContext.read("$.item_id")));
//                    List<Object> sku = itemImageAndSku.read("$.attributes[?(@.id == 'SELLER_SKU')].values[0].name");
//
//                    String getSku = null;
//
//                    if (!sku.isEmpty()){
//                        getSku = sku.get(0).toString();
//                    }
//
//                    Item item = Item
//                            .builder()
//                            .item_id(productContext.read("$.item_id"))
//                            .catalog_product_id(e.getCatalog_product_id())
//                            .title(e.getTitle())
//                            .category_id(productContext.read("$.category_id"))
//                            .price(price.doubleValue())
//                            .sold_quantity(productContext.read("$.sold_quantity"))
//                            .available_quantity(productContext.read("$.available_quantity"))
//                            .sellerId(productContext.read("$.seller_id"))
//                            .update_date_db(LocalDateTime.now())
//                            .listing_type_id(productContext.read("$.listing_type_id"))
//                            .catalog_position(i.incrementAndGet())
//                            .urlImage(itemImageAndSku.read("$.pictures[0].url"))
//                            .statusCondition(itemImageAndSku.read("$.status"))
//                            .sku(getSku)
//                            .build();
//
//                    batch.add(item);
//
//                    if(i.get() == 5) i.set(0);
//
//                    if (batch.size() >= batchSize) {
//                        itemRepository.saveAll(batch);
//                        batch.clear();
//                    }
//                });
//                if (!batch.isEmpty()) {
//                    itemRepository.saveAll(batch);
//                }
//            });
//        }catch (Exception e){
//            throw new RuntimeException(e);
//        }
//    }

}
