package vistas;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Equipo {
    private JTextField txtNombre;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton btnEliminar;
    private JPanel PanelEquipo;
    private JTable tablaEquipo;

    private final DefaultTableModel tableModel;
    private Integer selectedTeamId;

    private final SessionFactory factory;

    public Equipo(SessionFactory factory) {
        this.factory = factory;

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre del Equipo"}, 0);
        tablaEquipo.setModel(tableModel);
        cargarEquipos();

        tablaEquipo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarEquipo();
            }
        });

        btnGuardar.addActionListener(_ -> guardarEquipo());

        btnCancelar.addActionListener(_ -> limpiarCampos());

        btnEliminar.addActionListener(_ -> eliminarEquipo());
    }

    private void cargarEquipos() {
        try (Session session = factory.openSession()) {
            List<modelos.Equipo> equipos = session.createQuery("from modelos.Equipo", modelos.Equipo.class).list();
            for (modelos.Equipo equipo : equipos) {
                tableModel.addRow(new Object[]{equipo.getId(), equipo.getNombre()});
            }
        }
    }

    private void seleccionarEquipo() {
        int selectedRow = tablaEquipo.getSelectedRow();
        if (selectedRow != -1) {
            selectedTeamId = (Integer) tableModel.getValueAt(selectedRow, 0);
            txtNombre.setText((String) tableModel.getValueAt(selectedRow, 1));
        }
    }

    private void guardarEquipo() {
        String nombreEquipo = txtNombre.getText();

        if (nombreEquipo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
            return;
        }

        try (Session session = factory.openSession()) {
            session.beginTransaction();
            modelos.Equipo equipo;
            if (selectedTeamId == null) {
                equipo = new modelos.Equipo();
                equipo.setNombre(nombreEquipo);
                session.persist(equipo);
                tableModel.addRow(new Object[]{equipo.getId(), equipo.getNombre()});
            } else {
                equipo = session.get(modelos.Equipo.class, selectedTeamId);
                equipo.setNombre(nombreEquipo);
                session.merge(equipo);
                int selectedRow = tablaEquipo.getSelectedRow();
                tableModel.setValueAt(equipo.getNombre(), selectedRow, 1);
                selectedTeamId = null;
            }
            session.getTransaction().commit();
            limpiarCampos();
        }
    }

    private void eliminarEquipo() {
        int selectedRow = tablaEquipo.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un equipo.");
            return;
        }

        int dialogResult = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar este equipo?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.NO_OPTION) {
            return;
        }

        try (Session session = factory.openSession()) {
            session.beginTransaction();
            modelos.Equipo equipo = session.get(modelos.Equipo.class, selectedTeamId);
            if (equipo != null) {
                session.remove(equipo);
                session.getTransaction().commit();
                tableModel.removeRow(selectedRow);
                limpiarCampos();
                JOptionPane.showMessageDialog(null, "Equipo eliminado con éxito.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar el equipo", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void limpiarCampos() {
        txtNombre.setText("");
        selectedTeamId = null;

        tablaEquipo.clearSelection();
    }

    public JPanel getPanel() {
        return PanelEquipo;
    }
}