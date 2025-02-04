package com.exemplo.soagenerator.controller;

import com.exemplo.soagenerator.dto.WsdlRequest;
import com.exemplo.soagenerator.service.WsdlGeneratorService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/wsdl")
public class WsdlGeneratorController {

    private final WsdlGeneratorService wsdlGeneratorService;

    private static final String DIRECTORY = "generated_wsdl/";

    public WsdlGeneratorController(WsdlGeneratorService wsdlGeneratorService) {
        this.wsdlGeneratorService = wsdlGeneratorService;
    }

    /**
     * Endpoint para gerar um arquivo WSDL a partir da requisição JSON.
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
     * Endpoint para obter o conteúdo do arquivo WSDL gerado.
     */
    @GetMapping("/content/{serviceName}")
    public ResponseEntity<String> getFileContent(@PathVariable String serviceName) {
        String filePath = serviceName + ".wsdl";
        File file = new File(filePath);

        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Arquivo WSDL não encontrado: " + filePath);
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            return ResponseEntity.ok(content);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    /**
     * Endpoint para baixar o arquivo WSDL gerado.
     */
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadWsdl(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(DIRECTORY).resolve(fileName).normalize();
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Resource resource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
