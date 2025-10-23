package com.iesvdc.dam.acceso.modelo;

import java.util.ArrayList;
import java.util.List;


/**
 * El modelo que almacena el libro o lista de tablas.
 */
public class WorkbookModel {
    private final List<TableModel> tables = new ArrayList<>();
    public void addTabla(TableModel tabla){
        tables.add(tabla);
    }
    public List<TableModel> getTables() {
        return this.tables;
    }
}
