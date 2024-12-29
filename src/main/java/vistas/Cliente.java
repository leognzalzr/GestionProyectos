package vistas;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Cliente {
    private JPanel PanelCliente;
    private JTextField txtNombreInstu;
    private JTextField txtDireccion;
    private JTextField txtNumTelefono;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton btnEliminar;
    private JTable tablaCliente;

    private final DefaultTableModel tableModel;
    private Integer selectedClienteId;

    private final SessionFactory factory;

    public Cliente(SessionFactory factory) {
        this.factory = factory;
        if (this.factory == null) {
            throw new IllegalArgumentException("SessionFactory cannot be null");
        }


        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre Institución", "Dirección", "Teléfono"}, 0);
        tablaCliente.setModel(tableModel);


        cargarClientes();

        tablaCliente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarCliente();
            }
        });

        btnGuardar.addActionListener(_ -> guardarCliente());
        btnCancelar.addActionListener(_ -> limpiarCampos());
        btnEliminar.addActionListener(_ -> eliminarCliente());
    }

    private void cargarClientes() {
        try (Session session = factory.openSession()) {
            List<modelos.Cliente> clientes = session.createQuery("from modelos.Cliente", modelos.Cliente.class).list();
            tableModel.setRowCount(0);
            for (modelos.Cliente cliente : clientes) {
                tableModel.addRow(new Object[]{
                        cliente.getId(),
                        cliente.getNombreInstitucion(),
                        cliente.getDireccion(),
                        cliente.getTelefono()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar clientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarCliente() {
        int selectedRow = tablaCliente.getSelectedRow();
        if (selectedRow != -1) {
            try {
                Object value = tableModel.getValueAt(selectedRow, 0);
                if (value instanceof String) {
                    selectedClienteId = Integer.parseInt((String) value);
                } else if (value instanceof Integer) {
                    selectedClienteId = (Integer) value;
                }
                txtNombreInstu.setText((String) tableModel.getValueAt(selectedRow, 1));
                txtDireccion.setText((String) tableModel.getValueAt(selectedRow, 2));
                txtNumTelefono.setText((String) tableModel.getValueAt(selectedRow, 3));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El ID seleccionado no es un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ClassCastException e) {
                JOptionPane.showMessageDialog(null, "Error al convertir el valor seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guardarCliente() {
        if (txtNombreInstu.getText().isEmpty() || txtDireccion.getText().isEmpty() || txtNumTelefono.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
            return;
        }

        try (Session session = factory.openSession()) {
            session.beginTransaction();
            modelos.Cliente cliente;
            if (selectedClienteId == null) {
                cliente = new modelos.Cliente(txtNombreInstu.getText(), txtDireccion.getText(), txtNumTelefono.getText());
                session.persist(cliente);
                tableModel.addRow(new Object[]{
                        cliente.getId(),
                        cliente.getNombreInstitucion(),
                        cliente.getDireccion(),
                        cliente.getTelefono()
                });
            } else {
                cliente = session.get(modelos.Cliente.class, selectedClienteId);
                if (cliente != null) {
                    cliente.setNombreInstitucion(txtNombreInstu.getText());
                    cliente.setDireccion(txtDireccion.getText());
                    cliente.setTelefono(txtNumTelefono.getText());
                    session.merge(cliente);
                    int selectedRow = tablaCliente.getSelectedRow();
                    tableModel.setValueAt(cliente.getNombreInstitucion(), selectedRow, 1);
                    tableModel.setValueAt(cliente.getDireccion(), selectedRow, 2);
                    tableModel.setValueAt(cliente.getTelefono(), selectedRow, 3);
                    selectedClienteId = null;
                }
            }
            session.getTransaction().commit();
            limpiarCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCliente() {
        int selectedRow = tablaCliente.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un cliente.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar este cliente?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Session session = factory.openSession()) {
                session.beginTransaction();
                modelos.Cliente cliente = session.get(modelos.Cliente.class, selectedClienteId);
                if (cliente != null) {
                    session.remove(cliente);
                    session.getTransaction().commit();
                    tableModel.removeRow(selectedRow);
                    selectedClienteId = null;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        txtNombreInstu.setText("");
        txtDireccion.setText("");
        txtNumTelefono.setText("");
        selectedClienteId = null;
        tablaCliente.clearSelection();
    }

    public JPanel getPanel() {
        return PanelCliente;
    }
}
