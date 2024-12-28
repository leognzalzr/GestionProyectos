package vistas;

import modelos.Desarrollador;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class DesarrolladorReport {
    private JPanel panelReporteDesarrollador;
    private JTable tablaReportDesarrollador;

    private final DefaultTableModel tableModel;
    private final SessionFactory factory;

    public DesarrolladorReport(SessionFactory factory) {
        this.factory = factory;
        if (this.factory == null) {
            throw new IllegalArgumentException("SessionFactory cannot be null");
        }


        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Experiencia", "Especialidad", "Equipo"}, 0);
        tablaReportDesarrollador.setModel(tableModel);

        cargarDesarrolladores();


    }

    private void cargarDesarrolladores() {
        try (Session session = factory.openSession()) {
            List<Desarrollador> desarrolladores = session.createQuery("from modelos.Desarrollador", Desarrollador.class).list();
            tableModel.setRowCount(0);
            for (Desarrollador desarrollador : desarrolladores) {
                tableModel.addRow(new Object[]{
                        desarrollador.getId(),
                        desarrollador.getNombre(),
                        desarrollador.getExperiencia(),
                        desarrollador.getEspecialidad(),
                        desarrollador.getEquipo().getNombre(),
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar desarrolladores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JPanel getPanel() {
        return panelReporteDesarrollador;
    }
}