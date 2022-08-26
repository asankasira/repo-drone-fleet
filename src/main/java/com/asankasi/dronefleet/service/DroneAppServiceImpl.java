package com.asankasi.dronefleet.service;

import com.asankasi.dronefleet.model.DroneInfo;
import com.asankasi.dronefleet.model.Drone;
import com.asankasi.dronefleet.repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DroneAppServiceImpl implements DroneAppService {
    private DroneRepository droneRepository;


    @Override
    public void registerDrone(Drone drone) {
        droneRepository.save(drone);
    }

    @Override
    public DroneInfo getDroneStatsInfo(Long droneID) {
       return droneRepository.findById(droneID).map(Drone::getInfo).orElse(null);
    }

    @Autowired
    public void setDroneRepository(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }
}
