package com.exemplo.soagenerator.dto;

import java.util.List;

public class ResponseDefinition {
    private List<FieldDefinition> fields;

    public List<FieldDefinition> getFields() {
        return fields;
    }

    public void setFields(List<FieldDefinition> fields) {
        this.fields = fields;
    }
}
