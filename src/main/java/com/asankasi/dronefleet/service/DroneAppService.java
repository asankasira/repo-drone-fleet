package com.asankasi.dronefleet.service;

import com.asankasi.dronefleet.model.Drone;
import com.asankasi.dronefleet.model.DroneMedicationItemLine;
import com.asankasi.dronefleet.response.CustomApiResponse;

import java.util.List;

public interface DroneAppService {

    CustomApiResponse registerDrone(Drone drone);

    List<DroneMedicationItemLine> getLoadedMedicalItems(Long droneID);

    CustomApiResponse loadMedicationItem(Long droneID, DroneMedicationItemLine item);

    CustomApiResponse loadMedicationItemOnAvailableDrone(DroneMedicationItemLine itemLine);

    List<Drone> getAvailableDrones();

    CustomApiResponse checkDroneBatteryStatus(Long droneID);
}
