package com.asankasi.dronefleet.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class DroneMedicationItemLine {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String itemLineID;
    private Long droneID;
    private Long medicationID;
    private Integer quantity;
    private Double lineWeight;

    public String getItemLineID() {
        return itemLineID;
    }

    public Long getDroneID() {
        return droneID;
    }

    public Long getMedicationID() {
        return medicationID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getLineWeight() {
        return lineWeight;
    }
}
