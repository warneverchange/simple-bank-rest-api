package com.evilcorp.controllers;

import com.evilcorp.entities.Client;
import com.evilcorp.repositories.ClientRepository;
import com.evilcorp.services.ClientService;
import com.evilcorp.services.DepositService;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/clients")
public record ClientController(
        ClientService clientService,
        DepositService depositService) {
    @GetMapping("/{clientId}")
    public ResponseEntity<?> getClientById(@PathVariable("clientId") Integer clientId) {
        return ResponseEntity.ok(clientService.getClientById(clientId));
    }

    @GetMapping("/{clientId}/deposits")
    public ResponseEntity<?> getClientsDeposits(@PathVariable("clientId") Integer clientId) {
        return ResponseEntity.ok(depositService.getClientsDeposits(clientId));
    }


    @GetMapping
    public ResponseEntity<?> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }
    @GetMapping("/name")
    public ResponseEntity<?> getClientByName(String clientName) {
        return ResponseEntity.ok(clientService.getClientByClientName(clientName));
    }

    @GetMapping("/short_name")
    public ResponseEntity<?> getClientByShortName(String shortClientName) {
        return ResponseEntity.ok(clientService.getClientByClientShortName(shortClientName));
    }

    @PostMapping
    public ResponseEntity<?> registerNewClient(
            @RequestBody Client client,
            HttpRequest request) {
        Client registeredClient = clientService.registerNewClient(client);
        return ResponseEntity
                .created(URI.create(String.format("%s/%o", request.getURI(), client.getId())))
                .body(client);
    }
}
