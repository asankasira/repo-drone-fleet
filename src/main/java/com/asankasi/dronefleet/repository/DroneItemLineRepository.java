package com.asankasi.dronefleet.repository;

import com.asankasi.dronefleet.model.DroneMedicationItemLine;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneItemLineRepository extends CrudRepository<DroneMedicationItemLine, Long> {

}
