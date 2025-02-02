package com.exemplo.soagenerator.controller;

import com.exemplo.soagenerator.dto.XsdRequest;
import com.exemplo.soagenerator.service.XsdGeneratorService;
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
public class XsdGeneratorController {

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

    @GetMapping("/download/{fileName}")
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
