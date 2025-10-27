package com.iesvdc.dam.acceso;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
public class Excel2Database {
    public static void main(String[] args) {
        try (Connection conexion = Conexion.getConnection()) {
            //Compruebo la conexion
            if (conexion != null) {
                System.out.println("Conectado correctamente.");
            } else {
                System.out.println("Imposible conectar");
            }
            //Le pregunto al usuario que quiere
            System.out.println("Que quieres hacer? \n1.Insertar en la bd (1)\n2.Crear excel de la bs (2)");
            Scanner inputUsuario = new Scanner(System.in);
            switch (inputUsuario.nextLine()) {
                case "1":
                    ExcelReader.insertarWorkbook(conexion,ExcelReader.leerExcel());
                    break;
                case "2":
                    System.out.println("Opcion no disponible en este momento");
                    break;
                default:
                System.out.println("Saliendo . . .");
                    break;
            }
            
        } catch (Exception e) {
            System.out.println("ERROR AQUI:");
            e.printStackTrace();
        }

        
    }
}

//Ejemplos de las sentecias sql:
/*
 * CREATE TABLE `Borrame` (
 * `id` int NOT NULL,
 * `nombre` int NOT NULL
 * );
 */

/*
 * INSERT INTO `Almacen` (`id`, `producto`, `Perecedero`)
 * VALUES ('128', 'manzana', 'TRUE');
 */