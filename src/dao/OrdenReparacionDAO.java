package dao;

import factory.ConexionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.OrdenReparacion;

public class OrdenReparacionDAO {
    private ConexionFactory factory;

    public OrdenReparacionDAO() {
        this.factory = new ConexionFactory();
    }
    
    
    public boolean insertar(OrdenReparacion orden) {
        String sql = "INSERT INTO Ordenes_Reparacion (id_equipo, problema_reportado, trabajo_realizado, costo, estado) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conexion = factory.getConexion();
             PreparedStatement comando = conexion.prepareStatement(sql)) {
            
            comando.setInt(1, orden.getIdEquipo());
            comando.setString(2, orden.getProblemaReportado());
            comando.setString(3, orden.getTrabajoRealizado());
            comando.setDouble(4, orden.getCosto());
            comando.setString(5, orden.getEstado());
            
            return comando.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al crear la orden: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(OrdenReparacion orden) {
        String sql = "UPDATE Ordenes_Reparacion SET problema_reportado = ?, trabajo_realizado = ?, costo = ?, estado = ? WHERE id_orden = ?";
        
        try (Connection conexion = factory.getConexion();
             PreparedStatement comando = conexion.prepareStatement(sql)) {
            
            comando.setString(1, orden.getProblemaReportado());
            comando.setString(2, orden.getTrabajoRealizado());
            comando.setDouble(3, orden.getCosto());
            comando.setString(4, orden.getEstado());
            comando.setInt(5, orden.getIdOrden());
            
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
        System.err.println("Error al actualizar orden: " + e.getMessage());
        return false;
    }
}

    public List<Object[]> listarReporteCompleto() {
        List<Object[]> listaReporte = new ArrayList<>();
        
        String sql = "SELECT o.id_orden, CONCAT(c.nombre, ' ', c.apellido) AS nombre_completo, e.modelo, o.fecha_ingreso, o.problema_reportado, o.estado, o.costo " +
                     "FROM Ordenes_Reparacion o " +
                     "JOIN Equipos_Registrados e ON o.id_equipo = e.id_equipo " +
                     "JOIN Clientes c ON e.id_cliente = c.id_cliente " +
                     "ORDER BY o.fecha_ingreso DESC";
        
        try (Connection conexion = factory.getConexion();
             PreparedStatement comando = conexion.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {
            
            while (resultado.next()) {
                        
                        Object[] fila = new Object[6]; 

                        fila[0] = resultado.getInt("id_orden");
                        fila[1] = resultado.getString("nombre_completo"); 
                        fila[2] = resultado.getString("modelo");

                        // ¡OJO AQUÍ! Nos saltamos la "fecha_ingreso" y pasamos directo al problema
                        // para que caiga exactamente en la columna 3 de tu diseño.
                        fila[3] = resultado.getString("problema_reportado");

                        fila[4] = resultado.getString("estado");
                        fila[5] = resultado.getDouble("costo");

                        listaReporte.add(fila);
                    }
        } catch (SQLException e) {
            System.err.println("Error al generar el reporte completo: " + e.getMessage());
        }
        return listaReporte;
    }
    
    public List<Object[]> buscarOrden(String texto) {
        List<Object[]> lista = new ArrayList<>();
        // Usamos LEFT JOIN y CONCAT para mantener la misma estructura de tu tabla
        String sql = "SELECT o.id_orden, CONCAT(c.nombre, ' ', c.apellido) AS nombre_completo, " +
                     "e.modelo, o.problema_reportado, o.estado, o.costo " +
                     "FROM Ordenes_Reparacion o " +
                     "LEFT JOIN Equipos_Registrados e ON o.id_equipo = e.id_equipo " +
                     "LEFT JOIN Clientes c ON e.id_cliente = c.id_cliente " +
                     "WHERE o.id_orden LIKE ? OR c.nombre LIKE ? OR c.apellido LIKE ? OR e.modelo LIKE ? " +
                     "ORDER BY o.id_orden DESC";
        
        try (Connection conexion = factory.getConexion();
             PreparedStatement comando = conexion.prepareStatement(sql)) {
            
            String p = "%" + texto + "%";
            // Pasamos el parámetro 4 veces porque hay 4 signos de interrogación en el WHERE
            comando.setString(1, p);
            comando.setString(2, p);
            comando.setString(3, p);
            comando.setString(4, p);
            
            ResultSet resultado = comando.executeQuery();
            while (resultado.next()) {
                Object[] fila = new Object[6];
                fila[0] = resultado.getInt("id_orden");
                fila[1] = resultado.getString("nombre_completo");
                fila[2] = resultado.getString("modelo");
                fila[3] = resultado.getString("problema_reportado");
                fila[4] = resultado.getString("estado");
                fila[5] = resultado.getDouble("costo");
                lista.add(fila);
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
    
    // Método 1: Trae el problema y el trabajo actual para mostrarlos en la ventanita
    public String[] obtenerTextosOrden(int idOrden) {
        String[] textos = new String[2];
        String sql = "SELECT problema_reportado, trabajo_realizado FROM Ordenes_Reparacion WHERE id_orden = ?";
        
        try (Connection conexion = factory.getConexion();
             PreparedStatement comando = conexion.prepareStatement(sql)) {
             
            comando.setInt(1, idOrden);
            ResultSet rs = comando.executeQuery();
            
            if (rs.next()) {
                textos[0] = rs.getString("problema_reportado");
                textos[1] = rs.getString("trabajo_realizado");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener textos: " + e.getMessage());
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
    
    // Método para cambiar el estado de la orden a 'Entregado'
    public boolean marcarComoEntregado(int idOrden) {
        // La instrucción mágica para actualizar solo el estado de ese equipo
        String sql = "UPDATE ordenes_reparacion SET estado = 'Entregado' WHERE id_orden = ?";
        
        try (java.sql.Connection conexion = factory.getConexion();
             java.sql.PreparedStatement comando = conexion.prepareStatement(sql)) {

            comando.setInt(1, idOrden);
            return comando.executeUpdate() > 0;

        } catch (java.sql.SQLException e) {
            System.err.println("Error al entregar equipo: " + e.getMessage());
            return false;
        }
    }
    
    public List<Object[]> filtrarPorEstado(String estadoFiltro) {
        List<Object[]> listaReporte = new ArrayList<>();
        
        // ¡TU MISMA CONSULTA SQL! Solo le agregamos el WHERE para filtrar
        String sql = "SELECT o.id_orden, CONCAT(c.nombre, ' ', c.apellido) AS nombre_completo, e.modelo, o.fecha_ingreso, o.problema_reportado, o.estado, o.costo " +
                     "FROM Ordenes_Reparacion o " +
                     "JOIN Equipos_Registrados e ON o.id_equipo = e.id_equipo " +
                     "JOIN Clientes c ON e.id_cliente = c.id_cliente " +
                     "WHERE o.estado = ? " + // <-- AQUÍ ESTÁ LA MAGIA DEL FILTRO
                     "ORDER BY o.fecha_ingreso DESC";
        
        try (Connection conexion = factory.getConexion();
             PreparedStatement comando = conexion.prepareStatement(sql)) {
             
            // Le mandamos el estado ("Entregado", "Recibido", etc.) a la consulta
            comando.setString(1, estadoFiltro);
            
            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    Object[] fila = new Object[6]; 

                    // Tus mismos índices exactos
                    fila[0] = resultado.getInt("id_orden");
                    fila[1] = resultado.getString("nombre_completo"); 
                    fila[2] = resultado.getString("modelo");
                    fila[3] = resultado.getString("problema_reportado");
                    fila[4] = resultado.getString("estado");
                    fila[5] = resultado.getDouble("costo");

                    listaReporte.add(fila);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al filtrar por estado: " + e.getMessage());
        }
        return listaReporte;
    }
    
    public String[] buscarDetallesEquipoParaOrden(int idEquipo) {
        // Un arreglo para guardar [0] = Nombre del Cliente, [1] = Modelo del Equipo
        String[] detalles = new String[2]; 
        
        // Tu magia con JOIN
        String sql = "SELECT CONCAT(c.nombre, ' ', c.apellido) AS nombre_completo, e.modelo " +
                     "FROM Equipos_Registrados e " +
                     "JOIN Clientes c ON e.id_cliente = c.id_cliente " +
                     "WHERE e.id_equipo = ?";
                     
        try (java.sql.Connection conexion = factory.getConexion();
             java.sql.PreparedStatement comando = conexion.prepareStatement(sql)) {
             
            comando.setInt(1, idEquipo);
            
            try (java.sql.ResultSet rs = comando.executeQuery()) {
                if (rs.next()) {
                    detalles[0] = rs.getString("nombre_completo"); // Lo metemos en la posición 0
                    detalles[1] = rs.getString("modelo");          // Lo metemos en la posición 1
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error al buscar detalles del equipo: " + e.getMessage());
        }
        return detalles; // Si no lo encuentra, devuelve un arreglo con nulls
    }
}