package com.iesvdc.dam.acceso.excelutil;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {
    private Connection conexion;
    // La salida va a ñser fija en datos
    private String rutaSalida = "datos";

    // Constructor de la clase
    public ExcelWriter(Connection conexion) {
        this.conexion = conexion;
    }

    public void writeExcel() {
        try {
            // Hago el estatement
            XSSFWorkbook workbook = new XSSFWorkbook();
            String setenciaSQL = "SHOW TABLES FROM agenda;";
            PreparedStatement pedirTablas = conexion.prepareStatement(setenciaSQL);

            // Ahora al ejecutar el statement lso resultados los metos en un resulset para
            // despues usar esas tablas
            ResultSet resultado = pedirTablas.executeQuery();

            // Y ahora opero en un while para ir metiendo las sheets en el excel
            while (resultado.next()) {
                String nombreSheet = resultado.getString("Tables_in_agenda");
                Sheet tabla = workbook.createSheet(nombreSheet);
                System.out.println("Creando la hoja "+nombreSheet);
                //por cada tabla hago una consulta para obtner los datos, voy  usar el objeto ResulSetMetaData
                String sqlDatos = "SELECT * FROM "+ nombreSheet ;
                
                PreparedStatement pedirDatos = conexion.prepareStatement(sqlDatos);
                //Lo meto en el meta data
                ResultSet setDatos = pedirDatos.executeQuery();
                ResultSetMetaData metaDatos = setDatos.getMetaData();

                int numeroCol = metaDatos.getColumnCount();
                //System.out.println(numeroCol);

                //Escribir cabezeras
                Row cabezera = tabla.createRow(0);
                for (int i = 1; i <= numeroCol; i++) {
                    cabezera.createCell(i - 1).setCellValue(metaDatos.getColumnName(i));
                }

                //Escribir datos
                int Fila = 1;
                while (setDatos.next()) {
                    Row fila = tabla.createRow(Fila);
                    for (int j = 1; j <= numeroCol; j++) {
                        fila.createCell(j - 1).setCellValue(setDatos.getString(j));
                    }
                    Fila++;
                }   

            }
            FileOutputStream salida = new FileOutputStream("datos/agenda.xlsx");
            workbook.write(salida);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    // Setters y getters

    public Connection getConexion() {
        return this.conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

    public String getRutaSalida() {
        return this.rutaSalida;
    }

    public void setRutaSalida(String rutaSalida) {
        this.rutaSalida = rutaSalida;
    }

}
