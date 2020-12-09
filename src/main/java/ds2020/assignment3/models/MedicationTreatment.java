package ds2020.assignment3.models;

import java.io.Serializable;
import java.util.UUID;

public class MedicationTreatment implements Serializable {

    private UUID patientID;
    private String medicationName;
    private String takenDate;
    private String taken;

    public MedicationTreatment() {
    }

    public MedicationTreatment(UUID patientID, String medicationName, String takenDate, String taken) {
        this.patientID = patientID;
        this.medicationName = medicationName;
        this.takenDate = takenDate;
        this.taken = taken;
    }

    public UUID getPatientID() {
        return patientID;
    }

    public void setPatientID(UUID patientID) {
        this.patientID = patientID;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getTakenDate() {
        return takenDate;
    }

    public void setTakenDate(String takenDate) {
        this.takenDate = takenDate;
    }

    public String getTaken() {
        return taken;
    }

    public void setTaken(String taken) {
        this.taken = taken;
    }
}

