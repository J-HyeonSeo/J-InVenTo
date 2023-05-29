package com.jhsfully.inventoryManagement.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "member")
public class memberEntity {

    @Id
    @GeneratedValue
    private long id;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String name;
    private String department;

}
