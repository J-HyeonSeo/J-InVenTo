package com.jhsfully.inventoryManagement.model;

import com.jhsfully.inventoryManagement.dto.PurchaseDto;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "purchase")
public class PurchaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "productid")
    private ProductEntity product;
    @NotNull
    private LocalDateTime at;
    @NotNull
    private Double amount;
    @NotNull
    private Double price;
    private String company;
    private String note;

    public static PurchaseDto.PurchaseResponse toDto(PurchaseEntity p){
        return PurchaseDto.PurchaseResponse.builder()
                .id(p.getId())
                .productId(p.getProduct().getId())
                .productName(p.getProduct().getName())
                .at(p.getAt())
                .amount(p.getAmount())
                .price(p.getPrice())
                .company(p.getCompany())
                .note(p.getNote())
                .build();
    }

}
