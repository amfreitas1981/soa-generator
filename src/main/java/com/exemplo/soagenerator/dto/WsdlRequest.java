package com.exemplo.soagenerator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WsdlRequest {
    @JsonProperty("serviceName")
    private String serviceName;

    @JsonProperty("inputFields")
    private List<FieldDefinition> inputFields;

    @JsonProperty("outputFields")
    private List<FieldDefinition> outputFields;

    // Construtor padrão
    public WsdlRequest() {}

    // Construtor com parâmetros
    public WsdlRequest(String serviceName, List<FieldDefinition> inputFields, List<FieldDefinition> outputFields) {
        this.serviceName = serviceName;
        this.inputFields = inputFields;
        this.outputFields = outputFields;
    }

    // Getters e Setters
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<FieldDefinition> getInputFields() {
        return inputFields;
    }

    public void setInputFields(List<FieldDefinition> inputFields) {
        this.inputFields = inputFields;
    }

    public List<FieldDefinition> getOutputFields() {
        return outputFields;
    }

    public void setOutputFields(List<FieldDefinition> outputFields) {
        this.outputFields = outputFields;
    }
}
