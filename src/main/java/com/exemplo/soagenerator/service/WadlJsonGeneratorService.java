package com.exemplo.soagenerator.service;

import com.exemplo.soagenerator.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class WadlJsonGeneratorService {

    private static final String OUTPUT_DIRECTORY = "generated_wadl/";
    private final ObjectMapper objectMapper = new ObjectMapper();

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

    private String generateResourceMethod(WadlMethod method) {
        StringBuilder methodContent = new StringBuilder();
        methodContent.append("        <resource path=\"").append(method.getPath()).append("\">\n");

        // Gera a estrutura JSON do método
        try {
            String jsonStructure = generateMethodJson(method);
            // Adiciona indentação correta para o JSON
            String indentedJson = jsonStructure.replace("\n", "\n            ");
            methodContent.append("            ").append(indentedJson).append("\n");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar estrutura JSON do método: " + e.getMessage(), e);
        }

        methodContent.append("        </resource>\n");
        return methodContent.toString();
    }

    private String generateMethodJson(WadlMethod method) throws IOException {
        ObjectNode rootNode = objectMapper.createObjectNode();
        ObjectNode methodNode = rootNode.putObject("method");

        // Adiciona o nome do método
        methodNode.put("name", method.getName());

        // Adiciona request se existir
        if (method.getRequest() != null) {
            addRequestToJson(methodNode, method.getRequest());
        }

        // Adiciona response se existir
        if (method.getResponse() != null) {
            addResponseToJson(methodNode, method.getResponse());
        }

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }

    private void addRequestToJson(ObjectNode methodNode, WadlRequestDetails request) {
        ObjectNode requestNode = methodNode.putObject("request");

        // Adiciona parâmetros da URL
        if (request.getParameters() != null && !request.getParameters().isEmpty()) {
            ObjectNode paramNode = requestNode.putObject("param");
            WadlParameter param = request.getParameters().get(0);
            paramNode.put("name", param.getName());
            paramNode.put("style", "template");
            paramNode.put("type", param.getType());
            paramNode.put("required", String.valueOf(param.isRequired()));
        }

        // Adiciona campos do body se existirem
        if (request.getFields() != null && !request.getFields().isEmpty()) {
            ObjectNode representationNode = requestNode.putObject("representation");
            representationNode.put("mediaType", "application/json");

            ArrayNode paramsArray = representationNode.putArray("param");
            for (WadlField field : request.getFields()) {
                ObjectNode fieldNode = paramsArray.addObject();
                fieldNode.put("name", field.getName());
                fieldNode.put("style", "plain");
                fieldNode.put("type", field.getType());
            }
        }
    }

    private void addResponseToJson(ObjectNode methodNode, WadlResponseDetails response) {
        ObjectNode responseNode = methodNode.putObject("response");
        ObjectNode representationNode = responseNode.putObject("representation");
        representationNode.put("mediaType", "application/json");

        if (response.getFields() != null && !response.getFields().isEmpty()) {
            if (response.getFields().size() == 1) {
                // Se houver apenas um campo, coloca como objeto único
                ObjectNode paramNode = representationNode.putObject("param");
                WadlField field = response.getFields().get(0);
                paramNode.put("name", field.getName());
                paramNode.put("style", "plain");
                paramNode.put("type", field.getType());
            } else {
                // Se houver múltiplos campos, coloca como array
                ArrayNode paramsArray = representationNode.putArray("param");
                for (WadlField field : response.getFields()) {
                    ObjectNode fieldNode = paramsArray.addObject();
                    fieldNode.put("name", field.getName());
                    fieldNode.put("style", "plain");
                    fieldNode.put("type", field.getType());
                }
            }
        }
    }

    private String saveWadlFile(String fileName, String content) {
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
