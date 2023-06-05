package com.jhsfully.inventoryManagement.model;

import com.jhsfully.inventoryManagement.dto.BomDto;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "bom")
public class BOMEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "pid")
    private ProductEntity parentProduct;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "cid")
    private ProductEntity childProduct;
    @NotNull
    private Double cost;

    public static BomDto.BomResponse toDto(BOMEntity b){
        return BomDto.BomResponse.builder()
                .id(b.getId())
                .pid(b.getParentProduct().getId())
                .parentName(b.getParentProduct().getName())
                .cid(b.getChildProduct().getId())
                .childName(b.getChildProduct().getName())
                .cost(b.getCost())
                .build();
    }

}
