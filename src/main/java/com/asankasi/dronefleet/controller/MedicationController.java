package com.asankasi.dronefleet.controller;

import com.asankasi.dronefleet.service.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/medication")
public class MedicationController {

    private MedicationService service;

    @PatchMapping("/upload-file")
    public ResponseEntity<?> uploadMedicationImage(@RequestParam Long medicationId, @RequestParam MultipartFile imageFile) throws IOException {
        var customResp = service.uploadMedicationImage(medicationId, imageFile.getBytes());
        return ResponseEntity.status(customResp.getStatus()).body(customResp);
    }

    @Autowired
    public void setService(MedicationService service) {
        this.service = service;
    }
}
