package vistas;

import org.hibernate.SessionFactory;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MenuABM {
    private JButton btnCliente;
    private JButton btnEquipo;
    private JButton btnDesarrollador;
    private JButton btnTarea;
    private JButton btnProyecto;
    private JPanel PanelMenuABM;

    private final Map<String, JFrame> openFrames = new HashMap<>();

    public MenuABM(SessionFactory factory) {
        Dimension buttonSize = new Dimension(250, 50);
        btnCliente.setPreferredSize(buttonSize);
        btnEquipo.setPreferredSize(buttonSize);
        btnDesarrollador.setPreferredSize(buttonSize);
        btnTarea.setPreferredSize(buttonSize);
        btnProyecto.setPreferredSize(buttonSize);

        btnCliente.addActionListener(_ -> createAndShowFrame("ABM de Clientes", new Cliente(factory).getPanel()));

        btnEquipo.addActionListener(_ -> createAndShowFrame("ABM de Equipos", new Equipo(factory).getPanel()));

        btnDesarrollador.addActionListener(_ -> createAndShowFrame("ABM de Desarrolladores", new Desarrollador(factory).getPanel()));

        btnTarea.addActionListener(_ -> createAndShowFrame("ABM de Tareas", new Tarea(factory).getPanel()));

        btnProyecto.addActionListener(_ -> createAndShowFrame("ABM de Veterinarios", new Proyecto(factory).getPanel()));
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
        return PanelMenuABM;
    }
}