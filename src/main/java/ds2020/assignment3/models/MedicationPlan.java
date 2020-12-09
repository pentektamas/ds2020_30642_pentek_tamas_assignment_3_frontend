package ds2020.assignment3.models;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class MedicationPlan implements Serializable {

    private UUID id;
    private Integer treatmentPeriod;
    private List<Medication> medications;

    public MedicationPlan() {

    }

    public MedicationPlan(UUID id, Integer treatmentPeriod, List<Medication> medications) {
        this.id = id;
        this.treatmentPeriod = treatmentPeriod;
        this.medications = medications;
    }

    public MedicationPlan(Integer treatmentPeriod, List<Medication> medications) {
        this.treatmentPeriod = treatmentPeriod;
        this.medications = medications;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getTreatmentPeriod() {
        return treatmentPeriod;
    }

    public void setTreatmentPeriod(Integer treatmentPeriod) {
        this.treatmentPeriod = treatmentPeriod;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }
}
