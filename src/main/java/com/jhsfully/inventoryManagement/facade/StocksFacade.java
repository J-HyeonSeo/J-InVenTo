package com.jhsfully.inventoryManagement.facade;

import com.jhsfully.inventoryManagement.dto.BomDto;
import com.jhsfully.inventoryManagement.dto.PlanDto;
import com.jhsfully.inventoryManagement.dto.ProductDto;
import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.service.BomInterface;
import com.jhsfully.inventoryManagement.service.PlanInterface;
import com.jhsfully.inventoryManagement.service.ProductInterface;
import com.jhsfully.inventoryManagement.service.StocksInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//부족자재파악을 위한 파서드.
@Slf4j
@Component
@AllArgsConstructor
public class StocksFacade {

    private final ProductInterface productService;
    private final StocksInterface stocksService;
    private final BomInterface bomService;
    private final PlanInterface planService;

    public List<StocksDto.StockResponse> getAllStocks(){

        //모든 품목 정보들을 가져와야함.
        List<ProductDto.ProductResponse> products = productService.getProducts();

        //모든 재고 정보들을 가져옴.
        HashMap<Long, Double> stocks = new HashMap<>();

        stocksService.getAllStocks().stream()
                .forEach(x -> stocks.put(x.getProductId(), x.getAmount()));

        //반환할 데이터
        HashMap<Long, StocksDto.StockResponse> responses = new HashMap<>();
        for(var product : products){
            responses.put(product.getId(),
                    StocksDto.StockResponse.builder()
                            .productId(product.getId())
                            .productName(product.getName())
                            .spec(product.getSpec())
                            .amount(stocks.containsKey(product.getId()) ?
                                    stocks.get(product.getId()) : 0D)
                            .build());
        }


        //오늘을 기준으로 예정된 모든 출고 계획을 가져옴.(오름차순으로 가져와짐)
        List<PlanDto.PlanResponse> plans = planService.getPlans(LocalDate.now(), null);


        for(var plan : plans){ //plans를 순회함.

            //해당되는 plan의 최하단 leaf BOM가져옴.
            List<BomDto.BomLeaf> productsLeaves = bomService.getLeafProducts(plan.getProductId());

            for(var productLeaf : productsLeaves) {
                if (responses.containsKey(productLeaf.getProductId())) {

                    var response = responses.get(productLeaf.getProductId());

                    Double nowAmount = response.getAmount();
                    nowAmount -= plan.getAmount() * productLeaf.getCost();

                    if (nowAmount <= 0 && response.getLackDate() == null) {
                        response.setLackDate(plan.getDue());
                    }
                    response.setAmount(nowAmount);
                }
            }
        }

        //반환할 데이터 값 정리
        for(var response : responses.values()){
            if(response.getAmount() <= 0){
                response.setLackAmount(Math.abs(response.getAmount()));
            }
            response.setAmount(stocks.containsKey(response.getProductId()) ?
                    stocks.get(response.getProductId()) : 0D);
        }

        return responses.values().stream().collect(Collectors.toList());
    }

}
