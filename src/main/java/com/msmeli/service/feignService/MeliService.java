package com.msmeli.service.feignService;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Category;
import com.msmeli.model.Item;
import com.msmeli.model.Product;
import com.msmeli.model.Seller;
import com.msmeli.repository.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.swing.text.Document;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeliService {

    private final MeliFeignClient meliFeignClient;
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;
    private final SellerRatingRepository sellerRatingRepository;
    private final SellerReputationRepository sellerReputationRepository;
    private final SellerTransactionRepository sellerTransactionRepository;

    public MeliService(MeliFeignClient meliFeignClient, ProductRepository productRepository, ItemRepository itemRepository, CategoryRepository categoryRepository, SellerRepository sellerRepository, SellerRatingRepository sellerRatingRepository, SellerReputationRepository sellerReputationRepository, SellerTransactionRepository sellerTransactionRepository) {
        this.meliFeignClient = meliFeignClient;
        this.productRepository = productRepository;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.sellerRepository = sellerRepository;
        this.sellerRatingRepository = sellerRatingRepository;
        this.sellerReputationRepository = sellerReputationRepository;
        this.sellerTransactionRepository = sellerTransactionRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void saveSeller(){
        DocumentContext json = JsonPath.parse(meliFeignClient.getSellerByNickname("MORO TECH"));

        DocumentContext sellerJson = JsonPath.parse((Object) json.read("$.seller"));

        sellerRepository.save(
                    Seller
                        .builder()
                        .sellerId(sellerJson.read("$.id"))
                        .nickname(sellerJson.read("$.nickname"))
                        .build());
    }

    public Product getProductById(String productId) throws Exception {
        return productRepository.findById(productId).orElseThrow(() -> new Exception("Product not found"));
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

//    @EventListener(ApplicationReadyEvent.class)
//    public Product saveProduct(String productId){
//        DocumentContext jsonItem = JsonPath.parse(meliFeignClient.getSellerByNickname("MORO TECH"));
//
//
//        DocumentContext jsonProduct = JsonPath.parse(meliFeignClient.getProductSearch(productId));
//        DocumentContext jsonSeller = JsonPath.parse(meliFeignClient.getSellerNickname(jsonProduct.read("$.seller_id")));
//
//        List<Object> products = jsonProduct.read("$.results[0:5]");
//
//        productRepository.saveAll(
//            products
//                    .stream()
//                    .map(JsonPath::parse)
//                    .map(productContext -> {
//                        Number price = productContext.read("$.price");
//                        return Product
//                                .builder()
//                                .productId(productId)
//                                .soldQuantity(productContext.read("$.sold_quantity"))
//                                .available_quantity(productContext.read("$.available_quantity"))
//                                .productName(jsonItem.read("$.title"))
//                                .statusCondition(productContext.read("$.condition"))
//                                .listing_type_id(productContext.read("$.listing_type_id"))
//                                .price(price.doubleValue())
//                                .seller_name(jsonSeller.read("$.nickname"))
//                                .build();
//                    })
//                    .collect(Collectors.toList())
//        );
//        return null;
//    }


    @EventListener(ApplicationReadyEvent.class)
    public void saveSellerItems(){
        DocumentContext json = JsonPath.parse(meliFeignClient.getSellerByNickname("MORO TECH"));

        List<Object> items = json.read("$.results[*]");

        itemRepository.saveAll(items
                .stream()
                .map(JsonPath::parse)
                .map(itemContext -> {

                    Number price = itemContext.read("$.price");


                    try {
                        return Item
                                .builder()
                                .itemId(itemContext.read("$.id"))
    //                            .productId(saveProduct(itemContext.read("$.catalog_product_id")))
                                .title(itemContext.read("$.title"))
                                .statusCondition(itemContext.read("$.condition"))
                                .categoryId(saveCategory(itemContext.read("$.category_id")))
                                .price(price.doubleValue())
                                .soldQuantity(itemContext.read("$.sold_quantity"))
                                .availableQuantity(itemContext.read("$.available_quantity"))
                                .sellerId(getSeller(itemContext.read("$.seller_id")))
                                .build();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                })
                .collect(Collectors.toList()));
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void saveProducts(){
//        DocumentContext json = JsonPath.parse(meliFeignClient.getProductSearch("MLA16224063"));
//
//        List<Object> results = json.read("$.results[*]");
//
//        productRepository.saveAll(results
//                .stream()
//                .map(JsonPath::parse)
//                .map(productContext -> {
//                            Number price = productContext.read("$.price");
//                            return Product
//                            .builder()
//                            .product_id(productContext.read("$.item_id"))
//                            .seller_id(productContext.read("$.seller_id"))
//                            .price(price.doubleValue())
//                            .available_quantity(productContext.read("$.available_quantity"))
//                            .sold_quantity(productContext.read("$.sold_quantity"))
//                            .category_id(productContext.read("$.category_id"))
//                            .build();
//                        })
//                .collect(Collectors.toList())
//        );
//    }


}
