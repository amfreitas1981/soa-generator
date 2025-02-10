package com.exemplo.soagenerator.service;

import com.exemplo.soagenerator.dto.FieldDefinition;
import com.exemplo.soagenerator.dto.WsdlRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class WsdlGeneratorServiceTest {
    private WsdlGeneratorService wsdlGeneratorService;
    private static final String TEST_SERVICE_NAME = "TestService";

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        wsdlGeneratorService = new WsdlGeneratorService();
        System.setProperty("user.dir", tempDir.toString());
    }

    @AfterEach
    void cleanup() throws IOException {
        File directory = new File("generated_wsdl");
        if (directory.exists()) {
            Files.walk(directory.toPath())
                    .sorted((a, b) -> b.compareTo(a))
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    @Test
    void testGenerateWsdlWithIOError() {
        // Arrange
        WsdlRequest request = new WsdlRequest();
        request.setServiceName("Test/Invalid*Name"); // Nome inválido para causar erro de IO

        // Act
        String result = wsdlGeneratorService.generateWsdl(request);

        // Assert
        assertTrue(result.startsWith("Erro ao gerar WSDL:"));
    }

    @Test
    void testGenerateWsdlWithNestedComplexTypes() {
        // Arrange
        // Campo do endereço (nível mais interno)
        FieldDefinition streetField = new FieldDefinition();
        streetField.setName("street");
        streetField.setType("string");
        streetField.setRequired(true);

        // Campo de contato (nível intermediário)
        FieldDefinition addressField = new FieldDefinition();
        addressField.setName("address");
        addressField.setType("Address");
        addressField.setObject(true);
        addressField.setRequired(true);
        addressField.setFields(Collections.singletonList(streetField));

        // Campo de pessoa (nível mais externo)
        FieldDefinition personField = new FieldDefinition();
        personField.setName("person");
        personField.setType("Person");
        personField.setObject(true);
        personField.setRequired(true);
        personField.setFields(Collections.singletonList(addressField));

        WsdlRequest request = new WsdlRequest();
        request.setServiceName(TEST_SERVICE_NAME);
        request.setInputFields(Collections.singletonList(personField));

        // Act
        String result = wsdlGeneratorService.generateWsdl(request);

        // Assert
        File wsdlFile = new File(result);
        try {
            String content = new String(Files.readAllBytes(wsdlFile.toPath()));
            assertTrue(content.contains("complexType name=\"PersonType\""));
            assertTrue(content.contains("complexType name=\"AddressType\""));
            assertTrue(content.contains("type=\"tns:AddressType\""));
            assertTrue(content.contains("type=\"tns:PersonType\""));
        } catch (IOException e) {
            fail("Error reading WSDL file: " + e.getMessage());
        }
    }

    @Test
    void testDefaultAndNullTypeHandling() {
        // Arrange
        // Campo com tipo null
        FieldDefinition nullTypeField = new FieldDefinition();
        nullTypeField.setName("nullField");
        nullTypeField.setType(null);
        nullTypeField.setRequired(true);

        // Campo com tipo desconhecido
        FieldDefinition unknownTypeField = new FieldDefinition();
        unknownTypeField.setName("unknownField");
        unknownTypeField.setType("unknownType");
        unknownTypeField.setRequired(true);

        WsdlRequest request = new WsdlRequest();
        request.setServiceName(TEST_SERVICE_NAME);
        request.setInputFields(Arrays.asList(nullTypeField, unknownTypeField));

        // Act
        String result = wsdlGeneratorService.generateWsdl(request);

        // Assert
        File wsdlFile = new File(result);
        try {
            String content = new String(Files.readAllBytes(wsdlFile.toPath()));
            assertTrue(content.contains("name=\"nullField\" type=\"xsd:string\""));
            assertTrue(content.contains("name=\"unknownField\" type=\"xsd:string\""));
        } catch (IOException e) {
            fail("Error reading WSDL file: " + e.getMessage());
        }
    }

    @Test
    void testObjectTypeFieldHandling() {
        // Arrange
        FieldDefinition objectField = new FieldDefinition();
        objectField.setName("customObject");
        objectField.setType("CustomType");
        objectField.setObject(true);
        objectField.setRequired(true);

        WsdlRequest request = new WsdlRequest();
        request.setServiceName(TEST_SERVICE_NAME);
        request.setInputFields(Collections.singletonList(objectField));

        // Act
        String result = wsdlGeneratorService.generateWsdl(request);

        // Assert
        File wsdlFile = new File(result);
        try {
            String content = new String(Files.readAllBytes(wsdlFile.toPath()));
            assertTrue(content.contains("type=\"tns:CustomTypeType\""));
        } catch (IOException e) {
            fail("Error reading WSDL file: " + e.getMessage());
        }
    }

    @Test
    void testGenerateWsdlWithSimpleTypes() {
        // Arrange
        FieldDefinition input = new FieldDefinition();
        input.setName("name");
        input.setType("string");
        input.setRequired(true);

        FieldDefinition output = new FieldDefinition();
        output.setName("response");
        output.setType("boolean");
        output.setRequired(true);

        WsdlRequest request = new WsdlRequest();
        request.setServiceName(TEST_SERVICE_NAME);
        request.setInputFields(Collections.singletonList(input));
        request.setOutputFields(Collections.singletonList(output));

        // Act
        String result = wsdlGeneratorService.generateWsdl(request);

        // Assert
        assertTrue(result.contains(TEST_SERVICE_NAME + ".wsdl"));
        File wsdlFile = new File(result);
        assertTrue(wsdlFile.exists());

        try {
            String content = new String(Files.readAllBytes(wsdlFile.toPath()));
            assertTrue(content.contains("type=\"xsd:string\""));
            assertTrue(content.contains("type=\"xsd:boolean\""));
            assertTrue(content.contains("TestServiceRequest"));
            assertTrue(content.contains("TestServiceResponse"));
        } catch (IOException e) {
            fail("Error reading WSDL file: " + e.getMessage());
        }
    }

    @Test
    void testGenerateWsdlWithComplexTypes() {
        // Arrange
        FieldDefinition addressField = new FieldDefinition();
        addressField.setName("street");
        addressField.setType("string");
        addressField.setRequired(true);

        FieldDefinition personField = new FieldDefinition();
        personField.setName("person");
        personField.setType("Person");
        personField.setObject(true);
        personField.setRequired(true);
        personField.setFields(Collections.singletonList(addressField));

        WsdlRequest request = new WsdlRequest();
        request.setServiceName(TEST_SERVICE_NAME);
        request.setInputFields(Collections.singletonList(personField));

        // Act
        String result = wsdlGeneratorService.generateWsdl(request);

        // Assert
        assertTrue(result.contains(TEST_SERVICE_NAME + ".wsdl"));
        File wsdlFile = new File(result);
        assertTrue(wsdlFile.exists());

        try {
            String content = new String(Files.readAllBytes(wsdlFile.toPath()));
            assertTrue(content.contains("complexType name=\"PersonType\""));
            assertTrue(content.contains("element name=\"street\""));
        } catch (IOException e) {
            fail("Error reading WSDL file: " + e.getMessage());
        }
    }

    @Test
    void testGenerateWsdlWithAllDataTypes() {
        // Arrange
        List<FieldDefinition> fields = Arrays.asList(
                createField("stringField", "string", true),
                createField("intField", "int", true),
                createField("longField", "long", true),
                createField("booleanField", "boolean", true),
                createField("doubleField", "double", true),
                createField("floatField", "float", true),
                createField("dateField", "date", true),
                createField("dateTimeField", "datetime", true),
                createField("decimalField", "bigdecimal", true)
        );

        WsdlRequest request = new WsdlRequest();
        request.setServiceName(TEST_SERVICE_NAME);
        request.setInputFields(fields);

        // Act
        String result = wsdlGeneratorService.generateWsdl(request);

        // Assert
        File wsdlFile = new File(result);
        try {
            String content = new String(Files.readAllBytes(wsdlFile.toPath()));
            assertTrue(content.contains("xsd:string"));
            assertTrue(content.contains("xsd:int"));
            assertTrue(content.contains("xsd:long"));
            assertTrue(content.contains("xsd:boolean"));
            assertTrue(content.contains("xsd:double"));
            assertTrue(content.contains("xsd:float"));
            assertTrue(content.contains("xsd:date"));
            assertTrue(content.contains("xsd:dateTime"));
            assertTrue(content.contains("xsd:decimal"));
        } catch (IOException e) {
            fail("Error reading WSDL file: " + e.getMessage());
        }
    }

    @Test
    void testTernaryTypeSelection() {
        // Arrange
        // Campo objeto
        FieldDefinition objectField = new FieldDefinition();
        objectField.setName("person");
        objectField.setType("Person");
        objectField.setObject(true);
        objectField.setRequired(true);

        // Campo simples
        FieldDefinition simpleField = new FieldDefinition();
        simpleField.setName("name");
        simpleField.setType("string");
        simpleField.setRequired(true);

        List<FieldDefinition> fields = Arrays.asList(objectField, simpleField);

        WsdlRequest request = new WsdlRequest();
        request.setServiceName(TEST_SERVICE_NAME);
        request.setInputFields(fields);

        // Act
        String result = wsdlGeneratorService.generateWsdl(request);

        // Assert
        File wsdlFile = new File(result);
        try {
            String content = new String(Files.readAllBytes(wsdlFile.toPath()));
            // Verifica o tipo do campo objeto (parte do operador ternário antes do ':')
            assertTrue(content.contains("type=\"tns:PersonType\""),
                    "Should contain object type reference");
            // Verifica o tipo do campo simples (parte do operador ternário depois do ':')
            assertTrue(content.contains("type=\"xsd:string\""),
                    "Should contain simple type reference");
        } catch (IOException e) {
            fail("Error reading WSDL file: " + e.getMessage());
        }
    }

    private FieldDefinition createField(String name, String type, boolean required) {
        FieldDefinition field = new FieldDefinition();
        field.setName(name);
        field.setType(type);
        field.setRequired(required);
        return field;
    }
}