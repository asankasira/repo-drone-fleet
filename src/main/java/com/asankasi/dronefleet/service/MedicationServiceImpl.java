package com.asankasi.dronefleet.service;

import com.asankasi.dronefleet.model.Medication;
import com.asankasi.dronefleet.repository.MedicationRepository;
import com.asankasi.dronefleet.response.CustomApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.asankasi.dronefleet.util.Constants.GENERAL_MESSAGE_KEY;

@Service
public class MedicationServiceImpl implements MedicationService {
    private MedicationRepository mediRepository;


    @Override
    public Medication findMedication(Long id) {
        return mediRepository.findById(id).orElse(null);
    }

    @Override
    public Integer getMinUnitWeight() {
        return mediRepository.getMinUnitWeight();
    }

    @Transactional
    @Override
    public CustomApiResponse uploadMedicationImage(Long medicationID, byte[] imageData) {
        var resp = new CustomApiResponse();
        var medication = mediRepository.findById(medicationID).orElse(null);
        if (medication == null) {
            resp.setStatus(HttpStatus.BAD_REQUEST);
            resp.addError("Invalid Medication item: " + medicationID);
            return resp;
        }

        if(imageData == null || imageData.length == 0) {
            resp.setStatus(HttpStatus.BAD_REQUEST);
            resp.addError("Invalid image file");
            return resp;
        }

        medication.setImage(imageData);
        mediRepository.save(medication);
        resp.addAttribute(GENERAL_MESSAGE_KEY, "Image is uploaded for medication: " + medicationID);
        return resp;
    }

    @Autowired
    public void setMediRepository(MedicationRepository mediRepository) {
        this.mediRepository = mediRepository;
    }
}
