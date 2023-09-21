package com.msmeli.service.implement;

import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.dto.response.OneProductResponseDTO;
import com.msmeli.model.Item;
import com.msmeli.model.Seller;
import com.msmeli.repository.ItemRepository;
import com.msmeli.service.feignService.MeliService;
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

    private MeliService meliService;

    private ModelMapper mapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, SellerService sellerService, MeliService meliService, ModelMapper mapper) {
        this.itemRepository = itemRepository;
        this.sellerService = sellerService;
        this.meliService = meliService;
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
                        .listing_type_name(e.getListing_type_name())
                        .catalog_position(e.getCatalog_position())
//                        .seller_nickname(e.getSeller_nickname())
                        .category_id(e.getCategory_id())
                        .statusCondition(e.getStatusCondition())
                        .urlImage(e.getUrlImage())
                        .sku(e.getSku())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
