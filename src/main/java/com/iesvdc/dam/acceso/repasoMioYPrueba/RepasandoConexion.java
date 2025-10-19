package com.iesvdc.dam.acceso.repasoMioYPrueba;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class RepasandoConexion {
    public static void main(String[] args) {
        //Esto fichero es mio para hacer un mini repsao de conexion a bbdd
        //Eso de aqui estara en config.properties.
        String url = "jdbc:mysql://localhost:33307/agenda"; 
        String user = "root";
        String password = "s83n38DGB8d72";
        //Ahora pruebo la conexión y creo una tabla de prueba
         try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Crear tabla de prueba
            String sql = "CREATE TABLE IF NOT EXISTS probando (" +
                         "id INT AUTO_INCREMENT PRIMARY KEY," +
                         "nombre VARCHAR(50) NOT NULL" +
                         ")";
            stmt.executeUpdate(sql);
            System.out.println("Tabla 'prueba' creada correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
