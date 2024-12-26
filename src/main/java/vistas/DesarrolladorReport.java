package vistas;

import modelos.Desarrollador; // Importa la clase Desarrollador
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class DesarrolladorReport {
    private JPanel panelReporteDesarrollador;
    private JTable tablaReportDesarrollador;
    private JLabel lblTitulo;
    private DefaultTableModel tableModel;
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
            List<Desarrollador> desarrolladores = session.createQuery("from modelos.Desarrollador", Desarrollador.class).list(); // Consulta correcta
            tableModel.setRowCount(0);
            for (Desarrollador desarrollador : desarrolladores) {
                tableModel.addRow(new Object[]{
                        desarrollador.getId(), // Asumiendo que Desarrollador hereda 'id' de Persona
                        desarrollador.getNombre(), // Asumiendo que Desarrollador hereda 'nombre' de Persona
                        desarrollador.getExperiencia(),
                        desarrollador.getEspecialidad(),
                        desarrollador.getEquipo() != null ? desarrollador.getEquipo().getNombre() : "Sin equipo" // Manejo de equipos nulos
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar desarrolladores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JPanel getPanel() {
        return panelReporteDesarrollador;
    }

    public void actualizarTabla() {
        cargarDesarrolladores();
    }
}