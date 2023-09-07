package com.msmeli.service.feignService;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Item;
import com.msmeli.model.Product;
import com.msmeli.model.Seller;
import com.msmeli.repository.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.List;
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
                        .seller_id(sellerJson.read("$.id"))
                        .nickname(sellerJson.read("$.nickname"))
                        .build());
    }

    public Product getProductById(String productId) throws Exception {
        return productRepository.findById(productId).orElseThrow(() -> new Exception("Product not found"));
    }

    @EventListener(ApplicationReadyEvent.class)
    public void saveSellerItems(){
        DocumentContext json = JsonPath.parse(meliFeignClient.getSellerByNickname("MORO TECH"));

        List<Object> items = json.read("$.results[*]");

        itemRepository.saveAll(items
                .stream()
                .map(JsonPath::parse)
                .map(itemContext -> {
                    Number price = itemContext.read("$.price");
                    return Item
                            .builder()
                            .item_id(itemContext.read("$.id"))
                            .product(itemContext.read("$.catalog_product_id"))
                            .title(itemContext.read("$.title"))
                            .status_condition(itemContext.read("$.condition"))
                            .category(itemContext.read("$.category_id"))
                            .price(price.doubleValue())
                            .sold_quantity(itemContext.read("$.sold_quantity"))
                            .available_quantity(itemContext.read("$.available_quantity"))
                            .seller(itemContext.read("$.seller.id"))
                            .build();
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
