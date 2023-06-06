package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.exception.InboundException;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.exception.StocksException;
import com.jhsfully.inventoryManagement.model.InboundEntity;
import com.jhsfully.inventoryManagement.model.ProductEntity;
import com.jhsfully.inventoryManagement.model.StocksEntity;
import com.jhsfully.inventoryManagement.repository.InboundRepository;
import com.jhsfully.inventoryManagement.repository.ProductRepository;
import com.jhsfully.inventoryManagement.repository.StocksRepository;
import com.jhsfully.inventoryManagement.type.InboundErrorType;
import com.jhsfully.inventoryManagement.type.StocksErrorType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.jhsfully.inventoryManagement.type.InboundErrorType.*;
import static com.jhsfully.inventoryManagement.type.ProductErrorType.PRODUCT_NOT_FOUND;
import static com.jhsfully.inventoryManagement.type.StocksErrorType.*;

@Service
@AllArgsConstructor
@Transactional
public class StocksService implements StocksInterface {
    private final StocksRepository stocksRepository;
    private final ProductRepository productRepository;
    private final InboundRepository inboundRepository;

    @Override
    public List<StocksDto.StockGroupResponse> getAllStocks() {
        return stocksRepository.getStocksGroupProduct();
    }

    @Override
    public List<StocksDto.StockResponseLot> getLotByPid(Long productid) {

        ProductEntity product = productRepository.findById(productid)
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        return stocksRepository.findByProduct(product)
                .stream()
                .map(StocksEntity::toLotDto)
                .collect(Collectors.toList());
    }

    @Override
    public StocksDto.StockResponseLot addStock(StocksDto.StockAddRequest request) {

        ProductEntity productEntity = validateAddStock(request);

        StocksEntity stocksEntity = StocksEntity.builder()
                .product(productEntity)
                .amount(request.getAmount())
                .lot(request.getLot())
                .build();

        return StocksEntity.toLotDto(stocksRepository.save(stocksEntity));
    }

    @Override
    public void addInbound(Long stockId, Long inboundId){
        StocksEntity stock = stocksRepository.findById(stockId)
                .orElseThrow(() -> new StocksException(STOCKS_NOT_FOUND));

        InboundEntity inbound = inboundRepository.findById(inboundId)
                        .orElseThrow(() -> new InboundException(INBOUND_NOT_FOUND));

        stock.setInbound(inbound);
        stocksRepository.save(stock);
    }

    @Override
    public void spendStockById(Long id, Double amount) {
        StocksEntity stocksEntity = stocksRepository.findById(id)
                .orElseThrow(() -> new StocksException(STOCKS_NOT_FOUND));

        if(amount == null){
            throw new StocksException(STOCKS_AMOUNT_NULL);
        }

        if(amount <= 0){
            throw new StocksException(STOCKS_CANT_SPEND_OR_LESS_ZERO);
        }

        stocksEntity.spendAmount(amount);

        stocksRepository.save(stocksEntity);
    }

    @Override
    public void deleteStock(Long id) {
        //구현 보류(출고 서비스가 발생하여야 구현 가능)
    }

    //======================== Validates ======================================
    public ProductEntity validateAddStock(StocksDto.StockAddRequest request){

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        if(request.getAmount() == null){
            throw new StocksException(STOCKS_AMOUNT_NULL);
        }

        if(request.getAmount() <= 0){
            throw new StocksException(STOCKS_NOT_CREATE_OR_LESS_ZERO);
        }

        return product;
    }
}
