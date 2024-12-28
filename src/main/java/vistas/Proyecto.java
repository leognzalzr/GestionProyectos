package vistas;

import modelos.Cliente;
import modelos.Equipo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import raven.datetime.component.date.DatePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class Proyecto {
    private JPanel PanelProyecto;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JComboBox<ItemsComboBox> cbEquipo;
    private JComboBox<ItemsComboBox> cbCliente;
    private JFormattedTextField campoFecha;
    private JTable tablaProyecto;
    private JButton btnCancelar;
    private JButton btnGuardar;
    private JButton btnEliminar;

    private final DefaultTableModel tableModel;
    private Integer selectedProjectId;

    private final SessionFactory factory;
    private final DatePicker datePicker = new DatePicker();

    public Proyecto(SessionFactory factory) {
        this.factory = factory;

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre del Proyecto", "Descripción", "Fecha de fin estimada", "ID Equipo", "Equipo", "ID Cliente", "Cliente"}, 0);
        tablaProyecto.setModel(tableModel);
        cargarProyectos();
        cargarEquipos();
        cargarClientes();
        limpiarCampos();

        tablaProyecto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarProyecto();
            }
        });

        btnGuardar.addActionListener(_ -> guardarProyecto());

        btnCancelar.addActionListener(_ -> limpiarCampos());

        btnEliminar.addActionListener(_ -> eliminarProyecto());

        datePicker.setEditor(campoFecha);
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

    private void cargarEquipos() {
        try (Session session = factory.openSession()) {
            List<Equipo> equipos = session.createQuery("from modelos.Equipo", modelos.Equipo.class).list();
            for (Equipo equipo : equipos) {
                cbEquipo.addItem(new ItemsComboBox(equipo.getId(), equipo.getNombre()));
            }
        }
    }

    private void cargarClientes() {
        try (Session session = factory.openSession()) {
            List<modelos.Cliente> clientes = session.createQuery("from modelos.Cliente", modelos.Cliente.class).list();
            for (modelos.Cliente cliente : clientes) {
                cbCliente.addItem(new ItemsComboBox(cliente.getId(), cliente.getNombreInstitucion()));
            }
        }
    }

    private void seleccionarProyecto() {
        int selectedRow = tablaProyecto.getSelectedRow();
        if (selectedRow != -1) {
            selectedProjectId = (Integer) tableModel.getValueAt(selectedRow, 0);
            String nombre = (String) tableModel.getValueAt(selectedRow, 1);
            txtNombre.setText(nombre);
            String descripcion = (String) tableModel.getValueAt(selectedRow, 2);
            txtDescripcion.setText(descripcion);

            Object fecha = tableModel.getValueAt(selectedRow, 3);
            if (fecha instanceof java.sql.Timestamp timestamp) {
                datePicker.setSelectedDate(timestamp.toLocalDateTime().toLocalDate());
            } else if (fecha instanceof Date sqlDate) {
                datePicker.setSelectedDate(sqlDate.toLocalDate());
            }

            int equipoId = Integer.parseInt(tableModel.getValueAt(selectedRow, 4).toString());
            int clienteId = Integer.parseInt(tableModel.getValueAt(selectedRow, 6).toString());
            for (int i = 0; i < cbCliente.getItemCount(); i++) {
                if (cbCliente.getItemAt(i).getId() == equipoId) {
                    cbCliente.setSelectedIndex(i);
                    break;
                }
            }
            for (int i = 0; i < cbEquipo.getItemCount(); i++) {
                if (cbEquipo.getItemAt(i).getId() == clienteId) {
                    cbEquipo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void guardarProyecto() {
        if (txtNombre == null ||
                txtDescripcion == null ||
                cbEquipo.getSelectedItem() == null ||
                cbCliente.getSelectedItem() == null ||
                datePicker.getSelectedDate() == null) {
            JOptionPane.showMessageDialog(null, "Por favor, completá todos los campos.");
            return;
        }

        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();
        Date fecha = Date.valueOf(datePicker.getSelectedDate());
        int equipoId = cbEquipo.getItemAt(cbEquipo.getSelectedIndex()).getId();
        Equipo equipo = null;
        if (equipoId != 0) {
            try (Session session = factory.openSession()) {
                equipo = session.get(Equipo.class, equipoId);
            }
        }

        int clienteId = cbCliente.getItemAt(cbCliente.getSelectedIndex()).getId();
        Cliente cliente = null;
        if (clienteId != 0) {
            try (Session session = factory.openSession()) {
                cliente = session.get(modelos.Cliente.class, clienteId);
            }
        }

        try (Session session = factory.openSession()) {
            session.beginTransaction();
            modelos.Proyecto proyecto;
            if (selectedProjectId == null) {
                proyecto = new modelos.Proyecto();
                proyecto.setNombre(nombre);
                proyecto.setDescripcion(descripcion);
                proyecto.setFechaFin(fecha);
                proyecto.setEquipo(equipo);
                proyecto.setCliente(cliente);
                session.persist(proyecto);
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
            } else {
                proyecto = session.get(modelos.Proyecto.class, selectedProjectId);
                proyecto.setNombre(nombre);
                proyecto.setDescripcion(descripcion);
                proyecto.setFechaFin(fecha);
                proyecto.setEquipo(equipo);
                proyecto.setCliente(cliente);
                session.merge(proyecto);
                int selectedRow = tablaProyecto.getSelectedRow();
                tableModel.setValueAt(proyecto.getNombre(), selectedRow, 1);
                tableModel.setValueAt(proyecto.getDescripcion(), selectedRow, 2);
                tableModel.setValueAt(fecha, selectedRow, 3);
                tableModel.setValueAt(proyecto.getEquipo().getNombre(), selectedRow, 4);
                tableModel.setValueAt(proyecto.getEquipo().getId(), selectedRow, 5);
                tableModel.setValueAt(proyecto.getCliente().getNombreInstitucion(), selectedRow, 6);
                tableModel.setValueAt(proyecto.getCliente().getId(), selectedRow, 7);
                selectedProjectId = null;
            }
            session.getTransaction().commit();
            limpiarCampos();
        }
    }

    private void eliminarProyecto() {
        int selectedRow = tablaProyecto.getSelectedRow();
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
            modelos.Proyecto proyecto = session.get(modelos.Proyecto.class, selectedProjectId);
            if (proyecto != null) {
                session.remove(proyecto);
                session.getTransaction().commit();
                tableModel.removeRow(selectedRow);
            }
            limpiarCampos();
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        datePicker.setSelectedDate(LocalDate.now());
        cbEquipo.setSelectedIndex(-1);
        cbCliente.setSelectedIndex(-1);
        selectedProjectId = null;

        tablaProyecto.clearSelection();
    }

    public JPanel getPanel() {
        return PanelProyecto;
    }
}