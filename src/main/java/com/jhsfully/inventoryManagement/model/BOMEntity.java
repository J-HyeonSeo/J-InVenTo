package com.jhsfully.inventoryManagement.model;

import com.jhsfully.inventoryManagement.dto.BomDto;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(
        name = "bom",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"pid", "cid"}
                )
        }
)
public class BOMEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long pid;
    @NotNull
    private Long cid;
    @NotNull
    private Double cost;

    public static BomDto.BomResponse toDto(BOMEntity b){
        return BomDto.BomResponse.builder()
                .id(b.getId())
                .pid(b.getPid())
                .cid(b.getCid())
                .cost(b.getCost())
                .build();
    }

}
