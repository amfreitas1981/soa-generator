package com.exemplo.soagenerator.dto;

import java.util.List;

public class XsdObject {
    private String name;
    private List<XsdField> fields; // Lista de campos do objeto

    public XsdObject() {}

    public XsdObject(String name, List<XsdField> fields) {
        this.name = name;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<XsdField> getFields() {
        return fields;
    }

    public void setFields(List<XsdField> fields) {
        this.fields = fields;
    }
}
