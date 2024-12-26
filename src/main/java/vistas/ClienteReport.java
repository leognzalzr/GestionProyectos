package vistas;

import modelos.Cliente;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ClienteReport {
    private JPanel panelReporteCliente;
    private JTable tablaReporteCliente;
    private JLabel lblTitulo;
    private final DefaultTableModel tableModel;
    private final SessionFactory factory;

    public ClienteReport(SessionFactory factory) {
        this.factory = factory;
        if (this.factory == null) {
            throw new IllegalArgumentException("SessionFactory cannot be null");
        }

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre de la Institución", "Dirección", "Teléfono"}, 0);
        tablaReporteCliente.setModel(tableModel);

        cargarClientes();
    }

    private void cargarClientes() {
        try (Session session = factory.openSession()) {
            List<Cliente> clientes = session.createQuery("from modelos.Cliente", Cliente.class).list();
            tableModel.setRowCount(0);
            for (Cliente cliente : clientes) {
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

    public JPanel getPanel() {
        return panelReporteCliente;
    }

    public void actualizarTabla() {
        cargarClientes();
    }
}