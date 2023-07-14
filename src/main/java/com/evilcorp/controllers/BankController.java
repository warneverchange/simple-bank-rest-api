package com.evilcorp.controllers;

import com.evilcorp.entities.Bank;
import com.evilcorp.services.BankService;
import com.evilcorp.services.DepositService;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/banks")
public record BankController(
        BankService bankService,
        DepositService depositService) {
    @GetMapping
    public ResponseEntity<?> getAllBanks() {
        return ResponseEntity.ok(bankService.getAllBanks());
    }

    @GetMapping("/{bankId}/deposits")
    public ResponseEntity<?> getAllDepositsInBank(@PathVariable("bankId") Integer bankId) {
        return ResponseEntity.ok(depositService.getAllDepositsInBank(bankId));
    }

    @GetMapping("/{bankId}")
    public ResponseEntity<?> getBankById(@PathVariable("bankId") Integer bankId) {
        return ResponseEntity.ok(bankService.getBankById(bankId));
    }

    @GetMapping("/bin/{binNumber}")
    public ResponseEntity<?> getBankByBinNumber(@PathVariable("binNumber") String binNumber) {
        return ResponseEntity.ok(bankService.getBankByBin(binNumber));
    }

    @GetMapping("/name/{bankName}")
    public ResponseEntity<?> getBankByName(@PathVariable("bankName") String bankName) {
        return ResponseEntity.ok(bankService.getBankByName(bankName));
    }

    @PostMapping
    public ResponseEntity<?> registerNewBank(@RequestBody Bank bank, HttpRequest request) {
        Bank registeredBank = bankService.registerNewBank(bank);
        return ResponseEntity
                .created(URI.create(String.format("%s/%o", request.getURI(), registeredBank.getId())))
                .body(registeredBank);
    }
}
