package com.asankasi.dronefleet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class DroneMedicationItemLine {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @JsonIgnore
    private String itemLineID;

    @Column(name = "drone_id")
    private Long droneID;
    private Long medicationID;
    private Integer quantity;

    @OneToOne
    @JoinColumn(name = "medicationID", referencedColumnName = "ID", insertable = false, updatable = false)
    Medication medication;

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

    public void setDroneID(Long droneID) {
        this.droneID = droneID;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Medication getMedication() {
        return medication;
    }
}
