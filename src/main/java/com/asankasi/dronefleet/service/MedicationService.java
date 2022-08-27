package com.asankasi.dronefleet.service;

import com.asankasi.dronefleet.model.Medication;

public interface MedicationService {
    Medication findMedication(Long id);

    Integer getMinUnitWeight();
}
