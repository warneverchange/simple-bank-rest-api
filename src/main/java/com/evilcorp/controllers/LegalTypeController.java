package com.evilcorp.controllers;

import com.evilcorp.entities.LegalType;
import com.evilcorp.services.LegalTypesService;
import jakarta.servlet.http.HttpServletRequest;
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

    @PutMapping
    public ResponseEntity<?> updateLegalTypeInfo(@RequestBody LegalType legalType) {
        legalTypesService.updateLegalTypeInfo(legalType);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{legalTypeId}")
    public ResponseEntity<?> getLegalTypeById(@PathVariable("legalTypeId") Integer legalTypeId) {
        return ResponseEntity.ok(legalTypesService.getLegalTypeById(legalTypeId));
    }

    @DeleteMapping("/{legalTypeId}")
    public ResponseEntity<?> deleteLegalTypeById(@PathVariable("legalTypeId") Integer legalTypeId) {
        legalTypesService.deleteLegalTypeById(legalTypeId);
        return ResponseEntity.ok().build();

    }

    @PostMapping
    public ResponseEntity<?> registerNewLegalType(@RequestBody LegalType legalType, HttpServletRequest request) {
        LegalType registeredLegalType = legalTypesService.registerNewLegalType(legalType);
        return ResponseEntity
                .created(URI.create(String.format("%s/%o", request.getRequestURI(), registeredLegalType.getId())))
                .body(registeredLegalType);
    }
}
