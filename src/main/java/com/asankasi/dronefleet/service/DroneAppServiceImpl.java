package com.asankasi.dronefleet.service;

import com.asankasi.dronefleet.model.DroneInfo;
import com.asankasi.dronefleet.model.Drone;
import com.asankasi.dronefleet.repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DroneAppServiceImpl implements DroneAppService {
    private DroneRepository droneRepository;


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

    @Autowired
    public void setDroneRepository(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }
}
