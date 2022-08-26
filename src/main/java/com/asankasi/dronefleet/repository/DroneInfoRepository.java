package com.asankasi.dronefleet.repository;

import com.asankasi.dronefleet.model.DroneInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneInfoRepository extends CrudRepository<DroneInfo, Long> {
}
