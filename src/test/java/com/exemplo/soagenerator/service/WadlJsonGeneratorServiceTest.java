package com.exemplo.soagenerator.service;

import com.exemplo.soagenerator.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WadlJsonGeneratorServiceTest {

    private WadlJsonGeneratorService generatorService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        generatorService = new WadlJsonGeneratorService();
    }

    @Test
    void testGenerateWadl_Success() {
        WadlField requestField = new WadlField();
        requestField.setName("username");
        requestField.setType("string");

        WadlField responseField = new WadlField();
        responseField.setName("message");
        responseField.setType("string");

        WadlRequestDetails requestDetails = new WadlRequestDetails();
        requestDetails.setFields(List.of(requestField));

        WadlResponseDetails responseDetails = new WadlResponseDetails();
        responseDetails.setFields(List.of(responseField));

        WadlMethod method = new WadlMethod();
        method.setName("POST");
        method.setPath("/login");
        method.setRequest(requestDetails);
        method.setResponse(responseDetails);

        WadlRequest request = new WadlRequest();
        request.setServiceName("AuthService");
        request.setBaseUri("https://api.example.com");
        request.setMethods(List.of(method));

        String result = generatorService.generateWadl(request);
        assertTrue(result.contains("AuthService.wadl"));
    }

    @Test
    void testGenerateWadl_InvalidRequest_Null() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            generatorService.generateWadl(null);
        });
        assertEquals("O nome do serviço não pode ser nulo ou vazio.", thrown.getMessage());
    }

    @Test
    void testGenerateWadl_InvalidRequest_EmptyServiceName() {
        WadlRequest request = new WadlRequest();
        request.setServiceName("");
        request.setBaseUri("https://api.example.com");
        request.setMethods(List.of(new WadlMethod()));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            generatorService.generateWadl(request);
        });
        assertEquals("O nome do serviço não pode ser nulo ou vazio.", thrown.getMessage());
    }

    @Test
    void testGenerateWadl_InvalidRequest_EmptyMethods() {
        WadlRequest request = new WadlRequest();
        request.setServiceName("TestService");
        request.setBaseUri("https://api.example.com");
        request.setMethods(List.of());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            generatorService.generateWadl(request);
        });
        assertEquals("A lista de métodos não pode ser nula ou vazia.", thrown.getMessage());
    }

    @Test
    void testGenerateMethodJson_Success() throws Exception {
        WadlMethod method = new WadlMethod();
        method.setName("GET");
        method.setPath("/users");

        String jsonResult = generatorService.generateMethodJson(method);
        assertNotNull(jsonResult);
        assertTrue(jsonResult.contains("\"name\" : \"GET\""));
    }

    @Test
    void testGenerateWadl_FileCreation() throws IOException {
        WadlRequest request = new WadlRequest();
        request.setServiceName("FileService");
        request.setBaseUri("https://api.example.com");
        request.setMethods(List.of(new WadlMethod()));

        File invalidFile = tempDir.resolve("invalidFile.wadl").toFile();
        invalidFile.createNewFile();
        invalidFile.setWritable(false); // Bloqueia permissões de escrita

        // Sobrescreve o método para utilizar o arquivo inválido
        WadlJsonGeneratorService generatorServiceWithInvalidFile = new WadlJsonGeneratorService() {
            @Override
            protected String saveWadlFile(String fileName, String content) {
                try (FileWriter writer = new FileWriter(invalidFile)) {
                    writer.write(content);
                } catch (IOException e) {
                    throw new RuntimeException("Erro ao salvar o arquivo WADL", e);
                }
                return invalidFile.getAbsolutePath();
            }
        };

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            generatorServiceWithInvalidFile.generateWadl(request);
        });

        assertTrue(thrown.getMessage().contains("Erro ao salvar o arquivo WADL"));
    }

    @Test
    void testValidateRequest_NullRequest() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            generatorService.generateWadl(null);
        });
        assertEquals("O nome do serviço não pode ser nulo ou vazio.", thrown.getMessage());
    }

    @Test
    void testValidateRequest_EmptyServiceName() {
        WadlRequest request = new WadlRequest();
        request.setServiceName("");
        request.setBaseUri("https://api.example.com");
        request.setMethods(List.of(new WadlMethod()));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            generatorService.generateWadl(request);
        });
        assertEquals("O nome do serviço não pode ser nulo ou vazio.", thrown.getMessage());
    }

    @Test
    void testValidateRequest_EmptyBaseUri() {
        WadlRequest request = new WadlRequest();
        request.setServiceName("TestService");
        request.setBaseUri("");
        request.setMethods(List.of(new WadlMethod()));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            generatorService.generateWadl(request);
        });
        assertEquals("O baseUri não pode ser nulo ou vazio.", thrown.getMessage());
    }

    @Test
    void testValidateRequest_EmptyMethods() {
        WadlRequest request = new WadlRequest();
        request.setServiceName("TestService");
        request.setBaseUri("https://api.example.com");
        request.setMethods(List.of());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            generatorService.generateWadl(request);
        });
        assertEquals("A lista de métodos não pode ser nula ou vazia.", thrown.getMessage());
    }

    @Test
    void testGenerateResourceMethod_Exception() {
        WadlMethod method = new WadlMethod();
        method.setName("GET");
        method.setPath("/users");

        WadlJsonGeneratorService generatorServiceWithException = new WadlJsonGeneratorService() {
            @Override
            protected String generateMethodJson(WadlMethod method) throws IOException {
                throw new IOException("Erro ao gerar JSON");
            }
        };

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            generatorServiceWithException.generateResourceMethod(method);
        });

        assertEquals("Erro ao gerar estrutura JSON do método: Erro ao gerar JSON", thrown.getMessage());
    }

    @Test
    void testAddRequestToJson_WithParameters() {
        WadlRequestDetails requestDetails = new WadlRequestDetails();
        WadlParameter parameter = new WadlParameter();
        parameter.setName("id");
        parameter.setType("int");
        parameter.setRequired(true);
        requestDetails.setParameters(List.of(parameter));

        ObjectNode methodNode = new ObjectMapper().createObjectNode();
        generatorService.addRequestToJson(methodNode.putObject("method"), requestDetails);

        assertNotNull(methodNode.get("method").get("request").get("param"));
        assertEquals("id", methodNode.get("method").get("request").get("param").get("name").asText());
    }

    @Test
    void testAddResponseToJson_SingleField() {
        WadlResponseDetails responseDetails = new WadlResponseDetails();
        WadlField field = new WadlField();
        field.setName("message");
        field.setType("string");
        responseDetails.setFields(List.of(field));

        ObjectNode methodNode = new ObjectMapper().createObjectNode();
        generatorService.addResponseToJson(methodNode.putObject("method"), responseDetails);

        assertNotNull(methodNode.get("method").get("response").get("representation").get("param"));
        assertEquals("message", methodNode.get("method").get("response").get("representation").get("param").get("name").asText());
    }

    @Test
    void testAddResponseToJson_MultipleFields() {
        WadlResponseDetails responseDetails = new WadlResponseDetails();
        WadlField field1 = new WadlField();
        field1.setName("message");
        field1.setType("string");
        WadlField field2 = new WadlField();
        field2.setName("code");
        field2.setType("int");
        responseDetails.setFields(List.of(field1, field2));

        ObjectNode methodNode = new ObjectMapper().createObjectNode();
        generatorService.addResponseToJson(methodNode.putObject("method"), responseDetails);

        assertNotNull(methodNode.get("method").get("response").get("representation").get("param"));
        assertEquals(2, methodNode.get("method").get("response").get("representation").get("param").size());
    }

    @Test
    void testSaveWadlFile_Exception() throws IOException {
        String fileName = "invalidFile.wadl";
        String content = "<application></application>";
        File invalidFile = tempDir.resolve(fileName).toFile();
        invalidFile.createNewFile();
        invalidFile.setWritable(false); // Bloqueia permissões de escrita

        WadlJsonGeneratorService generatorServiceWithInvalidFile = new WadlJsonGeneratorService() {
            @Override
            protected String saveWadlFile(String fileName, String content) {
                try (FileWriter writer = new FileWriter(invalidFile)) {
                    writer.write(content);
                } catch (IOException e) {
                    throw new RuntimeException("Erro ao salvar o arquivo WADL: " + e.getMessage(), e);
                }
                return invalidFile.getAbsolutePath();
            }
        };

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            generatorServiceWithInvalidFile.saveWadlFile(fileName, content);
        });

        assertTrue(thrown.getMessage().contains("Erro ao salvar o arquivo WADL"));
    }
}
