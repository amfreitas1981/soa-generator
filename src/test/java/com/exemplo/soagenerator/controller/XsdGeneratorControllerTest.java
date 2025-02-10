package com.exemplo.soagenerator.controller;

import com.exemplo.soagenerator.dto.XsdField;
import com.exemplo.soagenerator.dto.XsdObject;
import com.exemplo.soagenerator.dto.XsdRequest;
import com.exemplo.soagenerator.service.XsdGeneratorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(XsdGeneratorController.class)
public class XsdGeneratorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private XsdRequest validRequest;
    private static final String TEST_SERVICE_NAME = "TestService";
    private static final String SUCCESS_MESSAGE = "Arquivos gerados: TestServiceRequest.xsd e TestServiceResponse.xsd";

    @BeforeEach
    void setUp() {
        // Configurar um objeto de requisição válido para os testes
        XsdField inputField = new XsdField("inputName", "string", true, false, false);
        XsdField outputField = new XsdField("outputName", "string", true, false, false);
        XsdField objectField = new XsdField("objectField", "string", true, false, false);

        XsdObject complexObject = new XsdObject("ComplexType", Collections.singletonList(objectField));

        validRequest = new XsdRequest(
                TEST_SERVICE_NAME,
                Collections.singletonList(inputField),
                Collections.singletonList(outputField),
                Collections.singletonList(complexObject)
        );
    }

    @Test
    void generateXsd_Success() throws Exception {
        try (MockedStatic<XsdGeneratorService> mockedStatic = mockStatic(XsdGeneratorService.class)) {
            // Configurar o comportamento do serviço mockado
            mockedStatic.when(() -> XsdGeneratorService.generateAndSaveXsd(
                    eq(TEST_SERVICE_NAME),
                    any(),
                    any(),
                    any()
            )).thenReturn(SUCCESS_MESSAGE);

            // Executar a requisição POST e verificar o resultado
            mockMvc.perform(post("/api/xsd/generate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(SUCCESS_MESSAGE));
        }
    }

    @Test
    void generateXsd_Error() throws Exception {
        try (MockedStatic<XsdGeneratorService> mockedStatic = mockStatic(XsdGeneratorService.class)) {
            // Simular uma exceção no serviço
            mockedStatic.when(() -> XsdGeneratorService.generateAndSaveXsd(
                    any(),
                    any(),
                    any(),
                    any()
            )).thenThrow(new IOException("Erro ao gerar arquivo"));

            // Executar a requisição POST e verificar o resultado de erro
            mockMvc.perform(post("/api/xsd/generate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Erro ao gerar XSD: Erro ao gerar arquivo"));
        }
    }

    @Test
    void downloadXsd_Success() throws Exception {
        String fileName = "TestService.xsd";
        byte[] fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes();

        try (MockedStatic<XsdGeneratorService> mockedStatic = mockStatic(XsdGeneratorService.class)) {
            // Configurar o comportamento do serviço mockado
            mockedStatic.when(() -> XsdGeneratorService.getFileContent(fileName))
                    .thenReturn(fileContent);

            // Executar a requisição GET e verificar o resultado
            mockMvc.perform(get("/api/xsd/download/{fileName}", fileName))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Disposition", "attachment; filename=" + fileName))
                    .andExpect(content().contentType(MediaType.APPLICATION_XML))
                    .andExpect(content().bytes(fileContent));
        }
    }

    @Test
    void downloadXsd_FileNotFound() throws Exception {
        String fileName = "NonExistent.xsd";

        try (MockedStatic<XsdGeneratorService> mockedStatic = mockStatic(XsdGeneratorService.class)) {
            // Simular arquivo não encontrado
            mockedStatic.when(() -> XsdGeneratorService.getFileContent(fileName))
                    .thenThrow(new IOException("Arquivo não encontrado"));

            // Executar a requisição GET e verificar o resultado
            mockMvc.perform(get("/api/xsd/download/{fileName}", fileName))
                    .andExpect(status().isNotFound());
        }
    }
}