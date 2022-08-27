package com.asankasi.dronefleet.service;

import com.asankasi.dronefleet.model.Medication;
import com.asankasi.dronefleet.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicationServiceImpl implements MedicationService {
    private MedicationRepository mediRepository;


    @Override
    public Medication findMedication(Long id) {
        return mediRepository.findById(id).orElse(null);
    }

    @Override
    public Double getMinUnitWeight() {
        return mediRepository.getMinUnitWeight();
    }

    @Autowired
    public void setMediRepository(MedicationRepository mediRepository) {
        this.mediRepository = mediRepository;
    }
}
