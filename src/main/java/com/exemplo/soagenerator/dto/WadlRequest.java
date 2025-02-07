package com.exemplo.soagenerator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WadlRequest {

    @NotBlank
    @JsonProperty("serviceName")
    private String serviceName;

    @NotBlank
    @JsonProperty("baseUri")
    private String baseUri;

    @JsonProperty("methods")
    private List<WadlMethod> methods;

    // Getters e Setters
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getBaseUri() { return baseUri; }
    public void setBaseUri(String baseUri) { this.baseUri = baseUri; }

    public List<WadlMethod> getMethods() { return methods; }
    public void setMethods(List<WadlMethod> methods) { this.methods = methods; }
}
