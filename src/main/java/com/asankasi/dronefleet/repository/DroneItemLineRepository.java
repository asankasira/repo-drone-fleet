package com.asankasi.dronefleet.repository;

import com.asankasi.dronefleet.model.DroneMedicationItemLine;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface DroneItemLineRepository extends CrudRepository<DroneMedicationItemLine, UUID> {

}
