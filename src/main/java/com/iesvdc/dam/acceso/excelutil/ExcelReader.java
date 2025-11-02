package com.iesvdc.dam.acceso.excelutil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.iesvdc.dam.acceso.conexion.Config;


public class ExcelReader {
    //Metodo que devuele el workbook configurado en properties
    public static XSSFWorkbook leerExcel(){
        Properties propiedades = Config.getProperties("config.properties");
        String fichero = propiedades.getProperty("inputFile");
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(fichero);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        return wb;
    }
    
    
    public static void insertarWorkbook(Connection conexion,XSSFWorkbook xssfworkbook){
        try  {
            
            // Ahora creo el objeto ExcelReader y ejecuto la setencia sql en bucles anidados
            // usando un stringbuilder

            XSSFWorkbook excel = xssfworkbook;
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
                                      .append(celda.getStringCellValue())
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
                System.out.println("Insertando datos de " + hoja.getSheetName());

            }

        } catch (Exception e) {
            System.out.println("ERROR AQUI:, LA TABLA/S YA EXISTEN ");
            e.printStackTrace();
        }
    }
    
}
