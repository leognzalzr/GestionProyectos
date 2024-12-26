package vistas;

import modelos.Equipo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class EquipoReport {
    private JPanel panelReporteEquipo;
    private JTable tablaReporteEquipo;
    private DefaultTableModel tableModel;
    private final SessionFactory factory;

    public EquipoReport(SessionFactory factory) {
        this.factory = factory;
        if (this.factory == null) {
            throw new IllegalArgumentException("SessionFactory cannot be null");
        }


        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre"}, 0);
        tablaReporteEquipo.setModel(tableModel);

        cargarEquipos();

    }

    private void cargarEquipos() {
        try (Session session = factory.openSession()) {
            List<Equipo> equipos = session.createQuery("from modelos.Equipo", Equipo.class).list();
            tableModel.setRowCount(0);
            for (Equipo equipo : equipos) {
                tableModel.addRow(new Object[]{equipo.getId(), equipo.getNombre()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar equipos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JPanel getPanel() {
        return panelReporteEquipo;
    }
}