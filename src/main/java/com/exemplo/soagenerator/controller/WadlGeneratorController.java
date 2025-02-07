package com.exemplo.soagenerator.controller;

import com.exemplo.soagenerator.dto.WadlRequest;
import com.exemplo.soagenerator.service.WadlGeneratorService;
import com.exemplo.soagenerator.service.WadlJsonGeneratorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/wadl")
public class WadlGeneratorController {

    private static final String OUTPUT_DIRECTORY = "generated_wadl/";

    @Autowired
    private WadlGeneratorService wadlGeneratorService;

    @Autowired
    private WadlJsonGeneratorService wadlJsonGeneratorService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateWadl(@Valid @RequestBody WadlRequest request) {
        return ResponseEntity.ok(wadlGeneratorService.generateWadl(request));
    }

    @GetMapping("/structure/{serviceName}")
    public ResponseEntity<String> getWadlStructure(@PathVariable String serviceName) {
        try {
            Path filePath = Paths.get(OUTPUT_DIRECTORY + serviceName + ".wadl");
            String content = Files.readString(filePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_XML)
                    .body(content);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/download/{serviceName}")
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

    @PostMapping("/generate-wadl")
    public ResponseEntity<String> generateWadlWithStructures(@Valid @RequestBody WadlRequest request) {
        // Gera tanto o arquivo WADL quanto o JSON com as estruturas
        String wadlResult = wadlGeneratorService.generateWadl(request);
        String jsonResult = wadlJsonGeneratorService.generateWadl(request);

        return ResponseEntity.ok("Arquivos gerados com sucesso:\n" + wadlResult + "\n" + jsonResult);
    }
}
