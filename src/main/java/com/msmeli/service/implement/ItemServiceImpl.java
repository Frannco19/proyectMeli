package com.msmeli.service.implement;

import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.dto.response.OneProductResponseDTO;
import com.msmeli.dto.response.ItemDTO;
import com.msmeli.model.Item;
import com.msmeli.repository.ItemRepository;
import com.msmeli.service.feignService.MeliService;
import com.msmeli.service.services.ItemService;
import com.msmeli.service.services.SellerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;

    private SellerService sellerService;

    private MeliService meliService;

    private ModelMapper mapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, SellerService sellerService, ModelMapper mapper) {
        this.itemRepository = itemRepository;
        this.sellerService = sellerService;
        this.meliService = meliService;
        this.mapper = mapper;
    }

    @Override
    public List<ItemDTO> getSellerItems(Integer sellerId){
        List<Item> itemList = itemRepository.getItemsBySellerId(sellerId);
        return getItemResponseDTOS(itemList);
    }

    @Override
    public List<ItemDTO> getCatalogItems(String productId) {
        List<Item> catalogItems = itemRepository.getCatalogItems(productId);
        return getItemResponseDTOS(catalogItems);
    }

    private List<ItemDTO> getItemResponseDTOS(List<Item> items) {
    @Override
    public OneProductResponseDTO getOneProduct(String productId) {
        Item item = itemRepository.findByProductId(productId);
        Optional<Seller> sellerOptional = sellerService.getSeller(item.getSellerId());

        Seller newSeller;
        newSeller = sellerOptional.orElseGet(() -> meliService.saveSeller(item.getSellerId()));

        return OneProductResponseDTO
                .builder()
                .item_id(item.getItem_id())
                .title(item.getTitle())
                .price(item.getPrice())
                .sold_quantity(item.getSold_quantity())
                .available_quantity(item.getAvailable_quantity())
                .listing_type_name(item.getListing_type_name())
                .catalog_position(item.getCatalog_position())
                .seller_nickname(newSeller.getNickname())
                .category_id(item.getCategory_id())
                .sku(item.getSku())
                .created_date_item(item.getCreated_date_item())
                .updated_date_item(item.getUpdated_date_item())
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
