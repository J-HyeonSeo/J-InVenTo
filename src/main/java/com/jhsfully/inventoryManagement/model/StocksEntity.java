package com.jhsfully.inventoryManagement.model;

import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Entity(name = "stocks")
public class StocksEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long pid;
    @NotNull
    private Double amount;
    @NotNull
    private LocalDate lot;
    @NotNull
    private String company;

    public static StocksDto.StockResponse toDto(StocksEntity s){
        return StocksDto.StockResponse.builder()
                .pid(s.getId())
                .amount(s.getAmount())
                .build();
    }
    public static StocksDto.StockResponseLot toLotDto(StocksEntity s){
        return StocksDto.StockResponseLot.builder()
                .id(s.getId())
                .amount(s.getAmount())
                .lot(s.getLot())
                .company(s.getCompany())
                .build();
    }

}
