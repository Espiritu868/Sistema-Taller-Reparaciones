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
    
    // =========================================================================
    // 1. OBTENER NÚMEROS MÁGICOS POR MES Y AÑO
    // =========================================================================
    // 1. Obtener números (Tarjetas) por mes y año
    public modelo.Estadisticas obtenerNumerosMagicosMes(int mes, int anio) {
        modelo.Estadisticas est = new modelo.Estadisticas();
        
        String sqlEntregados = "SELECT COUNT(*) AS total FROM ordenes_reparacion WHERE estado = 'Entregado' AND MONTH(fecha_ingreso) = ? AND YEAR(fecha_ingreso) = ?";
        String sqlPendientes = "SELECT COUNT(*) AS total FROM ordenes_reparacion WHERE estado NOT IN ('Entregado', 'Sin Reparacion') AND MONTH(fecha_ingreso) = ? AND YEAR(fecha_ingreso) = ?";
        String sqlGanancias = "SELECT SUM(costo) AS dinero FROM ordenes_reparacion WHERE estado = 'Entregado' AND MONTH(fecha_ingreso) = ? AND YEAR(fecha_ingreso) = ?";

        try (java.sql.Connection con = factory.getConexion()) {
            // Consulta Entregados
            try (java.sql.PreparedStatement ps = con.prepareStatement(sqlEntregados)) {
                ps.setInt(1, mes); ps.setInt(2, anio);
                try (java.sql.ResultSet rs = ps.executeQuery()) { if (rs.next()) est.setEquiposEntregados(rs.getInt("total")); }
            }
            // Consulta Pendientes
            try (java.sql.PreparedStatement ps = con.prepareStatement(sqlPendientes)) {
                ps.setInt(1, mes); ps.setInt(2, anio);
                try (java.sql.ResultSet rs = ps.executeQuery()) { if (rs.next()) est.setOrdenesPendientes(rs.getInt("total")); }
            }
            // Consulta Ganancias
            try (java.sql.PreparedStatement ps = con.prepareStatement(sqlGanancias)) {
                ps.setInt(1, mes); ps.setInt(2, anio);
                try (java.sql.ResultSet rs = ps.executeQuery()) { if (rs.next()) est.setGananciasTotales(rs.getDouble("dinero")); }
            }
        } catch (Exception e) { System.err.println("Error mensual: " + e.getMessage()); }
        return est;
    }

    // 2. Obtener datos del Gráfico por mes y año
    public java.util.Map<String, Integer> obtenerDatosGraficoEquiposMes(int mes, int anio) {
        java.util.Map<String, Integer> mapa = new java.util.HashMap<>();
        String sql = "SELECT t.nombre_tipo, COUNT(o.id_orden) AS total " +
                     "FROM ordenes_reparacion o " +
                     "JOIN equipos_registrados e ON o.id_equipo = e.id_equipo " +
                     "JOIN tipos_equipo t ON e.id_tipo = t.id_tipo " +
                     "WHERE MONTH(o.fecha_ingreso) = ? AND YEAR(o.fecha_ingreso) = ? " +
                     "GROUP BY t.nombre_tipo";

        try (java.sql.Connection con = factory.getConexion();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, mes); ps.setInt(2, anio);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) { mapa.put(rs.getString("nombre_tipo"), rs.getInt("total")); }
            }
        } catch (Exception e) { System.err.println("Error gráfico mensual: " + e.getMessage()); }
        return mapa;
    }
}