package com.exemplo.soagenerator.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class XsdRequest {
    @NotBlank(message = "O nome do serviço é obrigatório")
    private String serviceName;
    private List<XsdField> inputFields;
    private List<XsdField> outputFields;
    private List<XsdObject> objects; // Lista de objetos complexos

    public XsdRequest() {}

    public XsdRequest(String serviceName, List<XsdField> inputFields, List<XsdField> outputFields, List<XsdObject> objects) {
        this.serviceName = serviceName;
        this.inputFields = inputFields;
        this.outputFields = outputFields;
        this.objects = objects;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<XsdField> getInputFields() {
        return inputFields;
    }

    public void setInputFields(List<XsdField> inputFields) {
        this.inputFields = inputFields;
    }

    public List<XsdField> getOutputFields() {
        return outputFields;
    }

    public void setOutputFields(List<XsdField> outputFields) {
        this.outputFields = outputFields;
    }

    public List<XsdObject> getObjects() {
        return objects;
    }

    public void setObjects(List<XsdObject> objects) {
        this.objects = objects;
    }
}
