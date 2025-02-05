package com.exemplo.soagenerator.service;

import com.exemplo.soagenerator.dto.WadlRequest;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class WadlGeneratorService {

    private static final String OUTPUT_DIRECTORY = "generated_wadl/";

    public String generateWadl(WadlRequest request) {
        String serviceName = request.getServiceName();
        String fileName = OUTPUT_DIRECTORY + serviceName + ".wadl";

        File directory = new File(OUTPUT_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        StringBuilder wadlBuilder = new StringBuilder();
        wadlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        wadlBuilder.append("<application xmlns=\"http://wadl.dev.java.net/2009/02\">\n");
        wadlBuilder.append("    <resources base=\"http://localhost:8080/api/\">\n");
        wadlBuilder.append("        <resource path=\"" + serviceName.toLowerCase() + "\">\n");

        // Adiciona métodos suportados (GET, POST, PUT, DELETE)
        for (String method : List.of("GET", "POST", "PUT", "DELETE")) {
            wadlBuilder.append("            <method name=\"" + method + "\">\n");

            if (method.equals("POST") || method.equals("PUT")) {
                wadlBuilder.append("                <request>\n");
                wadlBuilder.append("                    <representation mediaType=\"application/json\">\n");
                wadlBuilder.append("                        <param name=\"body\" style=\"plain\" required=\"true\"/>\n");
                wadlBuilder.append("                    </representation>\n");
                wadlBuilder.append("                </request>\n");
            }

            wadlBuilder.append("                <response>\n");
            wadlBuilder.append("                    <representation mediaType=\"application/json\"/>\n");
            wadlBuilder.append("                </response>\n");

            wadlBuilder.append("            </method>\n");
        }

        wadlBuilder.append("        </resource>\n");
        wadlBuilder.append("    </resources>\n");
        wadlBuilder.append("</application>\n");

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(wadlBuilder.toString());
        } catch (IOException e) {
            return "Erro ao gerar WADL: " + e.getMessage();
        }

        return fileName;
    }
}
