package com.exemplo.soagenerator.service;

import com.exemplo.soagenerator.dto.FieldDefinition;
import com.exemplo.soagenerator.dto.WsdlRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class WsdlGeneratorService {

    private static final String OUTPUT_DIRECTORY = "generated_wsdl/";
    private Set<String> processedTypes = new HashSet<>();

    public String generateWsdl(WsdlRequest request) {
        String serviceName = request.getServiceName();
        String fileName = OUTPUT_DIRECTORY + serviceName + ".wsdl";

        File directory = new File(OUTPUT_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Limpa tipos processados para cada nova geração
        processedTypes.clear();

        StringBuilder wsdlBuilder = new StringBuilder();
        wsdlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        wsdlBuilder.append("<definitions name=\"" + serviceName + "\" ");
        wsdlBuilder.append("targetNamespace=\"http://exemplo.com/" + serviceName + "\" ");
        wsdlBuilder.append("xmlns:tns=\"http://exemplo.com/" + serviceName + "\" ");
        wsdlBuilder.append("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" ");
        wsdlBuilder.append("xmlns:soap=\"http://schemas.xmlsoap.org/wsdl/soap/\" ");
        wsdlBuilder.append("xmlns=\"http://schemas.xmlsoap.org/wsdl/\">\n");

        // Tipos
        wsdlBuilder.append("    <types>\n");
        wsdlBuilder.append("        <xsd:schema targetNamespace=\"http://exemplo.com/" + serviceName + "\">\n");

        // Processamento de tipos complexos para input e output
        processComplexTypesForFields(wsdlBuilder, request.getInputFields());
        processComplexTypesForFields(wsdlBuilder, request.getOutputFields());

        // Elementos de request e response
        generateRequestResponseElements(wsdlBuilder, request);

        wsdlBuilder.append("        </xsd:schema>\n");
        wsdlBuilder.append("    </types>\n");

        // Mensagens
        wsdlBuilder.append("    <message name=\"" + serviceName + "Request\">\n");
        wsdlBuilder.append("        <part name=\"parameters\" element=\"tns:" + serviceName + "Request\"/>\n");
        wsdlBuilder.append("    </message>\n");

        wsdlBuilder.append("    <message name=\"" + serviceName + "Response\">\n");
        wsdlBuilder.append("        <part name=\"parameters\" element=\"tns:" + serviceName + "Response\"/>\n");
        wsdlBuilder.append("    </message>\n");

        // Restante do WSDL (portType, binding, service) permanece igual
        appendWsdlStructure(wsdlBuilder, serviceName);

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(wsdlBuilder.toString());
        } catch (IOException e) {
            return "Erro ao gerar WSDL: " + e.getMessage();
        }

        return fileName;
    }

    private void processComplexTypesForFields(StringBuilder wsdlBuilder, List<FieldDefinition> fields) {
        if (fields == null) return;

        for (FieldDefinition field : fields) {
            if (field.isObject() && !processedTypes.contains(field.getType())) {
                generateComplexType(wsdlBuilder, field);
            }
        }
    }

    private void generateComplexType(StringBuilder wsdlBuilder, FieldDefinition field) {
        if (processedTypes.contains(field.getType())) return;

        wsdlBuilder.append("            <xsd:complexType name=\"" + field.getType() + "Type\">\n");
        wsdlBuilder.append("                <xsd:sequence>\n");

        // Processa campos do objeto
        if (field.getFields() != null) {
            for (FieldDefinition subField : field.getFields()) {
                String type;

                if (subField.isObject()) {
                    // Recursivamente processa tipos complexos aninhados
                    if (!processedTypes.contains(subField.getType())) {
                        generateComplexType(wsdlBuilder, subField);
                    }
                    type = "tns:" + subField.getType() + "Type";
                } else {
                    type = mapJavaTypeToXsd(subField.getType());
                }

                // Adiciona elemento
                String minOccurs = subField.isRequired() ? "1" : "0";
                String maxOccurs = subField.isList() ? "unbounded" : "1";

                wsdlBuilder.append("                    <xsd:element name=\"" + subField.getName() +
                        "\" type=\"" + type +
                        "\" minOccurs=\"" + minOccurs +
                        "\" maxOccurs=\"" + maxOccurs + "\"/>\n");
            }
        }

        wsdlBuilder.append("                </xsd:sequence>\n");
        wsdlBuilder.append("            </xsd:complexType>\n");

        // Marca tipo como processado
        processedTypes.add(field.getType());
    }

    private void generateRequestResponseElements(StringBuilder wsdlBuilder, WsdlRequest request) {
        // Elemento de Request
        wsdlBuilder.append("            <xsd:element name=\"" + request.getServiceName() + "Request\">\n");
        wsdlBuilder.append("                <xsd:complexType>\n");
        wsdlBuilder.append("                    <xsd:sequence>\n");

        // Campos de entrada
        if (request.getInputFields() != null) {
            for (FieldDefinition field : request.getInputFields()) {
                String type = field.isObject() ?
                        "tns:" + field.getType() + "Type" :
                        mapJavaTypeToXsd(field.getType());

                String minOccurs = field.isRequired() ? "1" : "0";
                String maxOccurs = field.isList() ? "unbounded" : "1";

                wsdlBuilder.append("                        <xsd:element name=\"" + field.getName() +
                        "\" type=\"" + type +
                        "\" minOccurs=\"" + minOccurs +
                        "\" maxOccurs=\"" + maxOccurs + "\"/>\n");
            }
        }

        wsdlBuilder.append("                    </xsd:sequence>\n");
        wsdlBuilder.append("                </xsd:complexType>\n");
        wsdlBuilder.append("            </xsd:element>\n");

        // Elemento de Response
        wsdlBuilder.append("            <xsd:element name=\"" + request.getServiceName() + "Response\">\n");
        wsdlBuilder.append("                <xsd:complexType>\n");
        wsdlBuilder.append("                    <xsd:sequence>\n");

        // Campos de saída
        if (request.getOutputFields() != null) {
            for (FieldDefinition field : request.getOutputFields()) {
                String type = field.isObject() ?
                        "tns:" + field.getType() + "Type" :
                        mapJavaTypeToXsd(field.getType());

                String minOccurs = field.isRequired() ? "1" : "0";
                String maxOccurs = field.isList() ? "unbounded" : "1";

                wsdlBuilder.append("                        <xsd:element name=\"" + field.getName() +
                        "\" type=\"" + type +
                        "\" minOccurs=\"" + minOccurs +
                        "\" maxOccurs=\"" + maxOccurs + "\"/>\n");
            }
        }

        wsdlBuilder.append("                    </xsd:sequence>\n");
        wsdlBuilder.append("                </xsd:complexType>\n");
        wsdlBuilder.append("            </xsd:element>\n");
    }

    private void appendWsdlStructure(StringBuilder wsdlBuilder, String serviceName) {
        // Port Type
        wsdlBuilder.append("    <portType name=\"" + serviceName + "PortType\">\n");
        wsdlBuilder.append("        <operation name=\"" + serviceName + "\">\n");
        wsdlBuilder.append("            <input message=\"tns:" + serviceName + "Request\"/>\n");
        wsdlBuilder.append("            <output message=\"tns:" + serviceName + "Response\"/>\n");
        wsdlBuilder.append("        </operation>\n");
        wsdlBuilder.append("    </portType>\n");

        // Binding
        wsdlBuilder.append("    <binding name=\"" + serviceName + "Binding\" type=\"tns:" + serviceName + "PortType\">\n");
        wsdlBuilder.append("        <soap:binding style=\"document\" transport=\"http://schemas.xmlsoap.org/soap/http\"/>\n");
        wsdlBuilder.append("        <operation name=\"" + serviceName + "\">\n");
        wsdlBuilder.append("            <soap:operation soapAction=\"http://exemplo.com/" + serviceName + "/" + serviceName + "\"/>\n");
        wsdlBuilder.append("            <input>\n");
        wsdlBuilder.append("                <soap:body use=\"literal\"/>\n");
        wsdlBuilder.append("            </input>\n");
        wsdlBuilder.append("            <output>\n");
        wsdlBuilder.append("                <soap:body use=\"literal\"/>\n");
        wsdlBuilder.append("            </output>\n");
        wsdlBuilder.append("        </operation>\n");
        wsdlBuilder.append("    </binding>\n");

        // Service
        wsdlBuilder.append("    <service name=\"" + serviceName + "Service\">\n");
        wsdlBuilder.append("        <port name=\"" + serviceName + "Port\" binding=\"tns:" + serviceName + "Binding\">\n");
        wsdlBuilder.append("            <soap:address location=\"http://localhost:8080/ws/" + serviceName + "\"/>\n");
        wsdlBuilder.append("        </port>\n");
        wsdlBuilder.append("    </service>\n");

        wsdlBuilder.append("</definitions>\n");
    }

    private String mapJavaTypeToXsd(String javaType) {
        if (javaType == null) {
            return "xsd:string";
        }

        switch (javaType.toLowerCase()) {
            case "string": return "xsd:string";
            case "int":
            case "integer": return "xsd:int";
            case "long": return "xsd:long";
            case "boolean": return "xsd:boolean";
            case "double": return "xsd:double";
            case "float": return "xsd:float";
            case "date":
            case "localdate": return "xsd:date";
            case "datetime":
            case "localdatetime": return "xsd:dateTime";
            case "bigdecimal": return "xsd:decimal";
            default: return "xsd:string";
        }
    }
}
