package com.asankasi.dronefleet.controller;


import com.asankasi.dronefleet.model.Drone;
import com.asankasi.dronefleet.model.DroneMedicationItemLine;
import com.asankasi.dronefleet.response.CustomApiResponse;
import com.asankasi.dronefleet.response.DroneResponse;
import com.asankasi.dronefleet.response.MedicationItemsResponse;
import com.asankasi.dronefleet.service.DroneAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping(path = "/drone-management", produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(path = "/drone-management")
public class DroneAppController {
    private DroneAppService droneAppService;

    @GetMapping( "/drones")
    public ResponseEntity<?> getAllRegisteredDrones() {
        var dronesList = droneAppService.findAllRegisteredDrones();
        return ResponseEntity.ok(dronesList);
    }

    @PostMapping( "/drones")
    public ResponseEntity<?> registerDrone(@RequestBody Drone drone) {
        var customResp = droneAppService.registerDrone(drone);
        return createResponseEntity(customResp);
    }

    @GetMapping("/available-drones")
    public ResponseEntity<List<DroneResponse>> getAvailableDrones() {
        List<DroneResponse> dronesList = droneAppService.getAvailableDrones().stream().map(DroneResponse::fromDrone).toList();
        return ResponseEntity.ok(dronesList);
    }

    @GetMapping("/drones/{id}/medical-items")
    public List<MedicationItemsResponse> getLoadedMedicalItems(@PathVariable Long id) {
        return droneAppService.getLoadedMedicalItems(id).stream().map(MedicationItemsResponse::fromDroneMedicationItem).toList();
    }

    @PutMapping("/drones/{id}/medical-item-line")
    public ResponseEntity<?> loadMedicationItem(@RequestBody DroneMedicationItemLine itemLine, @PathVariable Long id) {
        var customResp = droneAppService.loadMedicationItem(id, itemLine);
        return createResponseEntity(customResp);
    }

    @PutMapping("/medical-item-line")
    public ResponseEntity<?> loadMedicationItemOnAvailableDrone(@RequestBody DroneMedicationItemLine itemLine) {
        var customResp = droneAppService.loadMedicationItemOnAvailableDrone(itemLine);
        return createResponseEntity(customResp);
    }

    @GetMapping("/drone-info/{id}/battery-level")
    public ResponseEntity<?> checkBatteryStatus(@PathVariable Long id) {
        var customResp = droneAppService.checkDroneBatteryStatus(id);
        return createResponseEntity(customResp);
    }

    @Autowired
    public void setDroneAppService(DroneAppService droneAppService) {
        this.droneAppService = droneAppService;
    }

    private ResponseEntity<?> createResponseEntity(CustomApiResponse res) {
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
