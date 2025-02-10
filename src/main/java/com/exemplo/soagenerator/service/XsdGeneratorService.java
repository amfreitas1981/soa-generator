package com.exemplo.soagenerator.service;

import com.exemplo.soagenerator.dto.XsdField;
import com.exemplo.soagenerator.dto.XsdObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class XsdGeneratorService {

    private static final String BASE_DIRECTORY = "generated_xsd/";

    public static String generateAndSaveXsd(String serviceName, List<XsdField> inputFields, List<XsdField> outputFields, List<XsdObject> objects) throws IOException {
        String inputXsd = generateXsd(serviceName + "Request", inputFields, objects);
        String outputXsd = generateXsd(serviceName + "Response", outputFields, objects);

        saveXsdFile(serviceName + "Request.xsd", inputXsd);
        saveXsdFile(serviceName + "Response.xsd", outputXsd);

        return "Arquivos gerados: " + serviceName + "Request.xsd e " + serviceName + "Response.xsd";
    }

    private static String generateXsd(String elementName, List<XsdField> fields, List<XsdObject> objects) {
        StringBuilder xsd = new StringBuilder();

        xsd.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xsd.append("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n");

        if (objects != null) {
            for (XsdObject obj : objects) {
                xsd.append("    <xs:complexType name=\"").append(obj.getName()).append("\">\n");
                xsd.append("        <xs:sequence>\n");

                for (XsdField field : obj.getFields()) {
                    xsd.append("            <xs:element name=\"").append(field.getName()).append("\" ")
                            .append(field.isObject() ? "type=\"" + field.getType() + "\"" : "type=\"xs:" + mapType(field.getType()) + "\"")
                            .append(" minOccurs=\"").append(field.isRequired() ? "1" : "0").append("\" ")
                            .append(field.isList() ? "maxOccurs=\"unbounded\"" : "maxOccurs=\"1\"")
                            .append("/>\n");
                }

                xsd.append("        </xs:sequence>\n");
                xsd.append("    </xs:complexType>\n");
            }
        }

        xsd.append("    <xs:element name=\"").append(elementName).append("\">\n");
        xsd.append("        <xs:complexType>\n");
        xsd.append("            <xs:sequence>\n");

        for (XsdField field : fields) {
            xsd.append("                <xs:element name=\"").append(field.getName()).append("\" ")
                    .append(field.isObject() ? "type=\"" + field.getType() + "\"" : "type=\"xs:" + mapType(field.getType()) + "\"")
                    .append(" minOccurs=\"").append(field.isRequired() ? "1" : "0").append("\" ")
                    .append(field.isList() ? "maxOccurs=\"unbounded\"" : "maxOccurs=\"1\"")
                    .append("/>\n");
        }

        xsd.append("            </xs:sequence>\n");
        xsd.append("        </xs:complexType>\n");
        xsd.append("    </xs:element>\n");

        xsd.append("</xs:schema>");

        return xsd.toString();
    }

    private static void saveXsdFile(String fileName, String content) throws IOException {
        File directory = new File(BASE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(BASE_DIRECTORY + fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

    public static byte[] getFileContent(String fileName) throws IOException {
        Path filePath = Path.of(BASE_DIRECTORY + fileName);
        return Files.readAllBytes(filePath);
    }

    private static String mapType(String javaType) {
        return switch (javaType.toLowerCase()) {
            case "int", "integer" -> "int";
            case "long" -> "long";
            case "boolean" -> "boolean";
            case "double" -> "double";
            default -> "string";
        };
    }
}
