import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import static javax.swing.JOptionPane.showMessageDialog;


public class Clase_modelo extends JFrame {
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JButton btnEliminar;
    private JButton btnGuardar;
    private JButton btnMostrar;
    private JButton btnActualizar;
    private JPanel Jpanel1;
    private JTextField txtId;
    private JTextField txtUsuario;
    private JTextField txtPass;
    private static final String URL = "jdbc:mysql://localhost:3306/base";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    public Clase_modelo() {
        super("CRUD de Usuarios");
        setContentPane(Jpanel1);

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarDatos();
            }
        });

        btnMostrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarInformacion();
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarDatos();
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarRegistro();
            }
        });
    }

    public void iniciar() {
        setLocationRelativeTo(null);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private Connection establecerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    private void cerrarConexion(Connection conexion) {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void mostrarInformacion() {
        try (Connection conexion = establecerConexion();
             PreparedStatement statement = conexion.prepareStatement("SELECT id, nombre, apellido FROM usuarios");
             ResultSet resultSet = statement.executeQuery()) {

            // Construir el modelo de tabla
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Nombre");
            tableModel.addColumn("Apellido");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");

                // Agregar fila al modelo de tabla
                tableModel.addRow(new Object[]{id, nombre, apellido});
            }

            // Crear la tabla
            JTable tabla = new JTable(tableModel);

            // Crear el panel que contendrá la tabla
            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);

            // Crear el marco que contendrá el panel con la tabla
            JFrame frameTabla = new JFrame("Datos en Tabla");
            frameTabla.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo la ventana de la tabla
            frameTabla.getContentPane().add(panelTabla);
            frameTabla.setSize(500, 400);
            frameTabla.setLocationRelativeTo(this);
            frameTabla.setVisible(true);

        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessageDialog(this, "Error al obtener información de la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

     private void guardarDatos() {
        try (Connection conexion = establecerConexion();
             PreparedStatement statement = conexion.prepareStatement("INSERT INTO usuarios (nombre, apellido, usuario, pass) VALUES (?, ?, ?, ?)")) {

            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            String usuario = txtUsuario.getText();
            String pass = txtPass.getText();

            statement.setString(1, nombre);
            statement.setString(2, apellido);
            statement.setString(3, usuario);
            statement.setString(4, pass);

            statement.executeUpdate();

            showMessageDialog(this, "Datos guardados correctamente.");

        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessageDialog(this, "Error al guardar datos en la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }










    private void actualizarDatos() {
        try (Connection conexion = establecerConexion();
             PreparedStatement statement = conexion.prepareStatement("UPDATE usuarios SET nombre = ?, apellido = ? WHERE id = ?")) {

            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();

            // Suponiendo que txtId es un JTextField donde se ingresa el ID del usuario a actualizar
            int id = Integer.parseInt(txtId.getText());

            statement.setString(1, nombre);
            statement.setString(2, apellido);
            statement.setInt(3, id);

            int filasActualizadas = statement.executeUpdate();

            if (filasActualizadas > 0) {
                showMessageDialog(this, "Datos actualizados correctamente.");
            } else {
                showMessageDialog(this, "No se encontró el usuario con ID " + id, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showMessageDialog(this, "Error al actualizar datos en la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarRegistro() {
        try (Connection conexion = establecerConexion();
             PreparedStatement statement = conexion.prepareStatement("DELETE FROM usuarios WHERE id = ?")) {

            // Suponiendo que txtId es un JTextField donde se ingresa el ID del usuario a eliminar
            int id = Integer.parseInt(txtId.getText());

            statement.setInt(1, id);

            int filasEliminadas = statement.executeUpdate();

            if (filasEliminadas > 0) {
                showMessageDialog(this, "Usuario eliminado correctamente.");
            } else {
                showMessageDialog(this, "No se encontró el usuario con ID " + id, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showMessageDialog(this, "Error al eliminar registro en la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}



