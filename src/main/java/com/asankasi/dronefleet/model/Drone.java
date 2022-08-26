package com.asankasi.dronefleet.model;

import javax.persistence.*;

@Entity
public class Drone {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "drone_id")
    private Long droneID;

    @Column(length = 100)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private DroneModel model;

    private Double maxWeight;

    @OneToOne(mappedBy = "drone", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private DroneInfo info = new DroneInfo();

    public Long getDroneID() {
        return droneID;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public DroneModel getModel() {
        return model;
    }

    public Double getMaxWeight() {
        return maxWeight;
    }

    public DroneInfo getInfo() {
        return info;
    }

    public Double getRemainingWeight() {
        return maxWeight - info.getCurrentLoad();
    }
}
