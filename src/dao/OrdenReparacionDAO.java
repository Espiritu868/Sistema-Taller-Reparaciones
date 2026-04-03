package dao;

import factory.ConexionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.OrdenReparacion;

public class OrdenReparacionDAO {
    private ConexionFactory factory;

    public OrdenReparacionDAO() {
        this.factory = new ConexionFactory();
    }

    public int insertarConId(OrdenReparacion orden) {
        String sql = "INSERT INTO Ordenes_Reparacion (id_equipo, problema_reportado, trabajo_realizado, costo, estado) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conexion = factory.getConexion();
             PreparedStatement comando = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            comando.setInt(1, orden.getIdEquipo());
            comando.setString(2, orden.getProblemaReportado());
            comando.setString(3, orden.getTrabajoRealizado());
            comando.setDouble(4, orden.getCosto());
            comando.setString(5, orden.getEstado());
            
            int filasAfectadas = comando.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet rs = comando.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar orden: " + e.getMessage());
        }
        return -1;
    }

    public boolean insertar(OrdenReparacion orden) {
        return insertarConId(orden) != -1;
    }

    public boolean actualizar(OrdenReparacion orden) {
        String sql = "UPDATE Ordenes_Reparacion SET problema_reportado = ?, trabajo_realizado = ?, costo = ?, estado = ?, id_usuario_entrega = ? WHERE id_orden = ?";
        
        try (Connection conexion = factory.getConexion();
             PreparedStatement comando = conexion.prepareStatement(sql)) {
            
            comando.setString(1, orden.getProblemaReportado());
            comando.setString(2, orden.getTrabajoRealizado());
            comando.setDouble(3, orden.getCosto());
            comando.setString(4, orden.getEstado());
            
            if (orden.getIdUsuarioEntrega() > 0) {
                comando.setInt(5, orden.getIdUsuarioEntrega());
            } else {
                comando.setNull(5, java.sql.Types.INTEGER);
            }
            
            comando.setInt(6, orden.getIdOrden());
            
            return comando.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar la orden: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizarEstadoYCosto(int idOrden, String nuevoEstado, double nuevoCosto) {
        String sql = "UPDATE Ordenes_Reparacion SET estado = ?, costo = ? WHERE id_orden = ?";
        
        try (Connection conexion = factory.getConexion();
             PreparedStatement comando = conexion.prepareStatement(sql)) {
            
            comando.setString(1, nuevoEstado);
            comando.setDouble(2, nuevoCosto);
            comando.setInt(3, idOrden);
            
            return comando.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado y costo: " + e.getMessage());
            return false;
        }
    }

    public List<Object[]> listarReporteCompleto() {
        List<Object[]> listaReporte = new ArrayList<>();
        // Añadimos t.nombre_tipo a la consulta
        String sql = "SELECT o.id_orden, CONCAT(c.nombre, ' ', c.apellido) AS nombre_completo, e.modelo, o.fecha_ingreso, o.problema_reportado, o.estado, o.costo, t.nombre_tipo " +
                     "FROM Ordenes_Reparacion o " +
                     "JOIN Equipos_Registrados e ON o.id_equipo = e.id_equipo " +
                     "JOIN Clientes c ON e.id_cliente = c.id_cliente " +
                     "JOIN Tipos_Equipo t ON e.id_tipo = t.id_tipo " + 
                     "ORDER BY o.fecha_ingreso DESC";
        
        try (Connection conexion = factory.getConexion();
             PreparedStatement comando = conexion.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {
            
            while (resultado.next()) {
                Object[] fila = new Object[7]; // Ampliamos a 7 columnas
                fila[0] = resultado.getInt("id_orden");
                fila[1] = resultado.getString("nombre_completo"); 
                fila[2] = resultado.getString("modelo");
                fila[3] = resultado.getString("problema_reportado");
                fila[4] = resultado.getString("estado");
                fila[5] = resultado.getDouble("costo");
                fila[6] = resultado.getString("nombre_tipo"); // Nueva columna
                listaReporte.add(fila);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reporte completo: " + e.getMessage());
        }
        return listaReporte;
    }
    
    public List<Object[]> buscarOrden(String texto) {
        List<Object[]> lista = new ArrayList<>();
        // Añadimos t.nombre_tipo a la consulta de búsqueda
        String sql = "SELECT o.id_orden, CONCAT(c.nombre, ' ', c.apellido) AS nombre_completo, " +
                     "e.modelo, o.problema_reportado, o.estado, o.costo, t.nombre_tipo " +
                     "FROM Ordenes_Reparacion o " +
                     "LEFT JOIN Equipos_Registrados e ON o.id_equipo = e.id_equipo " +
                     "LEFT JOIN Clientes c ON e.id_cliente = c.id_cliente " +
                     "LEFT JOIN Tipos_Equipo t ON e.id_tipo = t.id_tipo " +
                     "WHERE o.id_orden LIKE ? OR c.nombre LIKE ? OR c.apellido LIKE ? OR e.modelo LIKE ? " +
                     "ORDER BY o.id_orden DESC";
        
        try (Connection conexion = factory.getConexion();
             PreparedStatement comando = conexion.prepareStatement(sql)) {
            
            String p = "%" + texto + "%";
            comando.setString(1, p);
            comando.setString(2, p);
            comando.setString(3, p);
            comando.setString(4, p);
            
            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    Object[] fila = new Object[7];
                    fila[0] = resultado.getInt("id_orden");
                    fila[1] = resultado.getString("nombre_completo");
                    fila[2] = resultado.getString("modelo");
                    fila[3] = resultado.getString("problema_reportado");
                    fila[4] = resultado.getString("estado");
                    fila[5] = resultado.getDouble("costo");
                    fila[6] = resultado.getString("nombre_tipo"); // Nueva columna
                    lista.add(fila);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error buscando orden: " + e.getMessage());
        }
        return lista;
    }
    
    public boolean eliminar(int idOrden) {
        String sql = "DELETE FROM Ordenes_Reparacion WHERE id_orden = ?";
        try (Connection conexion = factory.getConexion();
             PreparedStatement comando = conexion.prepareStatement(sql)) {
            
            comando.setInt(1, idOrden);
            return comando.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar orden: " + e.getMessage());
            return false;
        }
    }
    
    public String[] obtenerTextosOrden(int idOrden) {
        String[] textos = new String[2];
        String sql = "SELECT problema_reportado, trabajo_realizado FROM Ordenes_Reparacion WHERE id_orden = ?";
        
        try (Connection conexion = factory.getConexion();
             PreparedStatement comando = conexion.prepareStatement(sql)) {
             
            comando.setInt(1, idOrden);
            try (ResultSet rs = comando.executeQuery()) {
                if (rs.next()) {
                    textos[0] = rs.getString("problema_reportado");
                    textos[1] = rs.getString("trabajo_realizado");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener textos de la orden: " + e.getMessage());
        }
        return textos;
    }

    public boolean actualizarTextosOrden(int idOrden, String nuevoProblema, String nuevoTrabajo) {
        String sql = "UPDATE Ordenes_Reparacion SET problema_reportado = ?, trabajo_realizado = ? WHERE id_orden = ?";
        try (Connection conexion = factory.getConexion();
             PreparedStatement comando = conexion.prepareStatement(sql)) {
             
            comando.setString(1, nuevoProblema);
            comando.setString(2, nuevoTrabajo);
            comando.setInt(3, idOrden);
            return comando.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar textos: " + e.getMessage());
            return false;
        }
    }
    
    public boolean marcarComoEntregado(int idOrden, int idUsuarioEntrega) {
        String sql = "UPDATE Ordenes_Reparacion SET estado = 'Entregado', id_usuario_entrega = ? WHERE id_orden = ?";
        
        try (Connection conexion = factory.getConexion();
             PreparedStatement comando = conexion.prepareStatement(sql)) {

            comando.setInt(1, idUsuarioEntrega);
            comando.setInt(2, idOrden);
            
            return comando.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al marcar equipo como entregado: " + e.getMessage());
            return false;
        }
    }
    
    public List<Object[]> filtrarPorEstado(String estadoFiltro) {
        List<Object[]> listaReporte = new ArrayList<>();
        // Añadimos t.nombre_tipo al filtro también
        String sql = "SELECT o.id_orden, CONCAT(c.nombre, ' ', c.apellido) AS nombre_completo, e.modelo, o.fecha_ingreso, o.problema_reportado, o.estado, o.costo, t.nombre_tipo " +
                     "FROM Ordenes_Reparacion o " +
                     "JOIN Equipos_Registrados e ON o.id_equipo = e.id_equipo " +
                     "JOIN Clientes c ON e.id_cliente = c.id_cliente " +
                     "JOIN Tipos_Equipo t ON e.id_tipo = t.id_tipo " +
                     "WHERE o.estado = ? " + 
                     "ORDER BY o.fecha_ingreso DESC";
        
        try (Connection conexion = factory.getConexion();
             PreparedStatement comando = conexion.prepareStatement(sql)) {
             
            comando.setString(1, estadoFiltro);
            
            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    Object[] fila = new Object[7]; 
                    fila[0] = resultado.getInt("id_orden");
                    fila[1] = resultado.getString("nombre_completo"); 
                    fila[2] = resultado.getString("modelo");
                    fila[3] = resultado.getString("problema_reportado");
                    fila[4] = resultado.getString("estado");
                    fila[5] = resultado.getDouble("costo");
                    fila[6] = resultado.getString("nombre_tipo"); // Nueva columna
                    listaReporte.add(fila);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al filtrar órdenes por estado: " + e.getMessage());
        }
        return listaReporte;
    }
    
    public String[] buscarDetallesEquipoParaOrden(int idEquipo) {
        String[] detalles = new String[2]; 
        String sql = "SELECT CONCAT(c.nombre, ' ', c.apellido) AS nombre_completo, e.modelo " +
                     "FROM Equipos_Registrados e " +
                     "JOIN Clientes c ON e.id_cliente = c.id_cliente " +
                     "WHERE e.id_equipo = ?";
                     
        try (Connection conexion = factory.getConexion();
             PreparedStatement comando = conexion.prepareStatement(sql)) {
             
            comando.setInt(1, idEquipo);
            
            try (ResultSet rs = comando.executeQuery()) {
                if (rs.next()) {
                    detalles[0] = rs.getString("nombre_completo");
                    detalles[1] = rs.getString("modelo");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar detalles del equipo: " + e.getMessage());
        }
        return detalles;
    }
    
    public String obtenerNombreTecnicoEntrega(int idOrden) {
        String nombreTecnico = "Pendiente de entrega";
        String sql = "SELECT u.usuario FROM Ordenes_Reparacion o " +
                     "INNER JOIN usuarios u ON o.id_usuario_entrega = u.id_usuario " +
                     "WHERE o.id_orden = ?";
                     
        try (Connection conexion = factory.getConexion();
             PreparedStatement comando = conexion.prepareStatement(sql)) {
             
            comando.setInt(1, idOrden);
            try (ResultSet rs = comando.executeQuery()) {
                if (rs.next()) {
                    nombreTecnico = rs.getString("usuario");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener nombre del técnico: " + e.getMessage());
        }
        
        return nombreTecnico;
    }
}