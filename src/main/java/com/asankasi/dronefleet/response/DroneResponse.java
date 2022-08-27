package com.asankasi.dronefleet.response;

import com.asankasi.dronefleet.model.Drone;
import com.asankasi.dronefleet.model.DroneModel;
import com.asankasi.dronefleet.model.State;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DroneResponse {
    private Long droneID;
    private String serialNumber;
    private DroneModel model;
    private Integer maxWeight;
    private State state;
    private Integer batteryCapacity;
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
        res.batteryCapacity = drone.getInfo().getBatteryCapacity();
        res.currentLoad = drone.getInfo().getCurrentLoad();
        return res;
    }
}
