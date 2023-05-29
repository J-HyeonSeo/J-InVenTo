package com.jhsfully.inventoryManagement.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@Entity()
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
    private long id;
    @NotNull
    private long pid;
    @NotNull
    private long cid;
    @NotNull
    private double cost;

}
