package com.msmeli.service.feignService;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.msmeli.feignClient.ProductFeignClient;
import com.msmeli.model.Product;
import com.msmeli.repository.ProductRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeliService {

    private final ProductFeignClient productFeignClient;

    private final ProductRepository productRepository;

    public MeliService(ProductFeignClient productFeignClient, ProductRepository productRepository) {
        this.productFeignClient = productFeignClient;
        this.productRepository = productRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void saveProducts(){
        DocumentContext json = JsonPath.parse(productFeignClient.getProductSearch("MLA16224063"));

        List<Object> results = json.read("$.results[*]");

        productRepository.saveAll(results
                .stream()
                .map(JsonPath::parse)
                .map(productContext -> {
                            Number price = productContext.read("$.price");
                            return Product
                            .builder()
                            .item_id(productContext.read("$.item_id"))
                            .seller_id(productContext.read("$.seller_id"))
                            .price(price.doubleValue())
                            .available_quantity(productContext.read("$.available_quantity"))
                            .sold_quantity(productContext.read("$.sold_quantity"))
                            .category_id(productContext.read("$.category_id"))
                            .build();
                        })
                .collect(Collectors.toList())
        );
    }


}
