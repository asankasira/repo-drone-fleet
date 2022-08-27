package com.asankasi.dronefleet.service;

import com.asankasi.dronefleet.model.Drone;
import com.asankasi.dronefleet.model.DroneMedicationItemLine;
import com.asankasi.dronefleet.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DroneAppService {

    CustomApiResponse registerDrone(Drone drone);

    List<DroneMedicationItemLine> getLoadedMedicalItems(Long droneID);

    ResponseEntity<?> loadMedicationItem(Long droneID, DroneMedicationItemLine item);

    ResponseEntity<?> loadMedicationItemOnAvailableDrone(DroneMedicationItemLine itemLine);

    List<Drone> getAvailableDrones();

    CustomApiResponse checkDroneBatteryStatus(Long droneID);
}
