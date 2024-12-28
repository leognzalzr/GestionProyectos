package vistas;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Tarea {
    private JPanel PanelTarea;
    private JTextField txtDescripcion;
    private JComboBox cmbEstado;
    private JComboBox<ItemsComboBox> cmbDesarrollador;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton btnEliminar;
    private JTable tablaTarea;

    private final DefaultTableModel tableModel;
    private Integer selectedTareaId;
    private final SessionFactory factory;

    public Tarea(SessionFactory factory) {
        this.factory = factory;
        if (this.factory == null) {
            throw new IllegalArgumentException("SessionFactory cannot be null");
        }

        tableModel = new DefaultTableModel(new Object[]{"ID", "Descripción", "Estado", "ID Desarrollador", "Desarrollador"}, 0);
        tablaTarea.setModel(tableModel);

        cargarTareas();
        cargarDesarrolladores();

        tablaTarea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarTarea();
            }
        });

        btnGuardar.addActionListener(_ -> guardarTarea());
        btnCancelar.addActionListener(_ -> limpiarCampos());
        btnEliminar.addActionListener(_ -> eliminarTarea());
    }

    private void cargarDesarrolladores() {
        try (Session session = factory.openSession()) {
            List<modelos.Desarrollador> desarrolladores = session.createQuery("from modelos.Desarrollador", modelos.Desarrollador.class).list();
            cmbDesarrollador.removeAllItems();
            for (modelos.Desarrollador desarrollador : desarrolladores) {
                cmbDesarrollador.addItem(new ItemsComboBox(desarrollador.getId(), desarrollador.getNombre()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar desarrolladores: " + e.getMessage());
        }
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
                        tarea.getDesarrollador().getId(),
                        tarea.getDesarrollador().getNombre()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar tareas: " + e.getMessage());
        }
    }
        private void seleccionarTarea () {
            int selectedRow = tablaTarea.getSelectedRow();
            if (selectedRow != -1) {
                    selectedTareaId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    txtDescripcion.setText((String) tableModel.getValueAt(selectedRow, 1));
                    cmbEstado.setSelectedItem(tableModel.getValueAt(selectedRow, 2));
                    int desarrolladorId = Integer.parseInt(tableModel.getValueAt(selectedRow, 3).toString());
                    for (int i = 0; i < cmbDesarrollador.getItemCount(); i++) {
                        if (cmbDesarrollador.getItemAt(i).getId() == desarrolladorId) {
                            cmbDesarrollador.setSelectedIndex(i);
                            break;
                        }
                    }
            }
        }

    private void guardarTarea() {
        if (txtDescripcion.getText().isEmpty() || cmbEstado.getSelectedItem() == null || cmbDesarrollador.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
            return;
        }

        String estado = cmbEstado.getSelectedItem().toString();

        try (Session session = factory.openSession()) {
            session.beginTransaction();
            modelos.Tarea tarea;


            ItemsComboBox selectedItem = (ItemsComboBox) cmbDesarrollador.getSelectedItem();
            modelos.Desarrollador desarrollador = session.get(modelos.Desarrollador.class, selectedItem.getId());
            if (selectedTareaId == null) {
                tarea = new modelos.Tarea(
                        txtDescripcion.getText(),
                        estado,
                        desarrollador
                );
                session.persist(tarea);
            } else {
                tarea = session.get(modelos.Tarea.class, selectedTareaId);
                if (tarea != null) {
                    tarea.setDescripcion(txtDescripcion.getText());
                    tarea.setEstado(estado);
                    tarea.setDesarrollador(desarrollador);
                    session.merge(tarea);
                }
            }

            session.getTransaction().commit();
            cargarTareas();
            limpiarCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar tarea: " + e.getMessage());
        }
    }
        private void eliminarTarea () {
            if (selectedTareaId == null) {
                JOptionPane.showMessageDialog(null, "Por favor, seleccione una tarea.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar esta tarea?");
            if (confirm == JOptionPane.YES_OPTION) {
                try (Session session = factory.openSession()) {
                    session.beginTransaction();
                    modelos.Tarea tarea = session.get(modelos.Tarea.class, selectedTareaId);
                    if (tarea != null) {
                        session.remove(tarea);
                        session.getTransaction().commit();
                        cargarTareas();
                        limpiarCampos();
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error al eliminar tarea: " + e.getMessage());
                }
            }
        }

        private void limpiarCampos () {
            txtDescripcion.setText("");
            cmbEstado.setSelectedIndex(-1);
            cmbDesarrollador.setSelectedIndex(-1);
            selectedTareaId = null;
            tablaTarea.clearSelection();
        }

        public JPanel getPanel () {
            return PanelTarea;
        }
}