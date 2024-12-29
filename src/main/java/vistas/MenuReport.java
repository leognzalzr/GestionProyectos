package vistas;

import org.hibernate.SessionFactory;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MenuReport {
    private JPanel PanelMenuReport;
    private JButton btnTarea;
    private JButton btnProyecto;
    private JButton btnEquipo;
    private JButton btnDesarrollador;
    private JButton btnCliente;

    private final Map<String, JFrame> openFrames = new HashMap<>();

    public MenuReport(SessionFactory factory) {
        Dimension buttonSize = new Dimension(250, 50);
        btnCliente.setPreferredSize(buttonSize);
        btnEquipo.setPreferredSize(buttonSize);
        btnDesarrollador.setPreferredSize(buttonSize);
        btnTarea.setPreferredSize(buttonSize);
        btnProyecto.setPreferredSize(buttonSize);

        btnCliente.addActionListener(_ -> createAndShowFrame("Reporte de Clientes", new ClienteReport(factory).getPanel()));
        btnEquipo.addActionListener(_ -> createAndShowFrame("Reporte de Equipos", new EquipoReport(factory).getPanel()));
        btnDesarrollador.addActionListener(_ -> createAndShowFrame("Reporte de Desarrolladores", new DesarrolladorReport(factory).getPanel()));
        btnTarea.addActionListener(_ -> createAndShowFrame("Reporte de Tareas", new TareaReport(factory).getPanel()));
        btnProyecto.addActionListener(_ -> createAndShowFrame("Reporte de Proyectos", new ProyectoReport(factory).getPanel()));
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
