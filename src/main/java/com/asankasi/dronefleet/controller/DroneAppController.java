package com.asankasi.dronefleet.controller;


import com.asankasi.dronefleet.model.DroneInfo;
import com.asankasi.dronefleet.model.Drone;
import com.asankasi.dronefleet.model.DroneMedicationItemLine;
import com.asankasi.dronefleet.repository.DroneItemLineRepository;
import com.asankasi.dronefleet.service.DroneAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
//@RequestMapping(path = "/drone-management", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(path = "/drone-management")
public class DroneAppController {
    private DroneAppService droneAppService;
    private DroneItemLineRepository itemLineRepository;

    @PostMapping( "/drones")
    public ResponseEntity<?> registerDrone(@RequestBody Drone drone) {
        if(drone.getMaxWeight() > 500) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Drone weight should not exceed 500"));
        }
        drone.getInfo().setDrone(drone);
        droneAppService.registerDrone(drone);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/medical-item-line")
    public void loadMedicationItem(@RequestBody DroneMedicationItemLine itemLine) {
        itemLineRepository.save(itemLine);
    }

    @GetMapping("/drone-info/{id}")
    public ResponseEntity<?> checkBatteryStatus(@PathVariable Long id) {
        DroneInfo info = droneAppService.getDroneStatsInfo(id);
        if(info == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "No drone stats info found for id: " + id));
        }
        return ResponseEntity.ok(info);
    }

    @Autowired
    public void setDroneAppService(DroneAppService droneAppService) {
        this.droneAppService = droneAppService;
    }

    @Autowired
    public void setItemLineRepository(DroneItemLineRepository itemLineRepository) {
        this.itemLineRepository = itemLineRepository;
    }
}
