package com.asankasi.dronefleet.controller;


import com.asankasi.dronefleet.model.Drone;
import com.asankasi.dronefleet.model.DroneMedicationItemLine;
import com.asankasi.dronefleet.model.Medication;
import com.asankasi.dronefleet.repository.DroneItemLineRepository;
import com.asankasi.dronefleet.service.DroneAppService;
import com.asankasi.dronefleet.service.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping(path = "/drone-management", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(path = "/drone-management")
public class DroneAppController {
    private DroneAppService droneAppService;
    private MedicationService medicationService;
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

    @GetMapping("/available-drones")
    public List<Drone> getAvailableDrones() {
        return droneAppService.getAvailableDrones();
    }

    @PutMapping("/medical-item-line")
    public ResponseEntity<?> loadMedicationItem(@RequestBody DroneMedicationItemLine itemLine) {
        var drone = droneAppService.findAvailableDrone(itemLine.getDroneID());
        if(drone == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Drone " + itemLine.getDroneID() + " is not available"));
        }

        var m = medicationService.findMedication(itemLine.getMedicationID());
        if(m == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Medication " + itemLine.getMedicationID() + " is not available"));
        }

        String error = droneAppService.loadMedicationItem(drone, m, itemLine);
        if(error != null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", error));
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/drone-info/{id}/battery-level")
    public ResponseEntity<?> checkBatteryStatus(@PathVariable Long id) {
        var batteryStatus = droneAppService.checkDroneBatteryStatus(id);
        if(batteryStatus == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "No drone stats info found for id: " + id));
        }
        return ResponseEntity.ok(batteryStatus);
    }

    @Autowired
    public void setDroneAppService(DroneAppService droneAppService) {
        this.droneAppService = droneAppService;
    }

    @Autowired
    public void setMedicationService(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @Autowired
    public void setItemLineRepository(DroneItemLineRepository itemLineRepository) {
        this.itemLineRepository = itemLineRepository;
    }
}
