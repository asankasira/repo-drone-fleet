package com.asankasi.dronefleet.model;

import javax.persistence.*;

@Entity
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    private String name;
    private String code;
    private Double unitWeight;
}
