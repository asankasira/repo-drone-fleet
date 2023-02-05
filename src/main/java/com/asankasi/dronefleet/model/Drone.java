package com.asankasi.dronefleet.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Drone {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "drone_id")
    private Long droneID;

    @Column(length = 100)
    @NotBlank(message = "Serial number cannot be empty")
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private DroneModel model;

    @Max(value = 500, message = "Drone weight cannot exceeds 500")
    private Integer maxWeight;

    @OneToOne(mappedBy = "drone", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private DroneInfo info = new DroneInfo();

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "drone_id")
    private List<DroneMedicationItemLine> itemLines = new ArrayList<>();

    public Long getDroneID() {
        return droneID;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public DroneModel getModel() {
        return model;
    }

    public Integer getMaxWeight() {
        return maxWeight;
    }

    public DroneInfo getInfo() {
        return info;
    }

    public Integer getRemainingWeight() {
        return maxWeight - info.getCurrentLoad();
    }

    public List<DroneMedicationItemLine> getItemLines() {
        return itemLines;
    }
}
