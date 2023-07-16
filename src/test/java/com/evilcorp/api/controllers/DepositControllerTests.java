package com.evilcorp.api.controllers;

import com.evilcorp.controllers.ClientController;
import com.evilcorp.controllers.DepositController;
import com.evilcorp.entities.Bank;
import com.evilcorp.entities.BankDeposit;
import com.evilcorp.entities.Client;
import com.evilcorp.exceptions.EntityAlreadyExistException;
import com.evilcorp.exceptions.EntityNotFoundException;
import com.evilcorp.responses.EntityAlreadyExistResponse;
import com.evilcorp.responses.EntityNotFoundResponse;
import com.evilcorp.services.DepositServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(controllers = DepositController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class DepositControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void DepositController_getAllDeposits_ReturnsAllDeposits() throws Exception {
        when(depositService.getAllDeposits()).thenReturn(bankDeposits);
        var response = mockMvc.perform(
                get("/api/v1/deposits")
                        .content(MediaType.APPLICATION_JSON_VALUE)
        );
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                 bankDeposits
                        )));
    }

    @Test
    public void DepositController_getDepositById_ReturnsDeposit_1() throws Exception {
        when(depositService.getDepositById(any())).thenReturn(bankDeposit);
        var response = mockMvc.perform(
                get(String.format("/api/v1/deposits/%o", bankDeposit.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                bankDeposit
                        )));
    }
    @Test
    public void DepositController_getDepositById_ReturnsDeposit_2() throws Exception {
        var msg = "Deposit with such id not found";
        when(depositService.getDepositById(any())).thenThrow(new EntityNotFoundException(msg, bankDeposit.getId()));
        var response = mockMvc.perform(
                get(String.format("/api/v1/deposits/%o", bankDeposit.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                new EntityNotFoundResponse(msg, bankDeposit.getId())
                        )));
    }

    @Test
    public void DepositController_createNewDeposit_ReturnsDeposit_1() throws Exception{
        when(depositService.createNewDeposit(bankDeposit)).thenReturn(bankDeposit);
        var response = mockMvc.perform(
                post("/api/v1/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankDeposit))
        );
        response.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/api/v1/deposits/1"))
                .andExpect(content().json(objectMapper
                        .writeValueAsString(
                                bankDeposit
                        )));
    }

    @Test
    public void DepositController_createNewDeposit_ReturnsDeposit_2() throws Exception{
        var msg = "Deposit can't be created, cause already exist";
        when(depositService.createNewDeposit(bankDeposit)).thenThrow(new EntityAlreadyExistException(msg, bankDeposit));
        var response = mockMvc.perform(
                post("/api/v1/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankDeposit))
        );
        response.andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new EntityAlreadyExistResponse(msg, bankDeposit)
                )));
    }

    @Test
    public void DepositController_closeDeposit_ReturnsStatusCode_1() throws Exception {
        doAnswer(invocation -> invocation.getArgument(0)).when(depositService).closeDeposit(bankDeposit.getId());
        var response = mockMvc.perform(
                delete(String.format("/api/v1/deposits/%o", bankDeposit.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isOk());
    }

    @Test
    public void  DepositController_closeDeposit_ReturnsStatusCode_2() throws Exception {
        var msg = "Not such deposit for closing";
        doAnswer(invocation -> {throw new EntityNotFoundException("Not such deposit for closing", bankDeposit.getId());})
                .when(depositService).closeDeposit(bankDeposit.getId());
        var response = mockMvc.perform(
                delete(String.format("/api/v1/deposits/%o", bankDeposit.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(
                                new EntityNotFoundResponse(msg, bankDeposit.getId())
                        )));
    }
}
