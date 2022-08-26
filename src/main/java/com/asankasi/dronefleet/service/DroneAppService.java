package com.asankasi.dronefleet.service;

import com.asankasi.dronefleet.model.Drone;
import com.asankasi.dronefleet.model.DroneMedicationItemLine;
import com.asankasi.dronefleet.model.Medication;

import java.util.List;
import java.util.Map;

public interface DroneAppService {

    int DRONE_BATTERY_THRESHOLD = 25;

    void registerDrone(Drone drone);

    Map<String, String> checkDroneBatteryStatus(Long droneID);

    List<Drone> getAvailableDrones();

    Drone findAvailableDrone(Long droneID);

    String loadMedicationItem(Drone drone, Medication med, DroneMedicationItemLine item);
}
