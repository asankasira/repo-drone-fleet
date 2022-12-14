package com.asankasi.dronefleet.response;

import com.asankasi.dronefleet.model.Drone;
import com.asankasi.dronefleet.model.DroneModel;
import com.asankasi.dronefleet.model.State;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DroneResponse {
    private Long droneID;
    private String serialNumber;
    private DroneModel model;
    private Integer maxWeight;
    private State state;
    @JsonIgnore
    private Integer batteryLevel;
    private Integer currentLoad;

    private DroneResponse() {

    }

    public static DroneResponse fromDrone(Drone drone) {
        DroneResponse res = new DroneResponse();
        res.droneID = drone.getDroneID();
        res.serialNumber = drone.getSerialNumber();
        res.model = drone.getModel();
        res.maxWeight = drone.getMaxWeight();
        res.state = drone.getInfo().getState();
        res.batteryLevel = drone.getInfo().getBatteryCapacity();
        res.currentLoad = drone.getInfo().getCurrentLoad();
        return res;
    }

    public Long getDroneID() {
        return droneID;
    }

    public String getBatteryCapacity() {
        return batteryLevel + "%";
    }

    public State getState() {
        return state;
    }

    public Integer getCurrentLoad() {
        return currentLoad;
    }
}
