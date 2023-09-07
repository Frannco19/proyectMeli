package com.msmeli.service.feignService;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Seller;
import com.msmeli.repository.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

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

//    @EventListener(ApplicationReadyEvent.class)
    public void saveSeller(){

        DocumentContext json = JsonPath.parse(meliFeignClient.getSellerByNickname("MORO TECH"));

        DocumentContext seller = json.read("$.seller");

        sellerRepository.save(Seller
                .builder()
                .seller_id(seller.read("$.id"))
                .nickname(seller.read("$.nickname"))
                .build());
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
//                            .item_id(productContext.read("$.item_id"))
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
