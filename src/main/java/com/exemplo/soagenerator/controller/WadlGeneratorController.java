package com.exemplo.soagenerator.controller;

import com.exemplo.soagenerator.dto.WadlRequest;
import com.exemplo.soagenerator.service.WadlGeneratorService;
import com.exemplo.soagenerator.service.WadlJsonGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/wadl")
@Tag(name = "WADL", description = "Criação e download de arquivos no formato .wadl")
public class WadlGeneratorController {

    private static final String OUTPUT_DIRECTORY = "generated_wadl/";

    @Autowired
    private WadlGeneratorService wadlGeneratorService;

    @Autowired
    private WadlJsonGeneratorService wadlJsonGeneratorService;

    /**
     *  TODO: Endpoint para gerar um arquivo WADL a partir da requisição JSON.
     */
    @PostMapping("/generate")
    public ResponseEntity<String> generateWadl(@Valid @RequestBody WadlRequest request) {
        return ResponseEntity.ok(wadlGeneratorService.generateWadl(request));
    }

    /**
     *  TODO: Endpoint para gerar um arquivo WADL a partir da requisição JSON, retornando a estrutura dos métodos HTTP no formato JSON.
     */
    @PostMapping("/generate-wadl")
    public ResponseEntity<String> generateWadlWithStructures(@Valid @RequestBody WadlRequest request) {
        // Gera tanto o arquivo WADL quanto o JSON (para cada método HTTP correspondente) com as estruturas
        String jsonResult = wadlJsonGeneratorService.generateWadl(request);

        return ResponseEntity.ok("Arquivo gerado com sucesso:\n" + jsonResult);
    }

    /**
     * TODO: Endpoint para baixar o arquivo WADL gerado.
     */
    @GetMapping("/download/{serviceName}")
    @Operation(summary = "Obter e baixar um arquivo .wadl pelo nome do serviço.", description = "Retorna um arquivo com base no Serviço WADL fornecido")
    @ApiResponse(responseCode = "200", description = "Serviço encontrado",
            content = @Content(schema = @Schema(implementation = WadlGeneratorService.class)))
    @ApiResponse(responseCode = "404", description = "Arquivo .wadl não encontrado")
    public ResponseEntity<Resource> downloadWadl(@PathVariable String serviceName) {
        try {
            Path filePath = Paths.get(OUTPUT_DIRECTORY + serviceName + ".wadl");
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_XML)
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + serviceName + ".wadl\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
