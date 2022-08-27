package com.asankasi.dronefleet.schedule;

import com.asankasi.dronefleet.model.DroneInfo;
import com.asankasi.dronefleet.model.State;
import com.asankasi.dronefleet.service.DroneAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class PeriodicSchedulerTask {
    private static final Logger logger = LoggerFactory.getLogger(PeriodicSchedulerTask.class);

    private DroneAppService droneAppService;

    @Async
    @Scheduled(initialDelay = 2000, fixedDelay = 15000)
    public void simulateDroneOperating() {
        List<DroneInfo> droneInfoList = new ArrayList<>();
        Random random = new Random();

        //simulation starts when a drone state is moved to LOADED. Battery capacity and state is
        //randomized for api calls and event logs
        var operatingDrones = droneAppService.findAllOperatingDrones();
        operatingDrones.forEach(dr -> {
            int state = random.nextInt(2, 6);
            int battery = random.nextInt(3, 100);
            var info = new DroneInfo();
            info.setDroneID(dr.getDroneID());
            info.setState(State.values()[state]);
            info.setBatteryCapacity(battery);
            info.addToCurrentLoad(dr.getCurrentLoad());
            droneInfoList.add(info);
        });

        droneAppService.updateDronesInfo(droneInfoList);

        var drones = droneAppService.findAllRegisteredDrones();
        drones.forEach(d -> logger.info("Drone {} is on {}% battery level with status {}", d.getDroneID(), d.getBatteryCapacity(), d.getState()));
        logger.info("---------------------------------------------------------------");
    }

    @Autowired
    public void setDroneAppService(DroneAppService droneAppService) {
        this.droneAppService = droneAppService;
    }
}
