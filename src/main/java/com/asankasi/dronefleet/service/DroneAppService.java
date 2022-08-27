package com.asankasi.dronefleet.service;

import com.asankasi.dronefleet.model.Drone;
import com.asankasi.dronefleet.model.DroneMedicationItemLine;
import com.asankasi.dronefleet.model.Medication;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface DroneAppService {

    void registerDrone(Drone drone);

    Map<String, String> checkDroneBatteryStatus(Long droneID);

    List<Drone> getAvailableDrones();
    
    ResponseEntity<?> loadMedicationItem(Long droneID, DroneMedicationItemLine item);

    ResponseEntity<?> loadMedicationItemOnAvailableDrone(DroneMedicationItemLine itemLine);
}
