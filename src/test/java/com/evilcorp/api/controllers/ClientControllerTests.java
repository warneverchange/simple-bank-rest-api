package com.evilcorp.api.controllers;


import com.evilcorp.controllers.BankController;
import com.evilcorp.controllers.ClientController;
import com.evilcorp.entities.Bank;
import com.evilcorp.entities.BankDeposit;
import com.evilcorp.entities.Client;
import com.evilcorp.exceptions.EntityAlreadyExistException;
import com.evilcorp.exceptions.EntityNotFoundException;
import com.evilcorp.responses.EntityAlreadyExistResponse;
import com.evilcorp.responses.EntityNotFoundResponse;
import com.evilcorp.services.ClientServiceImpl;
import com.evilcorp.services.DepositServiceImpl;
import com.evilcorp.services.LegalTypeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

@WebMvcTest(controllers = ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ClientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientServiceImpl clientService;

    @MockBean
    private DepositServiceImpl depositService;

    private List<Client> clients;
    private Client client;
    private List<Bank> banks;
    private Bank bank;
    private BankDeposit bankDeposit;
    private List<BankDeposit> bankDeposits;
    private static int clientId = 1;
    private static int bankId = 1;
    private static Timestamp openingDate = new Timestamp(System.currentTimeMillis());
    private static double annualRate = 13.54;
    private static int period = 12;

    private static int depositId = 1;

    @BeforeEach
    private void init() {
        clients = new LinkedList<>();
        banks = new LinkedList<>();
        bankDeposits = new LinkedList<>();
        client = Client
                .builder()
                .id(clientId)
                .name("Konstantin")
                .shortName("Kostya")
                .address("Chkalova, 44")
                .build();
        bank = Bank
                .builder()
                .id(bankId)
                .name("Evil corp bank")
                .bin("044525716")
                .build();
        bankDeposit = BankDeposit
                .builder()
                .id(depositId)
                .bank(bank)
                .client(client)
                .openingDate(openingDate)
                .annualRate(annualRate)
                .period(period)
                .build();
        clients.add(client);
        banks.add(bank);
        bankDeposits.add(bankDeposit);
    }


    @Test
    public void ClientController_getClientById_ReturnsClient_1() throws Exception {
        when(clientService.getClientById(any())).thenReturn(client);
        var response = mockMvc.perform(
                get(String.format("/api/v1/clients/%o", client.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                client
                        )));
    }

    @Test
    public void ClientController_getClientById_ReturnsClient_2() throws Exception {
        var msg = "Client with such id not found";
        when(clientService.getClientById(any())).thenThrow(new EntityNotFoundException(msg, client.getId()));
        var response = mockMvc.perform(
                get(String.format("/api/v1/clients/%o", client.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                new EntityNotFoundResponse(msg, client.getId())
                        )));
    }

    @Test
    public void ClientController_registerNewClient_ReturnsClient_1() throws Exception{
        when(clientService.registerNewClient(client)).thenReturn(client);
        var response = mockMvc.perform(
                post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client))
        );
        response.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/api/v1/clients/1"))
                .andExpect(content().json(objectMapper
                        .writeValueAsString(
                                client
                        )));

    }

    @Test
    public void ClientController_registerNewClient_ReturnsClient_2() throws Exception{
        var msg = "Client with such name or short name already exist";
        when(clientService.registerNewClient(client)).thenThrow(new EntityAlreadyExistException(msg, client));
        var response = mockMvc.perform(
                post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client))
        );
        response.andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new EntityAlreadyExistResponse(msg, client)
                )));
    }

    @Test
    public void ClientController_getAllClients_ReturnsListOfClients() throws Exception {
        when(clientService.getAllClients()).thenReturn(clients);
        var response = mockMvc.perform(
                get("/api/v1/clients")
                        .content(MediaType.APPLICATION_JSON_VALUE)
        );
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                clients
                        )));
    }

    @Test
    public void ClientController_getClientByClientName_ReturnsClient_1() throws Exception {
        when(clientService.getClientByClientName(any())).thenReturn(client);
        var response = mockMvc.perform(
                get(String.format("/api/v1/clients/name/%s", client.getName()))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                client
                        )));
    }

    @Test
    public void ClientController_getClientByClientName_ReturnsClient_2() throws Exception {
        var msg = "Client with such name not found";
        when(clientService.getClientByClientName(any())).thenThrow(new EntityNotFoundException(msg, client.getName()));
        var response = mockMvc.perform(
                get(String.format("/api/v1/clients/name/%s", client.getName()))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                new EntityNotFoundResponse(msg, client.getName())
                        )));
    }

    @Test
    public void ClientController_getClientsDeposits_ReturnsListOfDeposits_1() throws Exception {
        when(depositService.getClientsDeposits(any())).thenReturn(bankDeposits
                .stream()
                .filter(bankDeposit1 -> bankDeposit.getClient().equals(client))
                .toList());
        var response = mockMvc.perform(
                get(String.format("/api/v1/clients/%o/deposits", client.getId()))
                        .content(MediaType.APPLICATION_JSON_VALUE)
        );
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(bankDeposits
                                .stream()
                                .filter(bankDeposit1 -> bankDeposit1.getClient().equals(client))
                                .toList()
                        )));
    }

    @Test
    public void ClientController_getClientsDeposits_ReturnsListOfDeposits_2() throws Exception {
        var msg = "Client with such id not found";

        when(depositService.getClientsDeposits(any()))
                .thenThrow(new EntityNotFoundException(msg, client.getId()));
        var response = mockMvc.perform(
                get(String.format("/api/v1/clients/%o/deposits", client.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                new EntityNotFoundResponse(
                                        msg,
                                        client.getId()
                                )
                        )));
    }

    @Test
    public void ClientController_getClientByShortName_ReturnsClient_1() throws Exception {
        when(clientService.getClientByClientShortName(any())).thenReturn(client);
        var response = mockMvc.perform(
                get(String.format("/api/v1/clients/short_name/%s", client.getShortName()))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                client
                        )));
    }

    @Test
    public void ClientController_getClientByShortName_ReturnsClient_2() throws Exception {
        var msg = "Client with such short name not found";
        when(clientService.getClientByClientShortName(any())).thenThrow(new EntityNotFoundException(msg, client.getShortName()));
        var response = mockMvc.perform(
                get(String.format("/api/v1/clients/short_name/%s", client.getShortName()))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                new EntityNotFoundResponse(msg, client.getShortName())
                        )));
    }
}
