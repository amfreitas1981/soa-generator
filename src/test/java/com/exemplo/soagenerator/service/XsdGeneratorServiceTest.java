package com.exemplo.soagenerator.service;

import com.exemplo.soagenerator.dto.XsdField;
import com.exemplo.soagenerator.dto.XsdObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class XsdGeneratorServiceTest {
    private static final String TEST_SERVICE_NAME = "TestService";

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        System.setProperty("user.dir", tempDir.toString());
    }

    @AfterEach
    void cleanup() throws IOException {
        // Limpar arquivos gerados após cada teste
        File directory = new File("generated_xsd");
        if (directory.exists()) {
            Files.walk(directory.toPath())
                    .sorted((a, b) -> b.compareTo(a))
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    @Test
    void testGenerateAndSaveXsd() throws IOException {
        // Arrange
        List<XsdField> inputFields = Arrays.asList(
                new XsdField("name", "string", true, false, false),
                new XsdField("age", "int", true, false, false)
        );

        List<XsdField> outputFields = Arrays.asList(
                new XsdField("id", "long", true, false, false),
                new XsdField("success", "boolean", true, false, false)
        );

        // Act
        String result = XsdGeneratorService.generateAndSaveXsd(
                TEST_SERVICE_NAME,
                inputFields,
                outputFields,
                Collections.emptyList()
        );

        // Assert
        assertTrue(result.contains("TestServiceRequest.xsd"));
        assertTrue(result.contains("TestServiceResponse.xsd"));

        // Verificar se os arquivos foram criados
        assertTrue(Files.exists(Path.of("generated_xsd/TestServiceRequest.xsd")));
        assertTrue(Files.exists(Path.of("generated_xsd/TestServiceResponse.xsd")));

        // Verificar conteúdo dos arquivos
        String requestContent = new String(XsdGeneratorService.getFileContent("TestServiceRequest.xsd"));
        String responseContent = new String(XsdGeneratorService.getFileContent("TestServiceResponse.xsd"));

        assertTrue(requestContent.contains("name=\"name\" type=\"xs:string\""));
        assertTrue(requestContent.contains("name=\"age\" type=\"xs:int\""));
        assertTrue(responseContent.contains("name=\"id\" type=\"xs:long\""));
        assertTrue(responseContent.contains("name=\"success\" type=\"xs:boolean\""));
    }

    @Test
    void testGenerateXsdWithComplexTypes() throws IOException {
        // Arrange
        XsdField addressField = new XsdField("address", "Address", true, true, false);
        List<XsdField> personFields = Arrays.asList(
                new XsdField("street", "string", true, false, false),
                new XsdField("number", "int", true, false, false)
        );
        XsdObject addressObject = new XsdObject("Address", personFields);

        // Act
        String result = XsdGeneratorService.generateAndSaveXsd(
                TEST_SERVICE_NAME,
                Collections.singletonList(addressField),
                Collections.emptyList(),
                Collections.singletonList(addressObject)
        );

        // Assert
        String requestContent = new String(XsdGeneratorService.getFileContent("TestServiceRequest.xsd"));
        assertTrue(requestContent.contains("<xs:complexType name=\"Address\">"));
        assertTrue(requestContent.contains("name=\"address\" type=\"Address\""));
    }

    @Test
    void testGenerateXsdWithLists() throws IOException {
        // Arrange
        XsdField phoneNumbers = new XsdField("phoneNumbers", "string", false, false, true);

        // Act
        String result = XsdGeneratorService.generateAndSaveXsd(
                TEST_SERVICE_NAME,
                Collections.singletonList(phoneNumbers),
                Collections.emptyList(),
                Collections.emptyList()
        );

        // Assert
        String requestContent = new String(XsdGeneratorService.getFileContent("TestServiceRequest.xsd"));
        assertTrue(requestContent.contains("maxOccurs=\"unbounded\""));
    }

    @Test
    void testGenerateXsdWithDoubleType() throws IOException {
        // Arrange
        List<XsdField> inputFields = Arrays.asList(
                new XsdField("price", "double", true, false, false),
                new XsdField("quantity", "double", true, false, false)
        );

        // Act
        String result = XsdGeneratorService.generateAndSaveXsd(
                TEST_SERVICE_NAME,
                inputFields,
                Collections.emptyList(),
                Collections.emptyList()
        );

        // Assert
        String requestContent = new String(XsdGeneratorService.getFileContent("TestServiceRequest.xsd"));
        assertTrue(requestContent.contains("name=\"price\" type=\"xs:double\""));
        assertTrue(requestContent.contains("name=\"quantity\" type=\"xs:double\""));
    }
}
