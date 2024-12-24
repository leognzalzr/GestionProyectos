package vistas;

import modelo.Equipo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Desarrollador {
    private JTextField txtNombre;
    private JTextField txtExperiencia;
    private JTextField txtEspecialidad;
    private JComboBox<ItemsComboBox> cbEquipo;
    private JTable tablaDesarrollador;
    private JButton btnCancelar;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JPanel PanelDesarrollador;

    private final DefaultTableModel tableModel;
    private Integer selectedDevelopmentId;

    private final SessionFactory factory;

    public Desarrollador(SessionFactory factory) {
        this.factory = factory;

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Años de Experiencia", "Especialidad", "Nombre Equipo", "ID Equipo"}, 0);
        tablaDesarrollador.setModel(tableModel);
        cargarDesarrollador();
        cargarEquipos();

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
            List<modelo.Desarrollador> desarrolladores = session.createQuery("from modelo.Desarrollador", modelo.Desarrollador.class).list();
            for (modelo.Desarrollador desarrollador : desarrolladores) {
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
            List<Equipo> equipos = session.createQuery("from modelo.Equipo", Equipo.class).list();
            for (Equipo equipo : equipos) {
                cbEquipo.addItem(new ItemsComboBox(equipo.getId(), equipo.getNombre()));
            }
        }
    }

    private void seleccionarDesarrollador() {
        int selectedRow = tablaDesarrollador.getSelectedRow();
        if (selectedRow != -1) {
            selectedDevelopmentId = (Integer) tableModel.getValueAt(selectedRow, 0);
            txtNombre.setText((String) tableModel.getValueAt(selectedRow, 1));
            Integer experiencia = (Integer) tableModel.getValueAt(selectedRow, 2);
            txtExperiencia.setText(experiencia.toString());
            txtEspecialidad.setText((String) tableModel.getValueAt(selectedRow, 3));
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
        String nombre = txtNombre.getText();
        String especialidad = txtEspecialidad.getText();
        int experiencia;
        try {
            experiencia = Integer.parseInt(txtExperiencia.getText());
            if (experiencia < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, ingresá una cantidad de años de experiencia válida.");
            return;
        }
        int equipoId = cbEquipo.getItemAt(cbEquipo.getSelectedIndex()).getId();
        Equipo equipo = null;
        if (equipoId != 0) {
            try (Session session = factory.openSession()) {
                equipo = session.get(Equipo.class, equipoId);
            }
        }

        if (nombre.isEmpty() || especialidad.isEmpty() || equipo == null) {
            JOptionPane.showMessageDialog(null, "Por favor, completá todos los campos.");
            return;
        }

        try (Session session = factory.openSession()) {
            session.beginTransaction();
            modelo.Desarrollador desarrollador;
            if (selectedDevelopmentId == null) {
                desarrollador = new modelo.Desarrollador();
                desarrollador.setNombre(nombre);
                desarrollador.setEspecialidad(especialidad);
                desarrollador.setExperiencia(experiencia);
                desarrollador.setEquipo(equipo);
                session.persist(desarrollador);
                tableModel.addRow(new Object[]{desarrollador.getId(), desarrollador.getNombre(), desarrollador.getExperiencia(), desarrollador.getEspecialidad(), desarrollador.getEquipo().getNombre(), desarrollador.getEquipo().getId()});
            } else {
                desarrollador = session.get(modelo.Desarrollador.class, selectedDevelopmentId);
                desarrollador.setNombre(nombre);
                desarrollador.setEspecialidad(especialidad);
                desarrollador.setExperiencia(experiencia);
                desarrollador.setEquipo(equipo);
                session.merge(desarrollador);
                int selectedRow = tablaDesarrollador.getSelectedRow();
                tableModel.setValueAt(nombre, selectedRow, 1);
                tableModel.setValueAt(especialidad, selectedRow, 2);
                tableModel.setValueAt(experiencia, selectedRow, 3);
                tableModel.setValueAt(equipo.getNombre(), selectedRow, 4);
                tableModel.setValueAt(equipo.getId(), selectedRow, 5);
                selectedDevelopmentId = null;
            }
            session.getTransaction().commit();
            limpiarCampos();
        }
    }

    private void eliminarDesarrollador() {
        int selectedRow = tablaDesarrollador.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccioná un registro.");
            return;
        }

        int dialogResult = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que querés eliminar esta registro?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.NO_OPTION) {
            return;
        }

        try (Session session = factory.openSession()) {
            session.beginTransaction();
            modelo.Desarrollador desarrollador = session.get(modelo.Desarrollador.class, selectedDevelopmentId);
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
        txtEspecialidad.setText("");
        txtExperiencia.setText("");
        cbEquipo.setSelectedIndex(0);
        selectedDevelopmentId = null;

        tablaDesarrollador.clearSelection();
    }

    public JPanel getPanel() {
        return PanelDesarrollador;
    }
}