package com.jhsfully.inventoryManagement.entity;

import com.jhsfully.inventoryManagement.dto.OutboundDto;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "outbounddetails")
public class OutboundDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "outboundid")
    private OutboundEntity outbound;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "stockid")
    private StocksEntity stock;
    @NotNull
    private Double amount;

    public static OutboundDto.OutboundDetailResponse toDto(OutboundDetailsEntity o){
        return OutboundDto.OutboundDetailResponse.builder()
                .id(o.getId())
                .stockId(o.getStock().getId())
                .productName(o.getStock().getProduct().getName())
                .company(o.getStock().getInbound().getPurchase().getCompany())
                .amount(o.getAmount())
                .build();
    }

}
