package com.msmeli.service.implement;

import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.model.Item;
import com.msmeli.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ItemServiceImplTest {

    @MockBean
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;
    @Test
    public void testGetSellerItems() {
        Integer sellerId = 1;
        int offset = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(offset, pageSize);
        List<Item> items = Collections.singletonList(new Item()); // reemplaza esto con tus propios datos
        Page<Item> itemPage = new PageImpl<>(items, PageRequest.of(0, 10), items.size());

        Mockito.when(itemRepository.getItemsBySellerId(Mockito.anyInt(), Mockito.any(PageRequest.class))).thenReturn(itemPage);
    }

//    @Test
//    public void testGetCatalogItems() {
//        Integer sellerId = 1;
//        int offset = 0;
//        int pageSize = 10;
//        Pageable pageable = PageRequest.of(offset, pageSize);
//        List<Item> items = Collections.singletonList(new Item()); // reemplaza esto con tus propios datos
//
//                when(itemRepository.getCatalogItems(sellerId, pageable)).thenReturn();
//
//        Page<ItemResponseDTO> result = itemService.getCatalogItems(sellerId, offset, pageSize);
//
//        assertEquals(items, result);
//    }
}