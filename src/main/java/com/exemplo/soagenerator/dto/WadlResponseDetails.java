package com.exemplo.soagenerator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WadlResponseDetails {

    @JsonProperty("fields")
    private List<WadlField> fields;

    public List<WadlField> getFields() { return fields; }
    public void setFields(List<WadlField> fields) { this.fields = fields; }
}
