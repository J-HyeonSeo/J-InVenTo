package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.model.ProductEntity;
import com.jhsfully.inventoryManagement.model.StocksEntity;
import com.jhsfully.inventoryManagement.repository.ProductRepository;
import com.jhsfully.inventoryManagement.repository.StocksRepository;
import com.jhsfully.inventoryManagement.type.ProductErrorType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StocksService implements StocksInterface {
    private final StocksRepository stocksRepository;
    private final ProductRepository productRepository;

    @Override
    public List<StocksDto.StockResponse> getAllStocks() {
//        List<StocksEntity> stocks = stocksRepository.findStockGroupPid();
//        List<StocksDto.StockResponse> responses = stocks.stream()
//                .map(StocksEntity::toDto).collect(Collectors.toList());
//        for(var response : responses){
//            ProductEntity entity = productRepository.findById(response.getPid())
//                    .orElseThrow(() -> new ProductException(ProductErrorType.PRODUCT_NOT_FOUND));
//
//            String productName = entity.getName();
//            String spec = entity.getSpec();
//
//            response.setProductName(productName);
//            response.setSpec(spec);
//        }
        return null;
    }

    @Override
    public List<StocksDto.StockResponseLot> getLotByPid(Long pid) {
        return stocksRepository.findByPid(pid)
                .stream()
                .map(StocksEntity::toLotDto)
                .collect(Collectors.toList());
    }

    @Override
    public StocksDto.StockResponseLot addStock(StocksDto.StockAddRequest request) {
        return null;
    }

    @Override
    public void spendStockById(Long id) {

    }

    @Override
    public void deleteStock(Long id) {

    }
}
