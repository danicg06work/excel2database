package com.iesvdc.dam.acceso.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * El modelo que almacena información de una tabla y su lista de campos.
 */
public class TableModel {
    private final String name;
    private final List<FieldModel> fields = new ArrayList<>();

    public TableModel(String name) {
        this.name = name;
    }

    public void addField(FieldModel field){
        fields.add(field);
    }


    public String getName() {
        return this.name;
    }


    public List<FieldModel> getFields() {
        return this.fields;
    }


}
