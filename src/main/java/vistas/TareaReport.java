package vistas;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TareaReport {
    private final DefaultTableModel tableModel;
    private JPanel PanelTareas;
    private JTable tablaTareas;
    private final SessionFactory factory;

    public TareaReport(SessionFactory factory) {
        this.factory = factory;
        if (this.factory == null) {
            throw new IllegalArgumentException("SessionFactory cannot be null");
        }


        tableModel = new DefaultTableModel(new Object[]{"ID", "Descripción", "Estado", "Desarrollador"}, 0);
        tablaTareas.setModel(tableModel);

        cargarTareas();

    }

    private void cargarTareas() {
        try (Session session = factory.openSession()) {
            List<modelos.Tarea> tareas = session.createQuery("from modelos.Tarea", modelos.Tarea.class).list();
            tableModel.setRowCount(0);
            for (modelos.Tarea tarea : tareas) {
                tableModel.addRow(new Object[]{
                        tarea.getId(),
                        tarea.getDescripcion(),
                        tarea.getEstado(),
                        tarea.getDesarrollador().getId()  // Changed to show ID instead of toString()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar tareas: " + e.getMessage());
        }
    }

    public JPanel getPanel() {
        return PanelTareas;
    }
}