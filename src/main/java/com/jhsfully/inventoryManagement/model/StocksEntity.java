package com.jhsfully.inventoryManagement.model;

import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.exception.StocksException;
import com.jhsfully.inventoryManagement.type.StocksErrorType;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
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
    @ManyToOne
    @JoinColumn(name = "productid")
    private ProductEntity product;

    @OneToOne
    @JoinColumn(name = "inboundid")
    private InboundEntity inbound;

    @NotNull
    private Double amount;
    @NotNull
    private LocalDate lot;

    public static StocksDto.StockResponseLot toLotDto(StocksEntity s){
        return StocksDto.StockResponseLot.builder()
                .id(s.getId())
                .productId(s.getProduct().getId())
                .productName(s.getProduct().getName())
                .amount(s.getAmount())
                .lot(s.getLot())
                .company(s.getInbound() != null ? s.getInbound().getPurchase().getCompany()
                        : null)
                .build();
    }

    public void spendAmount(double amount){
        if(this.amount < amount){
            throw new StocksException(StocksErrorType.STOCKS_CANT_SPEND_OR_LESS_ZERO);
        }
        this.amount -= amount;
    }

    public void cancelSpendAmount(double amount){
        if(this.amount < amount){
            throw new StocksException(StocksErrorType.STOCKS_CANT_CANCEL_SPEND);
        }
        this.amount += amount;
    }

}
