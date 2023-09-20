package com.msmeli.service.feignService;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.msmeli.dto.response.CreateItemDTO;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Category;
import com.msmeli.model.Item;
import com.msmeli.model.Seller;
import com.msmeli.repository.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.msmeli.global.Global.ACCESS_TOKEN;

@Service
public class MeliService {

    private final MeliFeignClient meliFeignClient;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;
    private final SellerRatingRepository sellerRatingRepository;
    private final SellerReputationRepository sellerReputationRepository;
    private final SellerTransactionRepository sellerTransactionRepository;

    public MeliService(MeliFeignClient meliFeignClient, ItemRepository itemRepository, CategoryRepository categoryRepository, SellerRepository sellerRepository, SellerRatingRepository sellerRatingRepository, SellerReputationRepository sellerReputationRepository, SellerTransactionRepository sellerTransactionRepository) {
        this.meliFeignClient = meliFeignClient;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.sellerRepository = sellerRepository;
        this.sellerRatingRepository = sellerRatingRepository;
        this.sellerReputationRepository = sellerReputationRepository;
        this.sellerTransactionRepository = sellerTransactionRepository;
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

    public int getPosition(String categoryId){
        DocumentContext itemPosition = JsonPath.parse(meliFeignClient.getItemPositionByCategory(categoryId));

        List<Object> items = itemPosition.read("$.results[*]");

//        itemPosition.read("$.content[?(@.id == 'MLA668715299')]")
        return 0;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(1)
    public void saveSellerItems() throws Exception{
        try {
            DocumentContext json = JsonPath.parse(meliFeignClient.getSellerByNickname("MORO TECH"));

            List<Object> items = json.read("$.results[*]");

            int batchSize = 50;
            List<Item> batch = new ArrayList<>(batchSize);

            items.forEach((e) -> {
                DocumentContext itemContext = JsonPath.parse(e);
                Number price = itemContext.read("$.price");

                DocumentContext itemImageAndSku = JsonPath.parse(meliFeignClient.getImageAndSku(itemContext.read("$.id")));
                List<Object> sku = itemImageAndSku.read("$.attributes[?(@.id == 'SELLER_SKU')].values[0].name");

                Item item = Item
                                .builder()
                                .item_id(itemContext.read("$.id"))
                                .catalog_product_id(itemContext.read("$.catalog_product_id"))
                                .title(itemContext.read("$.title"))
                                .category_id(itemContext.read("$.category_id"))
                                .price(price.doubleValue())
                                .sold_quantity(itemContext.read("$.sold_quantity"))
                                .available_quantity(itemContext.read("$.available_quantity"))
                                .sellerId(itemContext.read("$.seller.id"))
                                .update_date(LocalDateTime.now())
                                .listing_type_id(itemContext.read("$.listing_type_id"))
                                .catalog_position(0)
                                .statusCondition(itemImageAndSku.read("$.status"))
                                .urlImage(itemImageAndSku.read("$.pictures[0].url"))
                                .sku(sku.get(0).toString())
                                .build();

                batch.add(item);

                if (batch.size() >= batchSize) {
                    itemRepository.saveAll(batch);
                    batch.clear();
                }
            });
            if (!batch.isEmpty()) {
                itemRepository.saveAll(batch);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(2)
    private void saveItem(){
        try {
            List<CreateItemDTO> itemAtribbutes = itemRepository.getItemAtribbutes(1152777827);

            AtomicInteger i = new AtomicInteger(0);

            int batchSize = 100;
            List<Item> batch = new ArrayList<>(batchSize);

            itemAtribbutes.forEach((e) ->{

                DocumentContext jsonProduct = JsonPath.parse(meliFeignClient.getProductSearch(e.getCatalog_product_id()));
                List<Object> products = jsonProduct.read("$.results[0:5]");

                products.forEach((product) -> {
                    DocumentContext productContext = JsonPath.parse(product);
                    Number price = productContext.read("$.price");

                    DocumentContext itemImageAndSku = JsonPath.parse(meliFeignClient.getImageAndSku(productContext.read("$.item_id")));
                    List<Object> sku = itemImageAndSku.read("$.attributes[?(@.id == 'SELLER_SKU')].values[0].name");

                    String getSku = null;

                    if (!sku.isEmpty()){
                        getSku = sku.get(0).toString();
                    }

                    Item item = Item
                            .builder()
                            .item_id(productContext.read("$.item_id"))
                            .catalog_product_id(e.getCatalog_product_id())
                            .title(e.getTitle())
                            .category_id(productContext.read("$.category_id"))
                            .price(price.doubleValue())
                            .sold_quantity(productContext.read("$.sold_quantity"))
                            .available_quantity(productContext.read("$.available_quantity"))
                            .sellerId(productContext.read("$.seller_id"))
                            .update_date(LocalDateTime.now())
                            .listing_type_id(productContext.read("$.listing_type_id"))
                            .catalog_position(i.incrementAndGet())
                            .urlImage(itemImageAndSku.read("$.pictures[0].url"))
                            .statusCondition(itemImageAndSku.read("$.status"))
                            .sku(getSku)
                            .build();

                    batch.add(item);

                    if(i.get() == 5) i.set(0);

                    if (batch.size() >= batchSize) {
                        itemRepository.saveAll(batch);
                        batch.clear();
                    }
                });
                if (!batch.isEmpty()) {
                    itemRepository.saveAll(batch);
                }
            });
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
