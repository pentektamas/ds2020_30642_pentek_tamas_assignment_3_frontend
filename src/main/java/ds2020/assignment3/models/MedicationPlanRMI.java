package ds2020.assignment3.models;

import com.googlecode.jsonrpc4j.JsonRpcParam;

import java.util.UUID;

public interface MedicationPlanRMI {

    String getMedicationPlanByPatientId(@JsonRpcParam(value = "patientId") UUID patientId);

    void sendTakenMedications(@JsonRpcParam(value = "takenTreatment") String takenTreatment);

    void updateTreatmentPeriod(@JsonRpcParam(value = "patientId") UUID patientId);
}
