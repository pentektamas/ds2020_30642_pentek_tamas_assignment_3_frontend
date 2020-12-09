package ds2020.assignment3.views;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ds2020.assignment3.models.Medication;
import ds2020.assignment3.models.MedicationPlan;
import ds2020.assignment3.models.MedicationPlanRMI;
import ds2020.assignment3.models.MedicationTreatment;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class PillDispenserView extends JFrame {

    private final JLabel currentTimeLabel;
    private Timer timer;
    private LocalDateTime currentTime;
    private final MedicationPlanRMI medicationPlanRMI;
    private MedicationPlan medicationPlan;
    private List<JButton> takenButtons;
    private List<JPanel> panels;
    private final JPanel tablePanel;
    private final UUID patienID = UUID.fromString("46799127-8fb4-4b40-9b74-a2fe680b347d");
    private int flag;

    public PillDispenserView(MedicationPlanRMI medicationPlanRMI) {
        currentTime = LocalDateTime.now();
        currentTimeLabel = new JLabel();
        currentTimeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        currentTimeLabel.setForeground(Color.RED);

        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new TimerActionListener(), 1000, 1000);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel timePanel = new JPanel();
        timePanel.add(currentTimeLabel);
        timePanel.setSize(new Dimension(100, 100));
        panel.add(timePanel, BorderLayout.PAGE_START);
        tablePanel = new JPanel();
        this.medicationPlanRMI = medicationPlanRMI;
        takenButtons = new ArrayList<>();
        panel.add(tablePanel, BorderLayout.CENTER);
        this.setContentPane(panel);
        this.setVisible(true);
        this.setPreferredSize(new Dimension(400, 300));
        this.setTitle("Pill Dispenser Application");
        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void addTakenButtonListeners() {
        for (JButton button : takenButtons) {
            button.addActionListener(new TakenButtonListener());
        }
    }

    private List<JPanel> createList(List<Medication> medicationList) {
        List<JPanel> panels = new ArrayList<>();
        takenButtons = new ArrayList<>();
        for (Medication medication : medicationList) {
            JPanel panel1 = new JPanel();
            JLabel nameLabel = new JLabel(medication.getName());
            JLabel dosageLabel = new JLabel(medication.getDosage());
            JButton takenButton = new JButton("Taken");
            panel1.add(nameLabel);
            panel1.add(new JLabel("   "));
            panel1.add(dosageLabel);
            panel1.add(new JLabel("   "));
            panel1.add(takenButton);
            panel1.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            panels.add(panel1);
            takenButtons.add(takenButton);
        }
        return panels;
    }

    public MedicationPlan getFullMedicationPlan() {
        MedicationPlan medicationPlan = null;
        String val = medicationPlanRMI.getMedicationPlanByPatientId(patienID);
        try {
            JSONObject planObject = new JSONObject(val);
            if (planObject.getInt("treatmentPeriod") > 0) {
                JSONArray medications = planObject.getJSONArray("medications");
                List<Medication> medicationList = new ArrayList<>();
                for (int i = 0; i < medications.length(); i++) {
                    JSONObject med = (JSONObject) medications.get(i);
                    Medication medication = new Medication(UUID.fromString(med.getString("id")), med.getString("name"), med.getString("sideEffects"), med.getString("dosage"));
                    medicationList.add(medication);
                }
                medicationPlan = new MedicationPlan(UUID.fromString(planObject.getString("id")), planObject.getInt("treatmentPeriod"), medicationList);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Error when converting JSON!");
        }
        return medicationPlan;
    }

    public List<Medication> filterMedicationsByIntakeInterval() {
        List<Medication> filteredMedications = new ArrayList<>();
        if (this.medicationPlan != null) {
            List<Medication> medications = this.medicationPlan.getMedications();
            for (Medication med : medications) {
                String dosage = med.getDosage();
                int pillsPerDay = Integer.parseInt(String.valueOf(dosage.charAt(0)));
                int begin = dosage.indexOf("(");
                int end = dosage.indexOf(")");
                dosage = dosage.substring(begin + 1, end);
                String[] splitDosages = dosage.split(",");
                int flag = 0;
                for (String splitDosage : splitDosages) {
                    int start = Integer.parseInt(splitDosage.split("-")[0]);
                    int stop = Integer.parseInt(splitDosage.split("-")[1]);
                    if ((currentTime.getSecond() >= start && currentTime.getSecond() <= stop) && pillsPerDay > 0) {
                        //if ((currentTime.getHour() >= start && currentTime.getHour() < stop) && pillsPerDay > 0) {
                        flag = 1;
                    }
                }
                if (flag == 1)
                    filteredMedications.add(med);
            }
        }
        return filteredMedications;
    }

    public void updateMedicationList() {
        List<Medication> currentMedicationList = filterMedicationsByIntakeInterval();
        panels = createList(currentMedicationList);
        tablePanel.removeAll();
        addTakenButtonListeners();
        for (JPanel pan : panels) {
            tablePanel.add(pan);
        }
        tablePanel.updateUI();
    }

    public void processUntakenMedication() {
        if (panels != null && panels.size() > 0) {
            for (JPanel panel : panels) {
                JLabel nameLabel = (JLabel) panel.getComponent(0);
                MedicationTreatment medicationTreatment = new MedicationTreatment(patienID, nameLabel.getText(), currentTime.toString(), "NOT TAKEN");
                convertToJSON(medicationTreatment);
            }
        }
    }

    private void convertToJSON(MedicationTreatment medicationTreatment) {
        ObjectMapper mapper = new ObjectMapper();
        String takenTreatment;
        try {
            takenTreatment = mapper.writeValueAsString(medicationTreatment);
            medicationPlanRMI.sendTakenMedications(takenTreatment);
        } catch (JsonProcessingException jsonProcessingException) {
            jsonProcessingException.printStackTrace();
        }
    }

    private void decreasePillsNumber(String medication) {
        for (Medication med : this.medicationPlan.getMedications()) {
            if (med.getName().equals(medication)) {
                String newDosage = med.getDosage();
                int currentDosage = Integer.parseInt(String.valueOf(newDosage.charAt(0))) - 1;
                newDosage = currentDosage + newDosage.substring(1);
                med.setDosage(newDosage);
            }
        }
    }

    class TimerActionListener extends TimerTask {

        @Override
        public void run() {
            currentTime = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            currentTimeLabel.setText(currentTime.format(dtf));
            if (currentTime.getSecond() % 59 == 0) {
                //if (currentTime.getHour() == 5 && currentTime.getMinute() == 59 && currentTime.getSecond() == 59) {
                flag++;
                medicationPlan = getFullMedicationPlan();
                if (medicationPlan == null) {
                    currentTimeLabel.setText(currentTime.format(dtf) + " No medication plan !");
                } else {
                    currentTimeLabel.setText(currentTime.format(dtf) + " Loaded !");
                    if (flag == 1)
                        medicationPlanRMI.updateTreatmentPeriod(patienID);
                }
            }
            if (currentTime.getSecond() % 4 == 0) {
                processUntakenMedication();
                updateMedicationList();
                flag = 0;
            }
        }

    }

    class TakenButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            JPanel deletedPanel = new JPanel();
            for (JPanel panel : panels) {
                if (button.getParent() == panel) {
                    deletedPanel = panel;
                }
            }
            JLabel nameLabel = (JLabel) deletedPanel.getComponent(0);
            decreasePillsNumber(nameLabel.getText());
            MedicationTreatment medicationTreatment = new MedicationTreatment(patienID, nameLabel.getText(), currentTime.toString(), "TAKEN");
            convertToJSON(medicationTreatment);
            panels.remove(deletedPanel);
            tablePanel.remove(deletedPanel);
            tablePanel.updateUI();
        }
    }
}
