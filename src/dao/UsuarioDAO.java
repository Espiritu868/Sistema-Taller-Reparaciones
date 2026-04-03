package dao;

import factory.ConexionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;

public class UsuarioDAO {
    private ConexionFactory factory = new ConexionFactory();

    // 1. Método de seguridad suprema: Convierte "1234" en un Hash SHA-256
    public String encriptarContraseña(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error al encriptar: " + ex.getMessage());
        }
    }

    // Ahora devuelve un String. Si falla, devuelve "ERROR"
    public String validarLogin(String usuario, String passwordReal) {
        String sql = "SELECT rol FROM Usuarios WHERE usuario = ? AND password_hash = ?";
        
        try (java.sql.Connection conexion = factory.getConexion();
             java.sql.PreparedStatement comando = conexion.prepareStatement(sql)) {
             
            String passwordEncriptado = encriptarContraseña(passwordReal);
            
            comando.setString(1, usuario);
            comando.setString(2, passwordEncriptado);
            
            java.sql.ResultSet rs = comando.executeQuery();
            
            // Si encuentra al usuario, extraemos qué Rol tiene y lo devolvemos
            if (rs.next()) {
                return rs.getString("rol"); 
            }
            
        } catch (java.sql.SQLException e) {
            System.err.println("Error SQL en el login: " + e.getMessage());
        }
        
        // Si no encontró nada o hubo error, no pasa
        return "ERROR";
    }
    
    // Método para llenar la tabla de usuarios
    public java.util.List<modelo.Usuario> listarUsuarios() {
        java.util.List<modelo.Usuario> lista = new java.util.ArrayList<>();
        
        // Solo traemos el ID, el nombre y el rol. ¡La contraseña se queda en la BD!
        String sql = "SELECT id_usuario, usuario, rol FROM Usuarios";
        
        try (java.sql.Connection conexion = factory.getConexion();
             java.sql.PreparedStatement comando = conexion.prepareStatement(sql);
             java.sql.ResultSet rs = comando.executeQuery()) {

            while (rs.next()) {
                modelo.Usuario u = new modelo.Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombreUsuario(rs.getString("usuario"));
                u.setRol(rs.getString("rol"));
                lista.add(u);
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    // Método para crear un nuevo técnico/admin
    public boolean registrarUsuario(String usuario, String passwordReal, String rol) {
        String sql = "INSERT INTO Usuarios (usuario, password_hash, rol) VALUES (?, ?, ?)";
        
        try (java.sql.Connection conexion = factory.getConexion();
             java.sql.PreparedStatement comando = conexion.prepareStatement(sql)) {

            // ¡El paso mágico! Encriptamos la contraseña ANTES de mandarla a MySQL
            String passwordEncriptado = encriptarContraseña(passwordReal);

            comando.setString(1, usuario);
            comando.setString(2, passwordEncriptado);
            comando.setString(3, rol);

            return comando.executeUpdate() > 0;
            
        } catch (java.sql.SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }
    
    // Método para eliminar a un técnico
    public boolean eliminarUsuario(int idUsuario) {
        
        // ¡CANDADO DE SEGURIDAD NIVEL DIOS!
        if (idUsuario == 1) {
            System.out.println("Alerta de seguridad: Intento de borrar al admin principal bloqueado.");
            return false; // Retornamos falso inmediatamente para cancelar la operación
        }

        String sql = "DELETE FROM Usuarios WHERE id_usuario = ?";
        
        try (java.sql.Connection conexion = factory.getConexion();
             java.sql.PreparedStatement comando = conexion.prepareStatement(sql)) {

            comando.setInt(1, idUsuario);
            return comando.executeUpdate() > 0;

        } catch (java.sql.SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
    
    // Método para modificar usuarios
    public boolean modificarUsuario(int idUsuario, String nuevoUsuario, String passwordReal, String nuevoRol) {
        String sql;
        // Si la caja de contraseña tiene texto, actualizamos la contraseña. Si está vacía, la dejamos igual.
        boolean cambiarPassword = !passwordReal.isEmpty();

        if (cambiarPassword) {
            sql = "UPDATE Usuarios SET usuario = ?, password_hash = ?, rol = ? WHERE id_usuario = ?";
        } else {
            sql = "UPDATE Usuarios SET usuario = ?, rol = ? WHERE id_usuario = ?";
        }

        try (java.sql.Connection conexion = factory.getConexion();
             java.sql.PreparedStatement comando = conexion.prepareStatement(sql)) {

            comando.setString(1, nuevoUsuario);

            if (cambiarPassword) {
                // Encriptamos la nueva contraseña
                comando.setString(2, encriptarContraseña(passwordReal));
                comando.setString(3, nuevoRol);
                comando.setInt(4, idUsuario);
            } else {
                // Nos saltamos la contraseña
                comando.setString(2, nuevoRol);
                comando.setInt(3, idUsuario);
            }

            return comando.executeUpdate() > 0;

        } catch (java.sql.SQLException e) {
            System.err.println("Error al modificar usuario: " + e.getMessage());
            return false;
        }
    }
    
        // =========================================================================
    // NUEVO MÉTODO: OBTIENE EL ID DEL USUARIO A PARTIR DE SU NOMBRE
    // =========================================================================
    public int obtenerIdPorNombre(String nombreUsuario) {
        String sql = "SELECT id_usuario FROM usuarios WHERE usuario = ?";
        
        // Instanciamos tu ConexionFactory directamente aquí para evitar errores
        factory.ConexionFactory miFabrica = new factory.ConexionFactory();
        
        try (java.sql.Connection conexion = miFabrica.getConexion(); 
             java.sql.PreparedStatement comando = conexion.prepareStatement(sql)) {
             
            comando.setString(1, nombreUsuario);
            try (java.sql.ResultSet rs = comando.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_usuario");
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error al obtener ID del usuario: " + e.getMessage());
        }
        return 0; 
    }
}
