package com.jhsfully.inventoryManagement.entity;

import com.jhsfully.inventoryManagement.dto.PlanDto;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "plan")
public class PlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "productid")
    private ProductEntity product;
    @NotNull
    private LocalDate due;
    private String destination;
    @NotNull
    private Double amount;

    public static PlanDto.PlanResponse toDto(PlanEntity p){
        return PlanDto.PlanResponse.builder()
                .id(p.getId())
                .productId(p.getProduct().getId())
                .productName(p.getProduct().getName())
                .due(p.getDue())
                .destination(p.getDestination())
                .amount(p.getAmount())
                .build();
    }

}
