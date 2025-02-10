package com.exemplo.soagenerator.service;

import com.exemplo.soagenerator.dto.WadlField;
import com.exemplo.soagenerator.dto.WadlMethod;
import com.exemplo.soagenerator.dto.WadlParameter;
import com.exemplo.soagenerator.dto.WadlRequest;
import com.exemplo.soagenerator.dto.WadlRequestDetails;
import com.exemplo.soagenerator.dto.WadlResponseDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockConstruction;

class WadlGeneratorServiceTest {

    @InjectMocks
    private WadlGeneratorService wadlGeneratorService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateRequest_ShouldThrowException_WhenRequestIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> wadlGeneratorService.generateWadl(null));
        assertThrows(IllegalArgumentException.class, () -> wadlGeneratorService.generateWadl(new WadlRequest()));

        WadlRequest request = new WadlRequest();
        request.setServiceName("");
        request.setBaseUri("http://example.com");
        request.setMethods(List.of(new WadlMethod()));

        assertThrows(IllegalArgumentException.class, () -> wadlGeneratorService.generateWadl(request));

        request.setServiceName("TestService");
        request.setBaseUri(null);
        assertThrows(IllegalArgumentException.class, () -> wadlGeneratorService.generateWadl(request));

        request.setBaseUri("http://example.com");
        request.setMethods(null);
        assertThrows(IllegalArgumentException.class, () -> wadlGeneratorService.generateWadl(request));
    }

    @Test
    void testGenerateResourceMethod_ShouldReturnValidXml() {
        WadlMethod method = new WadlMethod();
        method.setName("GET");
        method.setPath("/test");

        String xml = wadlGeneratorService.generateResourceMethod(method);
        assertTrue(xml.contains("<resource path=\"/test\">"));
        assertTrue(xml.contains("<method name=\"GET\">"));
    }

    @Test
    void testGenerateRequest_ShouldReturnValidXml() {
        WadlRequestDetails requestDetails = new WadlRequestDetails();
        WadlParameter param = new WadlParameter();
        param.setName("id");
        param.setType("xs:int");
        param.setRequired(true);
        requestDetails.setParameters(Collections.singletonList(param));

        WadlField field = new WadlField();
        field.setName("name");
        field.setType("xs:string");
        requestDetails.setFields(Collections.singletonList(field));

        String xml = wadlGeneratorService.generateRequest(requestDetails);
        assertTrue(xml.contains("<param name=\"id\" style=\"template\" type=\"xs:int\" required=\"true\"/>"));
        assertTrue(xml.contains("<param name=\"name\" style=\"plain\" type=\"xs:string\"/>"));
    }

    @Test
    void testGenerateResponse_ShouldReturnValidXml() {
        WadlResponseDetails responseDetails = new WadlResponseDetails();
        WadlField field = new WadlField();
        field.setName("status");
        field.setType("xs:string");
        responseDetails.setFields(Collections.singletonList(field));

        String xml = wadlGeneratorService.generateResponse(responseDetails);
        assertTrue(xml.contains("<param name=\"status\" style=\"plain\" type=\"xs:string\"/>"));
    }

    @Test
    void testGenerateParameter_ShouldReturnValidXml() {
        WadlParameter param = new WadlParameter();
        param.setName("page");
        param.setType("xs:int");
        param.setRequired(false);

        String xml = wadlGeneratorService.generateParameter(param, "query");
        assertTrue(xml.contains("<param name=\"page\" style=\"query\" type=\"xs:int\" required=\"false\"/>"));
    }

    @Test
    void testGenerateField_ShouldReturnValidXml() {
        WadlField field = new WadlField();
        field.setName("age");
        field.setType("xs:int");

        String xml = wadlGeneratorService.generateField(field);
        assertTrue(xml.contains("<param name=\"age\" style=\"plain\" type=\"xs:int\"/>"));
    }

    @Test
    void testSaveWadlFile_CreatesFileSuccessfully() throws Exception {
        File tempFile = tempDir.resolve("test.wadl").toFile();

        try (MockedConstruction<FileWriter> mockedFileWriter = mockConstruction(FileWriter.class,
                (mock, context) -> {
                    doNothing().when(mock).write(anyString());
                    doNothing().when(mock).close();
                })) {

            String result = wadlGeneratorService.generateWadl(createValidRequest());
            assertTrue(result.contains("Arquivo WADL gerado com sucesso"));

            File generatedFile = new File("generated_wadl/TestService.wadl");
            assertTrue(generatedFile.exists());
        }
    }

    @Test
    void testSaveWadlFile_ShouldThrowException_WhenIOExceptionOccurs() {
        try (MockedConstruction<FileWriter> mockedFileWriter = mockConstruction(FileWriter.class,
                (mock, context) -> {
                    doThrow(new IOException("Simulated Error")).when(mock).write(anyString());
                })) {

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> wadlGeneratorService.saveWadlFile("test.wadl", "<application></application>"));

            assertTrue(exception.getMessage().contains("Erro ao salvar o arquivo WADL"));
        }
    }

    private WadlRequest createValidRequest() {
        WadlRequest request = new WadlRequest();
        request.setServiceName("TestService");
        request.setBaseUri("http://example.com");

        WadlMethod method = new WadlMethod();
        method.setName("GET");
        method.setPath("/test");

        request.setMethods(Collections.singletonList(method));
        return request;
    }
}
