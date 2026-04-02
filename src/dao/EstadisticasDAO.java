package dao;

import factory.ConexionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Estadisticas;

public class EstadisticasDAO {
    
    private ConexionFactory factory = new ConexionFactory();

    public Estadisticas obtenerNumerosMagicos() {
        Estadisticas est = new Estadisticas();
        
        // 1. Contamos cuántos equipos ya se entregaron
        // Nota: Asumo que guardas la palabra 'Entregado'. Si usas 'Finalizado' o 'Terminado', solo cambia esa palabra aquí.
        String sqlEntregados = "SELECT COUNT(*) AS total FROM ordenes_reparacion WHERE estado = 'Entregado'";
        
        // 2. Contamos cuántos están en el taller todavía (Todo lo que NO sea 'Entregado')
        String sqlPendientes = "SELECT COUNT(*) AS total FROM ordenes_reparacion WHERE estado != 'Entregado'";
        
        // 3. Sumamos el 'costo' de todas las órdenes entregadas
        String sqlGanancias = "SELECT SUM(costo) AS dinero FROM ordenes_reparacion WHERE estado = 'Entregado'";

        try (Connection conexion = factory.getConexion()) {
            
            // Ejecutamos la consulta 1
            try (PreparedStatement cmd1 = conexion.prepareStatement(sqlEntregados);
                 ResultSet rs1 = cmd1.executeQuery()) {
                if (rs1.next()) est.setEquiposEntregados(rs1.getInt("total"));
            }

            // Ejecutamos la consulta 2
            try (PreparedStatement cmd2 = conexion.prepareStatement(sqlPendientes);
                 ResultSet rs2 = cmd2.executeQuery()) {
                if (rs2.next()) est.setOrdenesPendientes(rs2.getInt("total"));
            }

            // Ejecutamos la consulta 3 (el dinero para la Ninja 400)
            try (PreparedStatement cmd3 = conexion.prepareStatement(sqlGanancias);
                 ResultSet rs3 = cmd3.executeQuery()) {
                if (rs3.next()) est.setGananciasTotales(rs3.getDouble("dinero"));
            }

        } catch (SQLException e) {
            System.err.println("Error al calcular estadísticas: " + e.getMessage());
        }
        
        return est;
    }
    
    // Método para alimentar el gráfico de pastel
    public java.util.Map<String, Integer> obtenerDatosGraficoEquipos() {
        java.util.Map<String, Integer> mapaDatos = new java.util.HashMap<>();
        
        // OJO: Verifica que los nombres de las tablas coincidan con tu base de datos.
        // Consulta corregida mirando tu base de datos real
        String sql = "SELECT t.nombre_tipo, COUNT(e.id_equipo) AS total "
                   + "FROM equipos_registrados e "
                   + "INNER JOIN tipos_equipo t ON e.id_tipo = t.id_tipo "
                   + "GROUP BY t.nombre_tipo";
        
        try (java.sql.Connection con = new factory.ConexionFactory().getConexion();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                // Guardamos el nombre del tipo y su cantidad en el mapa
                mapaDatos.put(rs.getString("nombre_tipo"), rs.getInt("total"));
            }
            
        } catch (java.sql.SQLException e) {
            System.err.println("Error al obtener datos para el gráfico: " + e.getMessage());
        }
        
        return mapaDatos;
    }
}