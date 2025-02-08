package com.exemplo.soagenerator.controller;

import com.exemplo.soagenerator.dto.WsdlRequest;
import com.exemplo.soagenerator.service.WsdlGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/wsdl")
@Tag(name = "WSDL", description = "Criação e download de arquivos no formato .wsdl")
public class WsdlGeneratorController {

    private final WsdlGeneratorService wsdlGeneratorService;

    private static final String DIRECTORY = "generated_wsdl/";

    public WsdlGeneratorController(WsdlGeneratorService wsdlGeneratorService) {
        this.wsdlGeneratorService = wsdlGeneratorService;
    }

    /**
     *  TODO: Endpoint para gerar um arquivo WSDL a partir da requisição JSON.
     */
    @PostMapping("/generate")
    public ResponseEntity<String> generateWsdlFile(@RequestBody WsdlRequest request) {
        String filePath = wsdlGeneratorService.generateWsdl(request);

        if (filePath.startsWith("Erro")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(filePath);
        }

        return ResponseEntity.ok("WSDL gerado com sucesso: " + filePath);
    }

    /**
     * TODO: Endpoint para baixar o arquivo WSDL gerado.
     */
    @GetMapping("/download/{fileName}")
    @Operation(summary = "Obter e baixar um arquivo .wsdl pelo nome do serviço, acompanhado da extensão.", description = "Retorna um arquivo com base no Serviço WSDL fornecido")
    @ApiResponse(responseCode = "200", description = "Serviço encontrado",
            content = @Content(schema = @Schema(implementation = WsdlGeneratorService.class)))
    @ApiResponse(responseCode = "404", description = "Arquivo .wsdl não encontrado")
    public ResponseEntity<Resource> downloadWsdl(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(DIRECTORY).resolve(fileName).normalize();
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Resource resource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
