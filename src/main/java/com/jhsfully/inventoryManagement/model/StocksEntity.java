package com.jhsfully.inventoryManagement.model;

import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    private String company;

    public static StocksDto.StockResponseLot toLotDto(StocksEntity s){
        return StocksDto.StockResponseLot.builder()
                .id(s.getId())
                .amount(s.getAmount())
                .lot(s.getLot())
                .company(s.getCompany())
                .build();
    }

    public void spendAmount(double amount){
        if(this.amount < amount){
            throw new RuntimeException("임시 오류!");
        }
        this.amount -= amount;
    }

}
