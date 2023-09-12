package com.msmeli.service.feignService;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Category;
import com.msmeli.model.Item;
import com.msmeli.model.Seller;
import com.msmeli.repository.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
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

    private void saveItem(String catalog_product_id, String product_title){
        DocumentContext jsonProduct = JsonPath.parse(meliFeignClient.getProductSearch(catalog_product_id));

        List<Object> products = jsonProduct.read("$.results[0:5]");
        AtomicInteger i = new AtomicInteger(0);
        itemRepository.saveAll(
                products
                        .stream()
                        .map(JsonPath::parse)
                        .map(productContext -> {
                            Number price = productContext.read("$.price");
                            try {
                                return Item
                                        .builder()
                                        .item_id(productContext.read("$.item_id"))
                                        .catalog_product_id(catalog_product_id)
                                        .title(product_title)
                                        .category_id(saveCategory(productContext.read("$.category_id")))
                                        .price(price.doubleValue())
                                        .sold_quantity(productContext.read("$.sold_quantity"))
                                        .available_quantity(productContext.read("$.available_quantity"))
                                        .seller_id(saveSeller(productContext.read("$.seller_id")))
                                        .update_date(LocalDateTime.now())
                                        .listing_type_id(productContext.read("$.listing_type_id"))
                                        .catalog_position(i.incrementAndGet())
                                        .build();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toList())
        );
    }

    @EventListener(ApplicationReadyEvent.class)
    public void saveSellerItems() throws Exception{
        DocumentContext json = JsonPath.parse(meliFeignClient.getSellerByNickname("MORO TECH"));

        List<Object> items = json.read("$.results[*]");

        itemRepository.saveAll(items
                .stream()
                .map(JsonPath::parse)
                .map(itemContext -> {
                    Number price = itemContext.read("$.price");
                    String catalog_product_id = itemContext.read("$.catalog_product_id");
                    String title = itemContext.read("$.title");
                    try {
                        return Item
                                .builder()
                                .item_id(itemContext.read("$.id"))
                                .catalog_product_id(catalog_product_id)
                                .title(title)
                                .category_id(saveCategory(itemContext.read("$.category_id")))
                                .price(price.doubleValue())
                                .sold_quantity(itemContext.read("$.sold_quantity"))
                                .available_quantity(itemContext.read("$.available_quantity"))
                                .seller_id(saveSeller(itemContext.read("$.seller.id")))
                                .update_date(LocalDateTime.now())
                                .listing_type_id(itemContext.read("$.listing_type_id"))
                                .catalog_position(0)
                                .build();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }finally {
                        saveItem(catalog_product_id,title);
                    }


                })
                .collect(Collectors.toList()));
    }


}
