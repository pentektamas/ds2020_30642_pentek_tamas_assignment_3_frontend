package ds2020.assignment3;

import ds2020.assignment3.models.MedicationPlanRMI;
import ds2020.assignment3.views.PillDispenserView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication
public class Ds2020Assignment3FrontendApp {

    private static MedicationPlanRMI medicationPlanRMI;

    public static void setUIFont() {

        UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("Arial", Font.BOLD, 14));
    }

    private static void setLookAndFeel() {

        UIManager.put("control", new Color(48, 45, 45));
        UIManager.put("info", new Color(0, 0, 0));
        UIManager.put("nimbusBase", new Color(18, 30, 49));
        UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
        UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
        UIManager.put("nimbusFocus", new Color(255, 0, 0));
        UIManager.put("nimbusGreen", new Color(176, 179, 50));
        UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
        UIManager.put("nimbusLightBackground", new Color(18, 30, 49));
        UIManager.put("nimbusOrange", new Color(191, 98, 4));
        UIManager.put("nimbusRed", new Color(169, 46, 34));
        UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
        UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
        UIManager.put("text", new Color(255, 255, 255));
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        System.setProperty("java.awt.headless", "false"); //Disables headless
        setLookAndFeel();
        setUIFont();
        medicationPlanRMI = SpringApplication.run(Ds2020Assignment3FrontendApp.class, args).getBean(MedicationPlanRMI.class);
        new PillDispenserView(medicationPlanRMI);
    }
}
