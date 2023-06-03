package com.jhsfully.inventoryManagement.model;

import com.jhsfully.inventoryManagement.dto.PlanDto;
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
@Entity(name = "plan")
public class PlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long productid;
    @NotNull
    private LocalDate due;
    private String destination;
    @NotNull
    private Double amount;

    public static PlanDto.PlanResponse toDto(PlanEntity p){
        return PlanDto.PlanResponse.builder()
                .id(p.getId())
                .productId(p.getProductid())
                .due(p.getDue())
                .destination(p.getDestination())
                .amount(p.getAmount())
                .build();
    }

}
