package com.exemplo.soagenerator.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "schema")
@XmlAccessorType(XmlAccessType.FIELD)
public class XsdSchema {

    @XmlElement(name = "serviceName")
    private String serviceName;

    @XmlElement(name = "inputFields")
    private List<String> inputFields;

    @XmlElement(name = "outputFields")
    private List<String> outputFields;

    public XsdSchema() {}

    public XsdSchema(String serviceName, List<String> inputFields, List<String> outputFields) {
        this.serviceName = serviceName;
        this.inputFields = inputFields;
        this.outputFields = outputFields;
    }
}
