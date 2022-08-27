package com.asankasi.dronefleet.controller;


import com.asankasi.dronefleet.model.Drone;
import com.asankasi.dronefleet.model.DroneMedicationItemLine;
import com.asankasi.dronefleet.response.CustomApiResponse;
import com.asankasi.dronefleet.service.DroneAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping(path = "/drone-management", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(path = "/drone-management")
public class DroneAppController {
    private DroneAppService droneAppService;

    @PostMapping( "/drones")
    public ResponseEntity<?> registerDrone(@RequestBody Drone drone) {
        if(drone.getMaxWeight() > 500) {
            var res = new CustomApiResponse();
            res.addError("Drone weight should not exceed 500");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
        drone.getInfo().setDrone(drone);
        droneAppService.registerDrone(drone);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/available-drones")
    public List<Drone> getAvailableDrones() {
        return droneAppService.getAvailableDrones();
    }

    @GetMapping("/drones/{id}/medical-items")
    public List<DroneMedicationItemLine> getLoadedMedicalItems(@PathVariable Long id) {
        return droneAppService.getLoadedMedicalItems(id);
    }

    @PutMapping("/drones/{id}/medical-item-line")
    public ResponseEntity<?> loadMedicationItem(@RequestBody DroneMedicationItemLine itemLine, @PathVariable Long id) {
        return droneAppService.loadMedicationItem(id, itemLine);
    }

    @PutMapping("/medical-item-line")
    public ResponseEntity<?> loadMedicationItemOnAvailableDrone(@RequestBody DroneMedicationItemLine itemLine) {
        return droneAppService.loadMedicationItemOnAvailableDrone(itemLine);
    }

    @GetMapping("/drone-info/{id}/battery-level")
    public ResponseEntity<?> checkBatteryStatus(@PathVariable Long id) {
        var batteryStatus = droneAppService.checkDroneBatteryStatus(id);
        if(batteryStatus == null) {
            var res = new CustomApiResponse();
            res.addError("No drone stats info found for id: " + id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
        return ResponseEntity.ok(batteryStatus);
    }

    @Autowired
    public void setDroneAppService(DroneAppService droneAppService) {
        this.droneAppService = droneAppService;
    }
}
