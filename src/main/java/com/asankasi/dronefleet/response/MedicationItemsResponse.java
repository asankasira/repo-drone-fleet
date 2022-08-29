package com.asankasi.dronefleet.response;

import com.asankasi.dronefleet.model.DroneMedicationItemLine;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MedicationItemsResponse {
    private Long droneID;
    private Long medicationID;
    private String name;
    private String code;
    private Integer quantity;
    private Integer unitWeight;

    private MedicationItemsResponse() {

    }

    public static MedicationItemsResponse fromDroneMedicationItem(DroneMedicationItemLine itemLine) {
        MedicationItemsResponse res = new MedicationItemsResponse();
        var medication = itemLine.getMedication();
        res.droneID = itemLine.getDroneID();
        res.medicationID = itemLine.getMedicationID();
        res.quantity = itemLine.getQuantity();
        res.name = medication.getName();
        res.code = medication.getCode();
        res.unitWeight = medication.getUnitWeight();
        return res;
    }
}
