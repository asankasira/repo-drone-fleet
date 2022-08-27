package com.asankasi.dronefleet.service;

import com.asankasi.dronefleet.model.Drone;
import com.asankasi.dronefleet.model.DroneMedicationItemLine;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface DroneAppService {

    void registerDrone(Drone drone);

    List<DroneMedicationItemLine> getLoadedMedicalItems(Long droneID);

    ResponseEntity<?> loadMedicationItem(Long droneID, DroneMedicationItemLine item);

    ResponseEntity<?> loadMedicationItemOnAvailableDrone(DroneMedicationItemLine itemLine);

    List<Drone> getAvailableDrones();

    Map<String, String> checkDroneBatteryStatus(Long droneID);
}
