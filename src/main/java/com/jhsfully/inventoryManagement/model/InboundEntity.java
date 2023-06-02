package com.jhsfully.inventoryManagement.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "inbound")
public class InboundEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long purchaseid;
    private Long stockid;
    @NotNull
    private LocalDateTime at;
    @NotNull
    private Double amount;
    private String company;
    @NotNull
    private String note;

}