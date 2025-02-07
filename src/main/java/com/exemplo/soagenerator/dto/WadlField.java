package com.exemplo.soagenerator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WadlField {

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private String type;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
