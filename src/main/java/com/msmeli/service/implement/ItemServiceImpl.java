package com.msmeli.service.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msmeli.dto.response.ItemDTO;
import com.msmeli.dto.response.OneProductResponseDTO;
import com.msmeli.dto.response.SellerResponseDTO;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Item;
import com.msmeli.repository.ItemRepository;
import com.msmeli.service.feignService.MeliService;
import com.msmeli.service.services.ItemService;
import com.msmeli.service.services.SellerService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final MeliFeignClient meliFeignClient;

    private final SellerService sellerService;

    private final MeliService meliService;

    private final ModelMapper mapper;

    public ItemServiceImpl(ItemRepository itemRepository, MeliFeignClient meliFeignClient, SellerService sellerService, MeliService meliService, ModelMapper mapper) {
        this.itemRepository = itemRepository;
        this.meliFeignClient = meliFeignClient;
        this.sellerService = sellerService;
        this.meliService = meliService;
        this.mapper = mapper;
    }


    @Override
    public List<ItemDTO> getSellerItems(Integer sellerId) {
        List<Item> itemList = itemRepository.getItemsBySellerId(sellerId);
        return getItemResponseDTOS(itemList);
    }

    @Override
    public List<ItemDTO> getCatalogItems(String productId) {
        List<Item> catalogItems = itemRepository.getCatalogItems(productId);
        return getItemResponseDTOS(catalogItems);
    }


    //TODO revision getOneProduct
    @Override
    public OneProductResponseDTO getOneProduct(String productId) throws JsonProcessingException {
        Item item = itemRepository.findByProductId(productId);

        SellerResponseDTO seller = meliFeignClient.getSellerBySellerId(item.getSellerId());
        String typeName = meliService.getListingTypeName(item.getListing_type_id());

        return OneProductResponseDTO.builder()
                .title(item.getTitle())
                .catalog_product_id(item.getCatalog_product_id())
                .price(item.getPrice())
                .sold_quantity(item.getSold_quantity())
                .available_quantity(item.getAvailable_quantity())
                .listing_type_name(typeName)
                .catalog_position(item.getCatalog_position())
                .seller_nickname(seller.getSeller().getNickname())
                .category_id(item.getCategory_id())
                .created_date_item(item.getCreated_date_item())
                .updated_date_item(item.getUpdated_date_item())
                .image_url(item.getImage_url())
                .sku(item.getSku())
                .build();

    }

    private List<ItemDTO> getItemResponseDTOS(List<Item> items) {
        return items
                .stream()
                .map((e) -> ItemDTO
                        .builder()
                        .item_id(e.getId())
                        .title(e.getTitle())
                        .catalog_product_id(e.getCatalog_product_id())
                        .price(e.getPrice())
                        .sold_quantity(e.getSold_quantity())
                        .available_quantity(e.getAvailable_quantity())
                        .listing_type_id(e.getListing_type_id())
                        .catalog_position(e.getCatalog_position())
                        .category_id(e.getCategory_id())
                        .status_condition(e.getStatus_condition())
                        .image_url(e.getImage_url())
                        .sku(e.getSku())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
