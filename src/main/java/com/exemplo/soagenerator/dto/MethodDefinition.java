package com.exemplo.soagenerator.dto;

public class MethodDefinition {
    private String name;
    private String path;
    private String description;
    private RequestDefinition request;
    private ResponseDefinition response;

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RequestDefinition getRequest() {
        return request;
    }

    public void setRequest(RequestDefinition request) {
        this.request = request;
    }

    public ResponseDefinition getResponse() {
        return response;
    }

    public void setResponse(ResponseDefinition response) {
        this.response = response;
    }
}
