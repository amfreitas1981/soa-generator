package com.exemplo.soagenerator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WadlMethod {
    @NotBlank
    @JsonProperty("name")
    private String name;

    @NotBlank
    @JsonProperty("path")
    private String path;

    @JsonProperty("description")
    private String description;

    @JsonProperty("request")
    private WadlRequestDetails request;

    @JsonProperty("response")
    private WadlResponseDetails response;

    // Getters e Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public WadlRequestDetails getRequest() { return request; }
    public void setRequest(WadlRequestDetails request) { this.request = request; }

    public WadlResponseDetails getResponse() { return response; }
    public void setResponse(WadlResponseDetails response) { this.response = response; }
}
