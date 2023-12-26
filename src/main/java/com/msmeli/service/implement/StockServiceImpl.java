package com.msmeli.service.implement;

import com.msmeli.dto.StockDTO;
import com.msmeli.dto.request.StockRequestDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Stock;
import com.msmeli.model.UserEntity;
import com.msmeli.repository.StockRepository;
import com.msmeli.repository.UserEntityRepository;
import com.msmeli.service.services.StockService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final UserEntityRepository userEntityRepository;
    private final ModelMapper modelMapper;

    /**
     * Servicio que gestiona las operaciones relacionadas con el stock.
     */
    public StockServiceImpl(StockRepository stockRepository, UserEntityRepository userEntityRepository) {
        this.stockRepository = stockRepository;
        this.userEntityRepository = userEntityRepository;
        this.modelMapper = new ModelMapper();
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id El ID del usuario.
     * @return La entidad del usuario correspondiente al ID.
     * @throws EntityNotFoundException Si no se encuentra ningún usuario con el ID proporcionado.
     */
    @Override
    public UserEntity getUserById(Long id) {
        return userEntityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    /**
     * Guarda las existencias de un usuario según la solicitud proporcionada.
     *
     * @param requestDTO La solicitud que contiene las existencias del usuario a guardar.
     */
    @Override
    public void saveUserStock(StockRequestDTO requestDTO) {
        stockRepository.saveAll(requestDTO.getContent()
                .parallelStream()
                .map(e -> {
                    Stock userStock = modelMapper.map(e, Stock.class);
                    userStock.setUser_id(getUserById(requestDTO.getUser_id()));
                    userStock.setPrice(Math.round(userStock.getPrice() * 100.0) / 100.0);
                    return userStock;
                }).collect(Collectors.toList()));
    }

    /**
     * Encuentra la última existencia por SKU.
     *
     * @param sku El SKU de la existencia.
     * @return La existencia correspondiente al SKU.
     */
    @Override
    public Stock findLastBySku(String sku) {
        return stockRepository.findBySku(sku);
    }

    /**
     * Obtiene el total de existencias por SKU.
     *
     * @param sku El SKU de las existencias.
     * @return El total de existencias para el SKU.
     */
    @Override
    public Integer getTotalStockBySku(String sku) {
        return stockRepository.getTotalBySku(sku);
    }

    /**
     * Obtiene todas las existencias mapeadas para un vendedor.
     *
     * @param sellerId El ID del vendedor.
     * @return Lista de existencias mapeadas para el vendedor.
     */
    @Override
    public List<StockDTO> findAllMapped(Long sellerId) {
        return stockRepository.findAllBySellerId(sellerId).stream().map(stock -> modelMapper.map(stock, StockDTO.class)).toList();
    }

    /**
     * Obtiene todas las existencias para un vendedor.
     *
     * @param sellerId El ID del vendedor.
     * @return Lista de todas las existencias para el vendedor.
     * @throws ResourceNotFoundException Si el vendedor no tiene existencias.
     */
    @Override
    public List<Stock> findAll(Long sellerId) throws ResourceNotFoundException {
        List<Stock> stockList = stockRepository.findAllBySellerId(sellerId);
        if(stockList.isEmpty()) throw new ResourceNotFoundException("El seller buscado no posee stock");
        return stockList;
    }

    /**
     * Obtiene todas las existencias paginadas para un vendedor.
     *
     * @param sellerId El ID del vendedor.
     * @param pageable La información de paginación.
     * @return Página de existencias para el vendedor.
     * @throws ResourceNotFoundException Si el vendedor no tiene existencias.
     */
    public Page<Stock> findAllPaged(Long sellerId, Pageable pageable) throws ResourceNotFoundException {
        Page<Stock> stockList = stockRepository.findAllBySellerId(sellerId,pageable);
        if(stockList.getContent().isEmpty()) throw new ResourceNotFoundException("El seller buscado no posee stock");
        return stockList;
    }


    public List<StockDTO> findAllByUserId(Long userId) {
        List<Stock> stockList = stockRepository.findAllByUserId(userId);

        // Ahora, crea instancias de StockDTO a partir de Stock
        List<StockDTO> stockDTOList = stockList.stream()
                .map(stock -> new StockDTO(
                        stock.getId(),
                        stock.getSku(),
                        stock.getAvailable_quantity(),
                        stock.getPrice(),
                        stock.getRegister_date(),
                        // Aquí deberías crear una instancia de SupplierStockResponseDTO si es necesario
                        null  // Por ahora, establezco null, ya que no proporcionaste cómo obtener SupplierStockResponseDTO
                ))
                .collect(Collectors.toList());

        return stockDTOList;
    }





}
