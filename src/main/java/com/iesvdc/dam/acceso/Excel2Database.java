package com.iesvdc.dam.acceso;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.iesvdc.dam.acceso.conexion.Conexion;
import com.iesvdc.dam.acceso.excelutil.ExcelReader;
import com.iesvdc.dam.acceso.modelo.TableModel;
import com.iesvdc.dam.acceso.modelo.WorkbookModel;

/**
 * Este programa genérico en java (proyecto Maven) es un ejercicio 
 * simple que vuelca un libro Excel (xlsx) a una base de datos (MySQL) 
 * y viceversa. El programa lee la configuración de la base de datos 
 * de un fichero "properties" de Java y luego, con apache POI, leo 
 * las hojas, el nombre de cada hoja será el nombre de las tablas, 
 * la primera fila de cada hoja será el nombre de los atributos de 
 * cada tabla (hoja) y para saber el tipo de dato, tendré que 
 * preguntar a la segunda fila qué tipo de dato tiene. 
 * 
 * Procesamos el fichero Excel y creamos una estructura de datos 
 * con la información siguiente: La estructura principal es el libro, 
 * que contiene una lista de tablas y cada tabla contiene tuplas 
 * nombre del campo y tipo de dato.
 *
 */
public class Excel2Database 
{
        public static void main( String[] args )
    {                
        try (Connection conexion = Conexion.getConnection()) {
            if (conexion!=null) {
                System.out.println("Conectado correctamente.");
            }
            else {
                System.out.println("Imposible conectar");
            }
            //Ahora creo el objeto ExcelReader y ejecuto la setencia sql
            WorkbookModel workbook = ExcelReader.leerExcel();
            for (TableModel tabla : workbook.getTables()) {
                //Muestro el nombre de la tabla que voy a crear
                System.out.println("Creando la tabla: "+tabla.getName());
                String setenciaSql = "CREATE TABLE `"+tabla.getName()+"` (  `id` int NOT NULL);";
                PreparedStatement ps = conexion.prepareStatement(setenciaSql);
                ps.executeUpdate(); 
            }

        } catch (Exception e) {
            System.err.println("No se pudo conectar o la tabla ya esta creada");            
        }                    
    }
}

