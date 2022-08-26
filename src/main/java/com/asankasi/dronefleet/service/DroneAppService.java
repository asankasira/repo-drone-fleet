package com.asankasi.dronefleet.service;

import com.asankasi.dronefleet.model.Drone;

import java.util.Map;

public interface DroneAppService {

    void registerDrone(Drone drone);

    Map<String, String> checkDroneBatteryStatus(Long droneID);
}
