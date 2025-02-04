package com.exemplo.soagenerator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FieldDefinition {
    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private String type;

    @JsonProperty("required")
    private boolean required;

    @JsonProperty("isObject")
    private boolean isObject;

    @JsonProperty("isList")
    private boolean isList;

    @JsonProperty("fields")
    private List<FieldDefinition> fields;

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isObject() {
        return isObject;
    }

    public void setObject(boolean object) {
        isObject = object;
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean list) {
        isList = list;
    }

    public List<FieldDefinition> getFields() {
        return fields;
    }

    public void setFields(List<FieldDefinition> fields) {
        this.fields = fields;
    }
}
