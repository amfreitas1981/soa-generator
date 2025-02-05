package com.exemplo.soagenerator.controller;

import com.exemplo.soagenerator.dto.WadlRequest;
import com.exemplo.soagenerator.service.WadlGeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wadl")
public class WadlController {

    private final WadlGeneratorService wadlGeneratorService;

    public WadlController(WadlGeneratorService wadlGeneratorService) {
        this.wadlGeneratorService = wadlGeneratorService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateWadl(@RequestBody WadlRequest request) {
        String wadl = wadlGeneratorService.generateWadl(request);
        return ResponseEntity.ok(wadl);
    }
}
