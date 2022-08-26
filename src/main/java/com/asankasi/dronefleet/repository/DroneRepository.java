package com.asankasi.dronefleet.repository;

import com.asankasi.dronefleet.model.Drone;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneRepository extends CrudRepository<Drone, Long> {
}
