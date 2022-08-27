package com.asankasi.dronefleet.service;

import com.asankasi.dronefleet.model.Drone;
import com.asankasi.dronefleet.model.DroneInfo;
import com.asankasi.dronefleet.model.DroneMedicationItemLine;
import com.asankasi.dronefleet.response.CustomApiResponse;
import com.asankasi.dronefleet.response.DroneResponse;

import java.util.List;

public interface DroneAppService {

    CustomApiResponse registerDrone(Drone drone);

    List<DroneMedicationItemLine> getLoadedMedicalItems(Long droneID);

    CustomApiResponse loadMedicationItem(Long droneID, DroneMedicationItemLine item);

    CustomApiResponse loadMedicationItemOnAvailableDrone(DroneMedicationItemLine itemLine);

    List<Drone> getAvailableDrones();

    CustomApiResponse checkDroneBatteryStatus(Long droneID);

    List<DroneResponse> findAllRegisteredDrones();

    List<DroneResponse> findAllOperatingDrones();

    void updateDronesInfo(List<DroneInfo> droneInfoList);
}
