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

    public Integer getBatteryCapacity() {
        return batteryCapacity;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }
}
