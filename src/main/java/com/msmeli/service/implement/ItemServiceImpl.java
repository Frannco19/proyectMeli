package com.msmeli.service.implement;

import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.model.Item;
import com.msmeli.model.Seller;
import com.msmeli.repository.ItemRepository;
import com.msmeli.service.services.ItemService;
import com.msmeli.service.services.SellerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;

    private SellerService sellerService;

    private ModelMapper mapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, SellerService sellerService, ModelMapper mapper) {
        this.itemRepository = itemRepository;
        this.sellerService = sellerService;
        this.mapper = mapper;
    }

    @Override
    public List<ItemResponseDTO> getSellerItems(Integer sellerId){
        List<Item> itemList = itemRepository.getItemsBySellerId(sellerId);
        return getItemResponseDTOS(itemList);
    }

    @Override
    public List<ItemResponseDTO> getCatalogItems(String productId) {
        List<Item> catalogItems = itemRepository.getCatalogItems(productId);
        return getItemResponseDTOS(catalogItems);
    }

    private List<ItemResponseDTO> getItemResponseDTOS(List<Item> items) {
        return items
                .stream()
                .map((e) -> ItemResponseDTO
                        .builder()
                        .item_id(e.getItem_id())
                        .title(e.getTitle())
                        .catalog_product_id(e.getCatalog_product_id())
                        .price(e.getPrice())
                        .sold_quantity(e.getSold_quantity())
                        .available_quantity(e.getAvailable_quantity())
                        .listing_type_id(e.getListing_type_id())
                        .catalog_position(e.getCatalog_position())
                        .seller_id(e.getSellerId().getSellerId())
                        .category_id(e.getCategory_id().getCategoryId())
                        .statusCondition(e.getStatusCondition())
                        .urlImage(e.getUrlImage())
                        .sku(e.getSku())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
