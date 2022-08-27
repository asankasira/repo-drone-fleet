package com.asankasi.dronefleet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class DroneInfo {
    @Id
    @Column(name = "drone_id")
    private Long droneID;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(50) default 'IDLE'")
    private State state = State.IDLE;

    @Column(columnDefinition = "integer default 100")
    private Integer batteryCapacity = 100;

    @Column(columnDefinition = "integer default 0")
    private Integer currentLoad = 0;

    @OneToOne
    @MapsId
    @JoinColumn(name = "drone_id")
    @JsonIgnore
    private Drone drone;

    public Long getDroneID() {
        return droneID;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Integer getBatteryCapacity() {
        return batteryCapacity;
    }

    public Integer getCurrentLoad() {
        return currentLoad;
    }

    public void addToCurrentLoad(Integer itemLineWeight) {
        currentLoad += itemLineWeight;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }
}
