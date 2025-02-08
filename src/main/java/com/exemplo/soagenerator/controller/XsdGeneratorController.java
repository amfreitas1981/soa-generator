package com.exemplo.soagenerator.controller;

import com.exemplo.soagenerator.dto.XsdRequest;
import com.exemplo.soagenerator.service.XsdGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/xsd")
@Tag(name = "XSD", description = "Criação e download de arquivos no formato .xsd")
public class XsdGeneratorController {

    /**
     *  TODO: Endpoint para gerar um arquivo XSD a partir da requisição JSON.
     */
    @PostMapping("/generate")
    public ResponseEntity<String> generateXsd(@RequestBody XsdRequest request) {
        try {
            String message = XsdGeneratorService.generateAndSaveXsd(
                    request.getServiceName(),
                    request.getInputFields(),
                    request.getOutputFields(),
                    request.getObjects()
            );

            return ResponseEntity.ok(message);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao gerar XSD: " + e.getMessage());
        }
    }

    /**
     * TODO: Endpoint para baixar o arquivo XSD gerado.
     */
    @GetMapping("/download/{fileName}")
    @Operation(summary = "Obter e baixar um arquivo .xsd pelo nome do serviço, acompanhado da extensão.", description = "Retorna um arquivo com base no Serviço XSD fornecido")
    @ApiResponse(responseCode = "200", description = "Serviço encontrado",
            content = @Content(schema = @Schema(implementation = XsdGeneratorService.class)))
    @ApiResponse(responseCode = "404", description = "Arquivo .xsd não encontrado")
    public ResponseEntity<ByteArrayResource> downloadXsd(@PathVariable String fileName) {
        try {
            byte[] data = XsdGeneratorService.getFileContent(fileName);
            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.APPLICATION_XML)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
