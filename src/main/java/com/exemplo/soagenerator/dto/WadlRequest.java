package com.exemplo.soagenerator.dto;

import java.util.List;

public class WadlRequest {
    private String serviceName;
    private List<String> methods;

    public WadlRequest() {
    }

    public WadlRequest(String serviceName, List<String> methods) {
        this.serviceName = serviceName;
        this.methods = methods;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }
}
