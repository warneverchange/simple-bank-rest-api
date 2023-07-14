package com.evilcorp.controllers;

import com.evilcorp.entities.BankDeposit;
import com.evilcorp.services.DepositService;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/deposits")
public record DepositController(DepositService depositService) {
    @GetMapping
    public ResponseEntity<?> getAllDeposits() {
        return ResponseEntity.ok(depositService.getAllDeposits());
    }

    @GetMapping("/{depositId}")
    public ResponseEntity<?> getDepositById(@PathVariable("depositId") Integer depositId) {
        return ResponseEntity.ok(depositService.getDepositById(depositId));
    }

    @DeleteMapping("/{depositId}")
    public ResponseEntity<?> closeDeposit(@PathVariable("depositId") Integer depositId) {
        depositService.closeDeposit(depositId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> createNewDeposit(@RequestBody BankDeposit bankDeposit, HttpRequest request) {
        BankDeposit createdDeposit = depositService.createNewDeposit(bankDeposit);
        return ResponseEntity
                .created(URI.create(String.format("%s/%o", request.getURI(), bankDeposit.getId())))
                .body(createdDeposit);

    }

}
