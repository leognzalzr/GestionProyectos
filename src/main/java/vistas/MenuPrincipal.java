package vistas;

import org.hibernate.SessionFactory;

import javax.swing.*;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

public class MenuPrincipal {
    private JButton btnABM;
    private JButton btnReportes;
    private JPanel PanelMainMenu;

    private final Map<String, JFrame> openFrames = new HashMap<>();

    public MenuPrincipal(SessionFactory factory) {
        Dimension buttonSize = new Dimension(250, 50);
        btnABM.setPreferredSize(buttonSize);
        btnReportes.setPreferredSize(buttonSize);

        btnABM.addActionListener(_ -> createAndShowFrame("Menú de Alta, Baja y Modificación (ABM)", new MenuABM(factory).getPanel()));

        btnReportes.addActionListener(_ -> createAndShowFrame("Menú de Reportes", new MenuReport(factory).getPanel()));
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
        return PanelMainMenu;
    }
}