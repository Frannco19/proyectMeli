package com.msmeli.service.implement;


import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Item;
import com.msmeli.model.SellerRefactor;
import com.msmeli.repository.ItemRepository;
import com.msmeli.repository.SellerRefactorRepository;
import com.msmeli.service.feignService.MeliService;
import com.msmeli.service.services.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {




}
