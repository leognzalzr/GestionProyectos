package vistas;

import modelos.Equipo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;

public class Desarrollador {
    private JPanel PanelDesarrollador;
    private JTextField txtNombre;
    private JTextField txtExperiencia;
    private JComboBox<ItemsComboBox> cbEquipo;
    private JTable tablaDesarrollador;
    private JButton btnCancelar;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JComboBox cmbEspecialidad;

    private final DefaultTableModel tableModel;
    private Integer selectedDeveloperId;

    private final SessionFactory factory;

    public Desarrollador(SessionFactory factory) {
        this.factory = factory;

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Años de Experiencia", "Especialidad", "Nombre Equipo", "ID Equipo"}, 0);
        tablaDesarrollador.setModel(tableModel);
        cargarDesarrollador();
        cargarEquipos();
        limpiarCampos();

        tablaDesarrollador.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarDesarrollador();
            }
        });

        btnGuardar.addActionListener(_ -> guardarDesarrollador());

        btnCancelar.addActionListener(_ -> limpiarCampos());

        btnEliminar.addActionListener(_ -> eliminarDesarrollador());
    }

    private void cargarDesarrollador() {
        try (Session session = factory.openSession()) {
            List<modelos.Desarrollador> desarrolladores = session.createQuery("from modelos.Desarrollador", modelos.Desarrollador.class).list();
            for (modelos.Desarrollador desarrollador : desarrolladores) {
                tableModel.addRow(new Object[]{
                        desarrollador.getId(),
                        desarrollador.getNombre(),
                        desarrollador.getExperiencia(),
                        desarrollador.getEspecialidad(),
                        desarrollador.getEquipo().getNombre(),
                        desarrollador.getEquipo().getId(),
                });
            }
        }
    }

    private void cargarEquipos() {
        try (Session session = factory.openSession()) {
            List<Equipo> equipos = session.createQuery("from modelos.Equipo", modelos.Equipo.class).list();
            for (Equipo equipo : equipos) {
                cbEquipo.addItem(new ItemsComboBox(equipo.getId(), equipo.getNombre()));
            }
        }
    }

    private void seleccionarDesarrollador() {
        int selectedRow = tablaDesarrollador.getSelectedRow();
        if (selectedRow != -1) {
            selectedDeveloperId = (Integer) tableModel.getValueAt(selectedRow, 0);
            txtNombre.setText((String) tableModel.getValueAt(selectedRow, 1));
            txtExperiencia.setText(tableModel.getValueAt(selectedRow, 2).toString());
            cmbEspecialidad.setSelectedItem(tableModel.getValueAt(selectedRow, 3));
            int equipoId = (int) tableModel.getValueAt(selectedRow, 5);
            for (int i = 0; i < cbEquipo.getItemCount(); i++) {
                if (cbEquipo.getItemAt(i).getId() == equipoId) {
                    cbEquipo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void guardarDesarrollador() {
        if (txtNombre.getText().isEmpty() ||
                txtExperiencia.getText().isEmpty() ||
                cmbEspecialidad.getSelectedItem() == null ||
                cbEquipo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
            return;
        }

        String nombre = txtNombre.getText();
        String especialidad = Objects.requireNonNull(cmbEspecialidad.getSelectedItem()).toString();
        int experiencia;
        try {
            experiencia = Integer.parseInt(txtExperiencia.getText());
            if (experiencia < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese una cantidad de años de experiencia válida.");
            return;
        }
        
        int equipoId = cbEquipo.getItemAt(cbEquipo.getSelectedIndex()).getId();
        Equipo equipo = null;
        if (equipoId != 0) {
            try (Session session = factory.openSession()) {
                equipo = session.get(Equipo.class, equipoId);
            }
        }

        try (Session session = factory.openSession()) {
            session.beginTransaction();
            modelos.Desarrollador desarrollador;
            if (selectedDeveloperId == null) {
                desarrollador = new modelos.Desarrollador();
                desarrollador.setNombre(nombre);
                desarrollador.setEspecialidad(especialidad);
                desarrollador.setExperiencia(experiencia);
                desarrollador.setEquipo(equipo);
                session.persist(desarrollador);
                tableModel.addRow(new Object[]{desarrollador.getId(), desarrollador.getNombre(),
                        desarrollador.getExperiencia(), desarrollador.getEspecialidad(),
                        desarrollador.getEquipo().getNombre(), desarrollador.getEquipo().getId()});
            } else {
                desarrollador = session.get(modelos.Desarrollador.class, selectedDeveloperId);
                desarrollador.setNombre(nombre);
                desarrollador.setEspecialidad(especialidad);
                desarrollador.setExperiencia(experiencia);
                desarrollador.setEquipo(equipo);
                session.merge(desarrollador);
                int selectedRow = tablaDesarrollador.getSelectedRow();
                tableModel.setValueAt(desarrollador.getNombre(), selectedRow, 1);
                tableModel.setValueAt(desarrollador.getEspecialidad(), selectedRow, 2);
                tableModel.setValueAt(desarrollador.getExperiencia(), selectedRow, 3);
                tableModel.setValueAt(desarrollador.getEquipo().getNombre(), selectedRow, 4);
                tableModel.setValueAt(desarrollador.getEquipo().getId(), selectedRow, 5);
                selectedDeveloperId = null;
            }
            session.getTransaction().commit();
            limpiarCampos();
        }
    }

    private void eliminarDesarrollador() {
        int selectedRow = tablaDesarrollador.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un registro.");
            return;
        }

        int dialogResult = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar esta registro?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.NO_OPTION) {
            return;
        }

        try (Session session = factory.openSession()) {
            session.beginTransaction();
            modelos.Desarrollador desarrollador = session.get(modelos.Desarrollador.class, selectedDeveloperId);
            if (desarrollador != null) {
                session.remove(desarrollador);
                session.getTransaction().commit();
                tableModel.removeRow(selectedRow);
            }
            limpiarCampos();
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        cmbEspecialidad.setSelectedIndex(-1);
        txtExperiencia.setText("");
        cbEquipo.setSelectedIndex(-1);
        selectedDeveloperId = null;

        tablaDesarrollador.clearSelection();
    }

    public JPanel getPanel() {
        return PanelDesarrollador;
    }
}