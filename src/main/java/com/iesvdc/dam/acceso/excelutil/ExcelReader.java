package com.iesvdc.dam.acceso.excelutil;

import java.io.IOException;
import java.util.Properties;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.iesvdc.dam.acceso.conexion.Config;


public class ExcelReader {
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
}
