package com.exemplo.soagenerator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WadlRequestDetails {

    @JsonProperty("parameters")
    private List<WadlParameter> parameters;

    @JsonProperty("fields")
    private List<WadlField> fields;

    public List<WadlParameter> getParameters() { return parameters; }
    public void setParameters(List<WadlParameter> parameters) { this.parameters = parameters; }

    public List<WadlField> getFields() { return fields; }
    public void setFields(List<WadlField> fields) { this.fields = fields; }
}
