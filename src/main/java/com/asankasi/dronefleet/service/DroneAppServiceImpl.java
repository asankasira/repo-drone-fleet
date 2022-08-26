package com.asankasi.dronefleet.service;

import com.asankasi.dronefleet.model.*;
import com.asankasi.dronefleet.repository.DroneInfoRepository;
import com.asankasi.dronefleet.repository.DroneItemLineRepository;
import com.asankasi.dronefleet.repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class DroneAppServiceImpl implements DroneAppService {
    private DroneRepository droneRepository;
    private DroneInfoRepository infoRepository;
    private DroneItemLineRepository itemLineRepository;

    @Override
    public void registerDrone(Drone drone) {
        droneRepository.save(drone);
    }

    @Override
    public Map<String, String> checkDroneBatteryStatus(Long droneID) {
       return droneRepository.findById(droneID).map(Drone::getInfo)
               .map(in -> Map.of("batteryCapacity", in.getBatteryCapacity()+"%"))
               .orElse(null);
    }

    @Override
    public List<Drone> getAvailableDrones() {
        return droneRepository.getAvailableDrones(DRONE_BATTERY_THRESHOLD, Arrays.asList(State.IDLE, State.LOADING));
    }

    @Override
    public Drone findAvailableDrone(Long droneID) {
        return droneRepository.getAvailableDrones(DRONE_BATTERY_THRESHOLD, Arrays.asList(State.IDLE, State.LOADING))
                .stream().filter(d -> droneID.equals(d.getDroneID()))
                .findFirst().orElse(null);
    }

    @Override
    @Transactional
    public String loadMedicationItem(Drone drone, Medication med, DroneMedicationItemLine item) {
       Double itemLineWeight = item.getQuantity() * med.getUnitWeight();
       if(drone.getRemainingWeight() < itemLineWeight) {
           return "Insufficient loading space";
       }

       itemLineRepository.save(item);

       DroneInfo droneInfo = drone.getInfo();
       droneInfo.setCurrentLoad(itemLineWeight);
       droneInfo.setState(State.LOADING);
       infoRepository.save(droneInfo);
       return null;
    }

    @Autowired
    public void setDroneRepository(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    @Autowired
    public void setInfoRepository(DroneInfoRepository infoRepository) {
        this.infoRepository = infoRepository;
    }

    @Autowired
    public void setItemLineRepository(DroneItemLineRepository itemLineRepository) {
        this.itemLineRepository = itemLineRepository;
    }
}
