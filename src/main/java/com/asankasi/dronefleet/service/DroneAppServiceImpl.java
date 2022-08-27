package com.asankasi.dronefleet.service;

import com.asankasi.dronefleet.model.*;
import com.asankasi.dronefleet.repository.DroneInfoRepository;
import com.asankasi.dronefleet.repository.DroneItemLineRepository;
import com.asankasi.dronefleet.repository.DroneRepository;
import com.asankasi.dronefleet.response.CustomApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.asankasi.dronefleet.util.Constants.*;

@Service
public class DroneAppServiceImpl implements DroneAppService {
    private DroneRepository droneRepository;
    private DroneInfoRepository infoRepository;
    private DroneItemLineRepository itemLineRepository;

    private MedicationService medicationService;

    @Override
    public CustomApiResponse registerDrone(Drone drone) {
        var res = new CustomApiResponse();
        if(drone.getMaxWeight() > 500) {
            res.setStatus(HttpStatus.BAD_REQUEST);
            res.addError("Drone weight should not exceed 500");
            return res;
        }
        drone.getInfo().setDrone(drone);
        var d = droneRepository.save(drone);
        res.addAttribute(GENERAL_MESSAGE_KEY, "Drone is registered with id " + d.getDroneID());
        return res;
    }

    @Override
    public List<DroneMedicationItemLine> getLoadedMedicalItems(Long droneID) {
        Drone drone = droneRepository.findById(droneID).orElse(null);
        if (drone == null) {
            return Collections.emptyList();
        }

        //If same medication item found in multiple packed lines quantities are aggregated to form a single entry
        var medicalItemsMap = drone.getItemLines()
                .stream().collect(
                        Collectors.groupingBy(DroneMedicationItemLine::getMedicationID,
                                Collectors.mapping(Function.identity(), Collectors.reducing((t1, t2) -> {
                                    t2.setQuantity(t1.getQuantity() + t2.getQuantity());
                                    return t2;
                                }))));
        return medicalItemsMap.values().stream().map(Optional::get).toList();
    }

    @Override
    public CustomApiResponse checkDroneBatteryStatus(Long droneID) {
        var res = new CustomApiResponse();
        var info = droneRepository.findById(droneID).map(Drone::getInfo).orElse(null);
        if(info == null) {
            res.setStatus(HttpStatus.BAD_REQUEST);
            res.addError("No drone stats info found for id: " + droneID);
            return res;
        }
        res.addAttribute("batteryCapacity", info.getBatteryCapacity() + "%");
        return res;
    }

    @Override
    public List<Drone> getAvailableDrones() {
        return droneRepository.getAvailableDrones(DRONE_BATTERY_THRESHOLD, Arrays.asList(State.IDLE, State.LOADING));
    }

    private Drone findAvailableDrone(Long droneID) {
        return getAvailableDrones()
                .stream().filter(d -> droneID.equals(d.getDroneID()))
                .findFirst().orElse(null);
    }

    @Override
    @Transactional
    public ResponseEntity<?> loadMedicationItem(Long droneID, DroneMedicationItemLine itemLine) {
        var res = new CustomApiResponse();

        itemLine.setDroneID(droneID);
        var drone = findAvailableDrone(itemLine.getDroneID());
        if (drone == null) {
            res.addError("Drone " + itemLine.getDroneID() + " is not available");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }

        var med = medicationService.findMedication(itemLine.getMedicationID());
        if (med == null) {
            res.addError("Medication " + itemLine.getMedicationID() + " is not available");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }

        Integer itemLineWeight = itemLine.getQuantity() * med.getUnitWeight();
        if (drone.getRemainingWeight() < itemLineWeight) {
            res.addError("Drone " + drone.getDroneID() + " insufficient loading space");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }

        return packMedicationItem(drone, itemLine, itemLineWeight, res);
    }

    @Override
    public ResponseEntity<?> loadMedicationItemOnAvailableDrone(DroneMedicationItemLine itemLine) {
        var res = new CustomApiResponse();

        var med = medicationService.findMedication(itemLine.getMedicationID());
        if (med == null) {
            res.addError("Medication " + itemLine.getMedicationID() + " is not available");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }

        Integer itemLineWeight = itemLine.getQuantity() * med.getUnitWeight();

        var pickedDrone = getAvailableDrones().stream()
                .filter(d -> d.getRemainingWeight() >= itemLineWeight)
                .findAny().orElse(null);
        if (pickedDrone == null) {
            res.addError("No suitable drones are available right now");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
        itemLine.setDroneID(pickedDrone.getDroneID());
        res.addAttribute(GENERAL_MESSAGE_KEY, "Medication package is picked by Drone " + pickedDrone.getDroneID());

        return packMedicationItem(pickedDrone, itemLine, itemLineWeight, res);
    }

    private ResponseEntity<CustomApiResponse> packMedicationItem(Drone drone, DroneMedicationItemLine item, Integer itemLineWeight, CustomApiResponse res) {

        if(item.getQuantity() == null || item.getQuantity() < 1) {
            var errResp = new CustomApiResponse();
            errResp.addError("Item quantity is invalid");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResp);
        }

        itemLineRepository.save(item);
        Integer minUnitWeight = medicationService.getMinUnitWeight();

        DroneInfo droneInfo = drone.getInfo();
        droneInfo.addToCurrentLoad(itemLineWeight);
        if (drone.getRemainingWeight() < minUnitWeight) { //drone is fully loaded now
            droneInfo.setState(State.LOADED);
        } else {
            droneInfo.setState(State.LOADING);
            res.addAttribute("remainingWeight", drone.getRemainingWeight());
        }
        infoRepository.save(droneInfo);

        res.addAttribute("droneID", drone.getDroneID())
                .addAttribute("model", drone.getModel())
                .addAttribute("maxWeight", drone.getMaxWeight())
                .addAttribute("state", droneInfo.getState())
                .addAttribute("currentLoad", droneInfo.getCurrentLoad());
        return ResponseEntity.ok(res);
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

    @Autowired
    public void setMedicationService(MedicationService medicationService) {
        this.medicationService = medicationService;
    }
}
