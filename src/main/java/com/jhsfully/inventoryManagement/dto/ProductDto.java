package com.jhsfully.inventoryManagement.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private String name;
    private String company;
    private double price;
    private String spec;


}
