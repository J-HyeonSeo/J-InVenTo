package com.jhsfully.inventoryManagement.testmodel;

import javax.persistence.*;

@Entity(name = "TESTDETAIL")
public class TestDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pid")
    private TestEntity parentProduct;

    @ManyToOne
    @JoinColumn(name = "cid")
    private TestEntity childProduct;

    private String note;

}
