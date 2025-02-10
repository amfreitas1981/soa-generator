package com.exemplo.soagenerator.service;

import com.exemplo.soagenerator.dto.WadlField;
import com.exemplo.soagenerator.dto.WadlMethod;
import com.exemplo.soagenerator.dto.WadlParameter;
import com.exemplo.soagenerator.dto.WadlRequest;
import com.exemplo.soagenerator.dto.WadlRequestDetails;
import com.exemplo.soagenerator.dto.WadlResponseDetails;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class WadlGeneratorService {

    private static final String OUTPUT_DIRECTORY = "generated_wadl/";

    public String generateWadl(WadlRequest request) {
        validateRequest(request);

        String fileName = request.getServiceName().trim() + ".wadl";
        StringBuilder wadlContent = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        wadlContent.append("<application xmlns=\"http://wadl.dev.java.net/2009/02\">\n");
        wadlContent.append("    <resources base=\"").append(request.getBaseUri()).append("\">\n");

        for (WadlMethod method : request.getMethods()) {
            wadlContent.append(generateResourceMethod(method));
        }

        wadlContent.append("    </resources>\n");
        wadlContent.append("</application>");

        return saveWadlFile(fileName, wadlContent.toString());
    }

    private void validateRequest(WadlRequest request) {
        if (request == null || request.getServiceName() == null || request.getServiceName().isEmpty()) {
            throw new IllegalArgumentException("O nome do serviço não pode ser nulo ou vazio.");
        }
        if (request.getBaseUri() == null || request.getBaseUri().isBlank()) {
            throw new IllegalArgumentException("O baseUri não pode ser nulo ou vazio.");
        }
        if (request.getMethods() == null || request.getMethods().isEmpty()) {
            throw new IllegalArgumentException("A lista de métodos não pode ser nula ou vazia.");
        }
    }

    String generateResourceMethod(WadlMethod method) {
        StringBuilder methodContent = new StringBuilder();
        methodContent.append("        <resource path=\"").append(method.getPath()).append("\">\n")
                .append("            <method name=\"").append(method.getName()).append("\">\n");

        // Adiciona request se existir
        if (method.getRequest() != null) {
            methodContent.append(generateRequest(method.getRequest()));
        }

        // Adiciona response se existir
        if (method.getResponse() != null) {
            methodContent.append(generateResponse(method.getResponse()));
        }

        methodContent.append("            </method>\n")
                .append("        </resource>\n");

        return methodContent.toString();
    }

    String generateRequest(WadlRequestDetails request) {
        StringBuilder requestContent = new StringBuilder("                <request>\n");

        // Adiciona parâmetros da URL/template se existirem
        if (request.getParameters() != null && !request.getParameters().isEmpty()) {
            for (WadlParameter param : request.getParameters()) {
                requestContent.append(generateParameter(param, "template"));
            }
        }

        // Adiciona representation se houver campos no body
        if (request.getFields() != null && !request.getFields().isEmpty()) {
            requestContent.append("                    <representation mediaType=\"application/json\">\n");
            for (WadlField field : request.getFields()) {
                requestContent.append(generateField(field));
            }
            requestContent.append("                    </representation>\n");
        }

        requestContent.append("                </request>\n");
        return requestContent.toString();
    }

    String generateResponse(WadlResponseDetails response) {
        StringBuilder responseContent = new StringBuilder("                <response>\n")
                .append("                    <representation mediaType=\"application/json\">\n");

        if (response.getFields() != null) {
            for (WadlField field : response.getFields()) {
                responseContent.append(generateField(field));
            }
        }

        responseContent.append("                    </representation>\n")
                .append("                </response>\n");

        return responseContent.toString();
    }

    String generateParameter(WadlParameter param, String style) {
        return String.format("                    <param name=\"%s\" style=\"%s\" type=\"%s\" required=\"%s\"/>\n",
                param.getName(),
                style,
                param.getType() != null ? param.getType() : "xs:string",
                param.isRequired());
    }

    String generateField(WadlField field) {
        return String.format("                        <param name=\"%s\" style=\"plain\"%s/>\n",
                field.getName(),
                field.getType() != null ? " type=\"" + field.getType() + "\"" : "");
    }

    String saveWadlFile(String fileName, String content) {
        File directory = new File(OUTPUT_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File wadlFile = new File(OUTPUT_DIRECTORY + fileName);
        try (FileWriter writer = new FileWriter(wadlFile)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo WADL: " + e.getMessage(), e);
        }

        return "Arquivo WADL gerado com sucesso: " + wadlFile.getAbsolutePath();
    }
}
