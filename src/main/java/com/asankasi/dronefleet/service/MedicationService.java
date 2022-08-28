package com.asankasi.dronefleet.service;

import com.asankasi.dronefleet.model.Medication;
import com.asankasi.dronefleet.response.CustomApiResponse;

public interface MedicationService {
    Medication findMedication(Long id);

    Integer getMinUnitWeight();

    CustomApiResponse uploadMedicationImage(Long medicationID, byte[] imageData);
}
