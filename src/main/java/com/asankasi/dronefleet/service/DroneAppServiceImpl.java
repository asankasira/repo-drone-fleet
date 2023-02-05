package com.asankasi.dronefleet.service;

import com.asankasi.dronefleet.exception.DataNotFoundException;
import com.asankasi.dronefleet.model.Drone;
import com.asankasi.dronefleet.model.DroneInfo;
import com.asankasi.dronefleet.model.DroneMedicationItemLine;
import com.asankasi.dronefleet.model.State;
import com.asankasi.dronefleet.repository.DroneInfoRepository;
import com.asankasi.dronefleet.repository.DroneItemLineRepository;
import com.asankasi.dronefleet.repository.DroneRepository;
import com.asankasi.dronefleet.response.CustomApiResponse;
import com.asankasi.dronefleet.response.DroneResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.asankasi.dronefleet.model.State.*;
import static com.asankasi.dronefleet.util.Constants.DRONE_BATTERY_THRESHOLD;
import static com.asankasi.dronefleet.util.Constants.GENERAL_MESSAGE_KEY;

@Service
public class DroneAppServiceImpl implements DroneAppService {
    private DroneRepository droneRepository;
    private DroneInfoRepository infoRepository;
    private DroneItemLineRepository itemLineRepository;

    private MedicationService medicationService;

    @Override
    public CustomApiResponse registerDrone(Drone drone) {
        var res = new CustomApiResponse();
        drone.getInfo().setDrone(drone);
        var d = droneRepository.save(drone);
        res.addAttribute(GENERAL_MESSAGE_KEY, "Drone is registered with id " + d.getDroneID());
        return res;
    }

    @Override
    public List<DroneMedicationItemLine> getLoadedMedicalItems(Long droneID) {
        Drone drone = droneRepository.findById(droneID).orElse(null);
        if (drone == null) {
            throw new DataNotFoundException("Drone with ID " + droneID + " is not found");
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
        if (info == null) {
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
    public CustomApiResponse loadMedicationItem(Long droneID, DroneMedicationItemLine itemLine) {
        var res = new CustomApiResponse();

        itemLine.setDroneID(droneID);
        var availableDrone = findAvailableDrone(itemLine.getDroneID());
        if (availableDrone == null) {
            var drone = droneRepository.findById(droneID).orElse(null);
            String msg = (drone != null) ? ("Drone " + itemLine.getDroneID() + " is not available for pick up") : "Invalid drone id: " + itemLine.getDroneID();
            res.setStatus(drone == null ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
            res.addError(msg);
            return res;
        }

        var med = medicationService.findMedication(itemLine.getMedicationID());
        if (med == null) {
            res.setStatus(HttpStatus.BAD_REQUEST);
            res.addError("Invalid Medication item: " + itemLine.getMedicationID());
            return res;
        }

        Integer itemLineWeight = itemLine.getQuantity() * med.getUnitWeight();
        if (availableDrone.getRemainingWeight() < itemLineWeight) {
            res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            res.addError("Insufficient loading space on drone " + availableDrone.getDroneID());
            return res;
        }

        return packMedicationItem(availableDrone, itemLine, itemLineWeight, res);
    }

    @Override
    public CustomApiResponse loadMedicationItemOnAvailableDrone(DroneMedicationItemLine itemLine) {
        var res = new CustomApiResponse();

        var med = medicationService.findMedication(itemLine.getMedicationID());
        if (med == null) {
            res.setStatus(HttpStatus.BAD_REQUEST);
            res.addError("Invalid Medication item: " + itemLine.getMedicationID());
            return res;
        }

        Integer itemLineWeight = itemLine.getQuantity() * med.getUnitWeight();

        var availableDrones = getAvailableDrones();
        var pickedDrone = availableDrones.stream()
                .filter(d -> d.getRemainingWeight() >= itemLineWeight)
                .findAny().orElse(null);
        if (pickedDrone == null) {
            res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            String msg = !availableDrones.isEmpty() ? "Load is too much for any available drone" : "Drones are not available for pick up";
            res.addError(msg);
            return res;
        }
        itemLine.setDroneID(pickedDrone.getDroneID());
        res.addAttribute(GENERAL_MESSAGE_KEY, "Medication package is picked by Drone " + pickedDrone.getDroneID());

        return packMedicationItem(pickedDrone, itemLine, itemLineWeight, res);
    }

    private CustomApiResponse packMedicationItem(Drone drone, DroneMedicationItemLine item, Integer itemLineWeight, CustomApiResponse res) {
        if (item.getQuantity() == null || item.getQuantity() < 1) {
            var errRes = new CustomApiResponse(HttpStatus.BAD_REQUEST);
            errRes.addError("Item quantity is invalid");
            return errRes;
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
                .addAttribute("batteryCapacity", droneInfo.getBatteryCapacity() + "%")
                .addAttribute("currentLoad", droneInfo.getCurrentLoad());

        return res;
    }

    @Override
    public List<DroneResponse> findAllRegisteredDrones() {
        Iterable<Drone> iterable = droneRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false).map(DroneResponse::fromDrone).toList();
    }

    @Override
    public List<DroneResponse> findAllOperatingDrones() {
        Iterable<Drone> iterable = droneRepository.findAllOperatingDrones(Arrays.asList(LOADED, DELIVERING, DELIVERED, RETURNING));
        return StreamSupport.stream(iterable.spliterator(), false).map(DroneResponse::fromDrone).toList();
    }

    @Override
    public void updateDronesInfo(List<DroneInfo> droneInfoList) {
        infoRepository.saveAll(droneInfoList);
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
