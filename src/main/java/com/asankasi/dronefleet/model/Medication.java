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

    private Integer unitWeight;

    @Lob
    private Byte[] image;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Integer getUnitWeight() {
        return unitWeight;
    }

    public Byte[] getImage() {
        return image;
    }
}
