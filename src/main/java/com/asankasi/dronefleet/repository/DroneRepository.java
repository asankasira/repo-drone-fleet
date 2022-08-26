package com.asankasi.dronefleet.repository;

import com.asankasi.dronefleet.model.Drone;
import com.asankasi.dronefleet.model.State;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DroneRepository extends CrudRepository<Drone, Long> {

    @Query("select d from Drone d inner join d.info n where n.batteryCapacity >= :capacity and n.state in :sList")
    List<Drone> getAvailableDrones(Integer capacity, Collection<State> sList);
}
