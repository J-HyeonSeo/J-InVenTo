package com.jhsfully.inventoryManagement.model;

import com.jhsfully.inventoryManagement.dto.InboundDto;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "inbound")
public class InboundEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "purchaseid")
    private PurchaseEntity purchase;
    @NotNull
    @OneToOne
    @JoinColumn(name = "stockid")
    private StocksEntity stock;
    @NotNull
    private LocalDateTime at;
    @NotNull
    private Double amount;
    @NotNull
    private String note;

    //확인하는 용도
    public static InboundDto.InboundResponse toDto(InboundEntity i){
        return InboundDto.InboundResponse.builder()
                .id(i.getId())
                .stockId(i.getStock().getId())
                .purchaseId(i.getPurchase().getId())
                .productName(i.getStock().getProduct().getName())
                .inboundAt(i.getAt())
                .purchasedAt(i.getPurchase().getAt())
                .amount(i.getAmount())
                .company(i.getPurchase().getCompany())
                .note(i.getNote())
                .build();
    }
}
