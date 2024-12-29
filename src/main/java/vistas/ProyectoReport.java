package vistas;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProyectoReport {
    private JTable tablaProyecto;
    private JPanel PanelProyecto;

    private final DefaultTableModel tableModel;

    private final SessionFactory factory;

    public ProyectoReport(SessionFactory factory) {
        this.factory = factory;

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre del Proyecto", "Descripción", "Fecha de fin estimada", "ID Equipo", "Equipo", "ID Cliente", "Cliente"}, 0);
        tablaProyecto.setModel(tableModel);
        cargarProyectos();
    }

    private void cargarProyectos() {
        try (Session session = factory.openSession()) {
            List<modelos.Proyecto> proyectos = session.createQuery("from modelos.Proyecto", modelos.Proyecto.class).list();
            for (modelos.Proyecto proyecto : proyectos) {
                tableModel.addRow(new Object[]{
                        proyecto.getId(),
                        proyecto.getNombre(),
                        proyecto.getDescripcion(),
                        proyecto.getFechaFin(),
                        proyecto.getEquipo().getId(),
                        proyecto.getEquipo().getNombre(),
                        proyecto.getCliente().getId(),
                        proyecto.getCliente().getNombreInstitucion(),
                });
            }
        }
    }
    public JPanel getPanel() {
        return PanelProyecto;
    }

}