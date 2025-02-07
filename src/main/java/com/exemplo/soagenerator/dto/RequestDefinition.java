package com.exemplo.soagenerator.dto;

import java.util.List;

public class RequestDefinition {
    private List<ParameterDefinition> parameters;
    private List<FieldDefinition> fields;

    public List<ParameterDefinition> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterDefinition> parameters) {
        this.parameters = parameters;
    }

    public List<FieldDefinition> getFields() {
        return fields;
    }

    public void setFields(List<FieldDefinition> fields) {
        this.fields = fields;
    }
}
