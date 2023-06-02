package com.jhsfully.inventoryManagement.model;

import com.jhsfully.inventoryManagement.dto.ProductDto;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "productinfo")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    private String company;
    @NotNull
    private Double price;
    private String spec;

    public static ProductDto.ProductResponse toDto(ProductEntity p){
        return ProductDto.ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .company(p.getCompany())
                .price(p.getPrice())
                .spec(p.getSpec())
                .build();
    }

}
