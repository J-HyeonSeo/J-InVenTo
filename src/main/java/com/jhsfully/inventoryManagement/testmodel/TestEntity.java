package com.jhsfully.inventoryManagement.testmodel;

import javax.persistence.*;
import java.util.List;

@Entity(name = "TEST")
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}
