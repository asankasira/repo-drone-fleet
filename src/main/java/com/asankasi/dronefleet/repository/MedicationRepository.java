package com.asankasi.dronefleet.repository;

import com.asankasi.dronefleet.model.Medication;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MedicationRepository extends CrudRepository<Medication, Long> {

    @Query(value = "select min(unitWeight) from Medication")
    Integer getMinUnitWeight();
}
