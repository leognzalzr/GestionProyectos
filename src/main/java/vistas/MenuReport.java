package vistas;

import org.hibernate.SessionFactory;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MenuReport {
    private JPanel PanelMenuReport;
    private JButton TAREASButton;
    private JButton PROYECTOSButton;
    private JButton EQUIPOSButton;
    private JButton DESARROLLADORESButton;
    private JButton CLIENTESButton;

    private final Map<String, JFrame> openFrames = new HashMap<>();

    public MenuReport(SessionFactory factory) {


        Dimension buttonSize = new Dimension(250, 50);
        TAREASButton.setPreferredSize(buttonSize);
        PROYECTOSButton.setPreferredSize(buttonSize);
        EQUIPOSButton.setPreferredSize(buttonSize);
        DESARROLLADORESButton.setPreferredSize(buttonSize);
        CLIENTESButton.setPreferredSize(buttonSize);


        CLIENTESButton.addActionListener(_ -> createAndShowFrame("Reporte de Clientes", new ClienteReport(factory).getPanel()));
        EQUIPOSButton.addActionListener(_ -> createAndShowFrame("Reporte de Equipos", new EquipoReport(factory).getPanel()));

        DESARROLLADORESButton.addActionListener(_ -> createAndShowFrame("Reporte de Desarrolladores", new DesarrolladorReport(factory).getPanel()));
        TAREASButton.addActionListener(_ -> createAndShowFrame("Reporte de Tareas", new TareaReport(factory).getPanel()));

        PROYECTOSButton.addActionListener(_ -> createAndShowFrame("Reporte de Proyectos", new ProyectoReport(factory).getPanel()));
    }

    private void createAndShowFrame(String title, JPanel panel) {
        JFrame frame = openFrames.get(title);
        if (frame == null) {
            frame = new JFrame(title);
            frame.setContentPane(panel);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            openFrames.put(title, frame);
        }
        frame.setVisible(true);
        frame.toFront();
    }
    public JPanel getPanel() {
        return PanelMenuReport;
    }
}
