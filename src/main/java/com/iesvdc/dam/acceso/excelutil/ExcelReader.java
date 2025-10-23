package com.iesvdc.dam.acceso.excelutil;

import java.io.FileInputStream;
import java.util.Properties;

import javax.imageio.IIOException;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.iesvdc.dam.acceso.conexion.Config;
import com.iesvdc.dam.acceso.modelo.TableModel;
import com.iesvdc.dam.acceso.modelo.WorkbookModel;

public class ExcelReader {
    public static WorkbookModel leerExcel(){
        WorkbookModel wm1 = new WorkbookModel();
        Properties propiedades = Config.getProperties("config.properties");
        String path = propiedades.getProperty("inputFile");
            try {
                FileInputStream fichero = new FileInputStream(path);
                XSSFWorkbook wb = new XSSFWorkbook(fichero); 
                for (Sheet hoja : wb) {
                    TableModel tabla = new TableModel(hoja.getSheetName());  
                    wm1.addTabla(tabla);
                }
            } catch (Exception e) {
                throw new RuntimeException("te da excepción, codigo: " + e);
            }
        return wm1;
    }
}
