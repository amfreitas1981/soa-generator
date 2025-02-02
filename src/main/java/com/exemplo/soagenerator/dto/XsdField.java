package com.exemplo.soagenerator.dto;

public class XsdField {
    private String name;
    private String type;
    private boolean required;
    private boolean isObject; // Se for um objeto, o tipo será referenciado no XSD
    private boolean isList;   // Indica se é uma lista (maxOccurs="unbounded")

    public XsdField() {}

    public XsdField(String name, String type, boolean required, boolean isObject, boolean isList) {
        this.name = name;
        this.type = type;
        this.required = required;
        this.isObject = isObject;
        this.isList = isList;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public boolean isObject() { return isObject; }
    public void setObject(boolean object) { isObject = object; }

    public boolean isList() { return isList; }
    public void setList(boolean list) { isList = list; }
}
