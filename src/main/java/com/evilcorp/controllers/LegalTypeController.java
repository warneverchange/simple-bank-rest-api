package com.evilcorp.controllers;

import com.evilcorp.entities.LegalType;
import com.evilcorp.services.LegalTypesService;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/legal_types")
public record LegalTypeController(LegalTypesService legalTypesService) {
    @GetMapping("/all")
    public ResponseEntity<List<LegalType>> getAllLegalTypes() {
        return ResponseEntity.ok(legalTypesService.getAllLegalTypes());
    }

    @PostMapping
    public ResponseEntity<?> registerNewLegalType(@RequestBody LegalType legalType, HttpRequest request) {
        LegalType registeredLegalType = legalTypesService.registerNewLegalType(legalType);
        return ResponseEntity
                .created(URI.create(String.format("%s/%o", request.getURI(), registeredLegalType.getId())))
                .body(registeredLegalType);
    }
}
