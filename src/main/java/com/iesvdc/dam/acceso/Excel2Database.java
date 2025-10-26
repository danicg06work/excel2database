package com.iesvdc.dam.acceso;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

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
            if (conexion != null) {
                System.out.println("Conectado correctamente.");
            } else {
                System.out.println("Imposible conectar");
            }
            // Ahora creo el objeto ExcelReader y ejecuto la setencia sql en bucles anidados
            // usando un stringbuilder

            XSSFWorkbook excel = ExcelReader.leerExcel();
            for (int i = 0; i < excel.getNumberOfSheets(); i++) {
                Sheet hoja = excel.getSheetAt(i);
                System.out.println("Añadiendo tabla: " + hoja.getSheetName());

                // Empiezo a crear la setencia con Stringbuilder
                // Se que es un poco loco lo que he hecho pero funciona
                StringBuilder sb = new StringBuilder();
                Row fila = hoja.getRow(0);
                Row fila2 = hoja.getRow(1);
                int contador = 0;
                sb.append("CREATE TABLE `").append(hoja.getSheetName()).append("` ( ");
                for (Cell celda : fila) {
                    // Esto if es para que ponga la coma menos en la primera columna
                    if (contador != 0) {
                        sb.append(", ");
                    }
                    // Este append meta la columna
                    sb.append("`").append(celda.getStringCellValue()).append("` ");
                    // Ahora para meterle segun el tipo de dato
                    Cell dato = fila2.getCell(contador);
                    switch (dato.getCellType()) {
                        case BOOLEAN:
                            sb.append("tinyint(1)");
                            break;
                        case NUMERIC:
                            double numero = dato.getNumericCellValue();
                            if (numero == Math.floor(numero)) {
                                sb.append("INT (20)");
                            } else {
                                sb.append("decimal(20,2)");
                            }
                            break;
                        default:
                            sb.append("varchar(100)");
                            break;
                    }
                    contador++;
                }
                sb.append("); ");

                // Ejecuto la setencia para crear la tabla:

                PreparedStatement ps = conexion.prepareStatement(sb.toString());
                ps.executeUpdate();


                
                // Ahora hago el insert de la tabla de los datos:
             StringBuilder insert = new StringBuilder();
                insert.append("INSERT INTO `").append(hoja.getSheetName()).append("` (");

                
                for (int c = 0; c < fila.getLastCellNum(); c++) {
                    if (c != 0) insert.append(", ");
                    insert.append("`").append(fila.getCell(c).getStringCellValue()).append("`");
                }
                insert.append(") VALUES ");

                
                boolean primeraFila = true;
                for (int r = 1; r <= hoja.getLastRowNum(); r++) {
                    Row filaDatos = hoja.getRow(r);
                    

                    if (!primeraFila) insert.append(", ");
                    insert.append("(");

                    for (int c = 0; c < filaDatos.getLastCellNum(); c++) {
                        if (c != 0) insert.append(", ");
                        Cell celda = filaDatos.getCell(c);

                        if (celda == null) {
                            insert.append("NULL");
                            continue;
                        }

                        switch (celda.getCellType()) {
                            case BOOLEAN:
                                insert.append(celda.getBooleanCellValue() ? 1 : 0);
                                break;
                            case NUMERIC:
                                insert.append(celda.getNumericCellValue());
                                break;
                            default:
                                insert.append("'")
                                      .append(celda.getStringCellValue().replace("'", "''"))
                                      .append("'");
                                break;
                        }
                    }

                    insert.append(")");
                    primeraFila = false;
                }

                insert.append(";");
                PreparedStatement psInsert = conexion.prepareStatement(insert.toString());
                psInsert.executeUpdate();
                System.out.println("Insertando datos de" + hoja.getSheetName());

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