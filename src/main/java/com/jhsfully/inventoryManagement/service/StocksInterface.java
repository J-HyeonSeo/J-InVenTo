package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.StocksDto;

import java.util.List;

public interface StocksInterface {

    public List<StocksDto.StockGroupResponse> getAllStocks();

    public List<StocksDto.StockResponseLot> getLotByPid(Long productid);

    public StocksDto.StockResponseLot addStock(StocksDto.StockAddRequest request);

    public void spendStockById(Long id, Double amount);

    public void deleteStock(Long id);

}
