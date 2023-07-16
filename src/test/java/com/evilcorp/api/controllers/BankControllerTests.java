package com.evilcorp.api.controllers;

import com.evilcorp.controllers.BankController;
import com.evilcorp.entities.Bank;
import com.evilcorp.entities.BankDeposit;
import com.evilcorp.entities.Client;
import com.evilcorp.exceptions.EntityAlreadyExistException;
import com.evilcorp.exceptions.EntityNotFoundException;
import com.evilcorp.responses.EntityAlreadyExistResponse;
import com.evilcorp.responses.EntityNotFoundResponse;
import com.evilcorp.services.BankServiceImpl;
import com.evilcorp.services.DepositServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.awt.*;
import java.lang.runtime.ObjectMethods;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = BankController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BankControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BankServiceImpl bankService;

    @MockBean
    private DepositServiceImpl depositService;

    private List<Client> clients;
    private Client client;
    private List<BankDeposit> bankDeposits;
    private BankDeposit bankDeposit;
    private static int clientId = 1;
    private static int bankId = 1;
    private static int depositId = 1;
    private static Timestamp openingDate = new Timestamp(System.currentTimeMillis());
    private double annualRate = 13.54;
    private int period = 12;
    private List<Bank> banks;
    private Bank bank;

    @BeforeEach
    public void init() {
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
    public void BankController_getAllBanks_ReturnAllBanks() throws Exception {
        when(bankService.getAllBanks()).thenReturn(banks);
        var response = mockMvc.perform(
                get("/api/v1/banks")
                        .content(MediaType.APPLICATION_JSON_VALUE)
        );
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                banks
                        )));
    }

    @Test
    public void BankController_getAllDepositsInBank_ReturnsListOfBankDeposits_1() throws Exception {
        when(depositService.getAllDepositsInBank(any())).thenReturn(bankDeposits
                .stream()
                .filter(bankDeposit1 -> bankDeposit.getBank().equals(bank))
                .toList());
        var response = mockMvc.perform(
                get(String.format("/api/v1/banks/%o/deposits", bank.getId()))
                        .content(MediaType.APPLICATION_JSON_VALUE)
        );
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                bankDeposits
                                        .stream()
                                        .filter(bankDeposit1 -> bankDeposit.getBank().equals(bank))
                                        .toList()
                        )));
    }

    @Test
    public void BankController_getAllDepositsInBank_ReturnsListOfBankDeposits_2()
            throws Exception {
        var msg = "Bank with such id not found";

        when(depositService.getAllDepositsInBank(any()))
                .thenThrow(new EntityNotFoundException(msg, bank.getId()));
        var response = mockMvc.perform(
                get(String.format("/api/v1/banks/%o/deposits", bank.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                new EntityNotFoundResponse(
                                        msg,
                                        bank.getId()
                                )
                        )));
    }

    @Test
    public void BankController_getBankById_ReturnsBank_1() throws Exception {
        when(bankService.getBankById(any())).thenReturn(bank);
        var response = mockMvc.perform(
                get(String.format("/api/v1/banks/%o", bank.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                bank
                        )));
    }

    @Test
    public void BankController_getBankById_ReturnsBank_2() throws Exception {
        var msg = "Bank with such id's not found";
        when(bankService.getBankById(any())).thenThrow(new EntityNotFoundException(msg, bank.getId()));
        var response = mockMvc.perform(
                get(String.format("/api/v1/banks/%o", bank.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                new EntityNotFoundResponse(msg, bank.getId())
                        )));
    }

    @Test
    public void BankController_getBankByBin_ReturnsBank_1() throws Exception {
        when(bankService.getBankByBin(any())).thenReturn(bank);
        var response = mockMvc.perform(
                get(String.format("/api/v1/banks/bin/%s", bank.getBin()))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                bank
                        )));
    }


    @Test
    public void BankController_getBankByBin_ReturnsBank_2() throws Exception {
        var msg = "Bank with such bin not found";
        when(bankService.getBankByBin(any())).thenThrow(new EntityNotFoundException(msg, bank.getBin()));
        var response = mockMvc.perform(
                get(String.format("/api/v1/banks/bin/%s", bank.getBin()))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                new EntityNotFoundResponse(msg, bank.getBin())
                        )));
    }

    @Test
    public void BankController_getBankByName_ReturnsBank_1() throws Exception {
        when(bankService.getBankByName(any())).thenReturn(bank);
        var response = mockMvc.perform(
                get(String.format("/api/v1/banks/name/%s", bank.getName()))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                bank
                        )));
    }

    @Test
    public void BankController_getBankByName_ReturnsBank_2() throws Exception {
        var msg = "Bank with such name not found";
        when(bankService.getBankByName(any())).thenThrow(new EntityNotFoundException(msg, bank.getName()));
        var response = mockMvc.perform(
                get(String.format("/api/v1/banks/name/%s", bank.getName()))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper
                        .writeValueAsString(
                                new EntityNotFoundResponse(msg, bank.getName())
                        )));
    }


    @Test
    public void BankController_registerNewBank_ReturnsBank_1() throws Exception {
        when(bankService.registerNewBank(bank)).thenReturn(bank);
        var response = mockMvc.perform(
                post("/api/v1/banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bank))
        );
        response.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/api/v1/banks/1"))
                .andExpect(content().json(objectMapper
                        .writeValueAsString(
                                bank
                        )));


    }


    @Test
    public void BankController_registerNewBank_ReturnsBank_2() throws Exception {
        var msg = "Bank with such bin or name already exist";
        when(bankService.registerNewBank(bank)).thenThrow(new EntityAlreadyExistException(msg, bank));
        var response = mockMvc.perform(
                post("/api/v1/banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bank))
        );
        response.andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new EntityAlreadyExistResponse(msg, bank)
                )));


    }

}
