package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.model.InboundEntity;

import java.util.List;

public interface StocksInterface {
    public StocksDto.StockResponseLot getStock(Long id);

    public List<StocksDto.StockGroupResponse> getAllStocks();

    public List<StocksDto.StockResponseLot> getLotByPid(Long productid);

    public StocksDto.StockResponseLot addStock(StocksDto.StockAddRequest request);

    public void addInbound(Long stockId, Long inboundId);

    public void spendStockById(Long id, Double amount);

    public void cancelSpendStockById(Long id, Double amount);

    public void deleteStock(Long id);

}
