package com.jhsfully.inventoryManagement.entity;

import com.jhsfully.inventoryManagement.dto.OutboundDto;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "outbound")
public class OutboundEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "productid")
    private ProductEntity product;
    private String destination;
    @NotNull
    private Double amount;
    @NotNull
    private LocalDateTime at;
    private String note;

    public static OutboundDto.OutboundResponse toDto(OutboundEntity o){
        return OutboundDto.OutboundResponse.builder()
                .id(o.getId())
                .productName(o.getProduct().getName())
                .destination(o.getDestination())
                .amount(o.getAmount())
                .price(o.getProduct().getPrice())
                .at(o.getAt())
                .note(o.getNote())
                .build();
    }

}
