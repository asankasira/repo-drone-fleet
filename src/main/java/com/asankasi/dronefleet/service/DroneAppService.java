package com.asankasi.dronefleet.service;

import com.asankasi.dronefleet.model.DroneInfo;
import com.asankasi.dronefleet.model.Drone;

public interface DroneAppService {

    void registerDrone(Drone drone);

    DroneInfo getDroneStatsInfo(Long droneID);
}
