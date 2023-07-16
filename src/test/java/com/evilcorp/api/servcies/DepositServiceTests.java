package com.evilcorp.api.servcies;

import com.evilcorp.entities.Bank;
import com.evilcorp.entities.BankDeposit;
import com.evilcorp.entities.Client;
import com.evilcorp.exceptions.*;
import com.evilcorp.repositories.BankRepository;
import com.evilcorp.repositories.ClientRepository;
import com.evilcorp.repositories.DepositRepository;
import com.evilcorp.services.DepositServiceImpl;
import junit.framework.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepositServiceTests {
    @Mock
    private DepositRepository depositRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private BankRepository bankRepository;

    @InjectMocks
    private DepositServiceImpl depositService;

    private List<Client> clients;
    private Client client;
    private List<Bank> banks;
    private Bank bank;
    private List<BankDeposit> bankDeposits;
    private BankDeposit bankDeposit;
    private static final Timestamp openingDate = new Timestamp(System.currentTimeMillis());

    private static final double annualRate = 14.35;
    private static final int period = 12;
    private static final int bankId = 1;
    private static final int clientId = 1;
    private static final int depositId = 1;

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
    public void DepositServiceImpl_getAllDeposits_ReturnsListOfBankDeposits() {
        given(depositRepository.findAll()).willReturn(bankDeposits);
        Assertions.assertThat(depositService.getAllDeposits()).isEqualTo(bankDeposits);
    }

    @Test
    public void DepositServiceImpl_getDepositById_ReturnsBankDeposit_1() {
        given(depositRepository.existsById(bankDeposit.getId())).willReturn(false);
        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> depositService.getDepositById(bankDeposit.getId()));
    }

    @Test
    public void DepositServiceImpl_getDepositById_ReturnsBankDeposit_2() {
        given(depositRepository.existsById(bankDeposit.getId())).willReturn(true);
        given(depositRepository.findById(bankDeposit.getId())).willReturn(Optional.of(bankDeposit));
        Assertions.assertThat(depositService.getDepositById(bankDeposit.getId())).isEqualTo(bankDeposit);
    }


    @Test
    public void DepositServiceImpl_getClientsDeposits_ReturnsListOfBankDeposits_1() {
        given(clientRepository.existsById(client.getId())).willReturn(true);
        given(clientRepository.findById(clientId)).willReturn(Optional.of(client));
        given(depositRepository.findAllByClient(client)).willReturn(bankDeposits
                .stream()
                .filter(
                        deposit -> deposit
                                .getClient()
                                .equals(client))
                .toList());
        Assertions.assertThat(depositService.getClientsDeposits(client.getId())).isEqualTo(bankDeposits
                .stream()
                .filter(
                        deposit -> deposit
                                .getClient()
                                .equals(client))
                .toList()
        );
    }

    @Test
    public void DepositServiceImpl_getClientsDeposits_ReturnsListOfBankDeposits_2() {
        given(clientRepository.existsById(client.getId())).willReturn(false);
        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> depositService.getClientsDeposits(client.getId()));
    }

    @Test
    public void DepositServiceImpl_getAllDepositsInBank_ReturnsListOfBankDeposits_1() {
        given(bankRepository.existsById(bank.getId())).willReturn(true);
        given(bankRepository.findById(bank.getId())).willReturn(Optional.of(bank));
        given(depositRepository.findAllByBank(bank)).willReturn(bankDeposits.stream().filter(
                dep -> dep.getBank().equals(bank)
        ).toList());
        Assertions.assertThat(depositService.getAllDepositsInBank(bank.getId())).isEqualTo(
                bankDeposits.stream().filter(
                        dep -> dep.getBank().equals(bank)
                ).toList());
    }

    @Test
    public void DepositServiceImpl_getAllDepositsInBank_ReturnsListOfBankDeposits_2() {
        given(bankRepository.existsById(bank.getId())).willReturn(false);
        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> depositService.getAllDepositsInBank(bank.getId()));
    }

    @Test
    public void DepositServiceImpl_createNewDeposit_ReturnsBankDeposit_1() {
        given(depositRepository.existsByClientAndBankAndOpeningDateAndAnnualRateAndPeriod(
                bankDeposit.getClient(),
                bankDeposit.getBank(),
                bankDeposit.getOpeningDate(),
                bankDeposit.getAnnualRate(),
                bankDeposit.getPeriod()
        )).willReturn(false);
        var passedBankDeposit = BankDeposit
                .builder()
                .bank(bankDeposit.getBank())
                .annualRate(bankDeposit.getAnnualRate())
                .client(bankDeposit.getClient())
                .openingDate(bankDeposit.getOpeningDate())
                .period(bankDeposit.getPeriod())
                .build();
        given(depositRepository.save(passedBankDeposit)).willReturn(bankDeposit);
        Assertions.assertThat(depositService.createNewDeposit(passedBankDeposit)).isEqualTo(bankDeposit);
    }

    @Test
    public void DepositServiceImpl_createNewDeposit_ReturnsBankDeposit_2() {
        given(depositRepository.existsByClientAndBankAndOpeningDateAndAnnualRateAndPeriod(
                bankDeposit.getClient(),
                bankDeposit.getBank(),
                bankDeposit.getOpeningDate(),
                bankDeposit.getAnnualRate(),
                bankDeposit.getPeriod()
        )).willReturn(true);
        var passedBankDeposit = BankDeposit
                .builder()
                .bank(bankDeposit.getBank())
                .annualRate(bankDeposit.getAnnualRate())
                .client(bankDeposit.getClient())
                .openingDate(bankDeposit.getOpeningDate())
                .period(bankDeposit.getPeriod())
                .build();
        org.junit.jupiter.api.Assertions.assertThrows(EntityAlreadyExistException.class, () -> depositService.createNewDeposit(passedBankDeposit));
    }

    @Test
    public void DepositServiceImpl_closeDeposit_ReturnsVoid_1() {
        given(depositRepository.existsById(bankDeposit.getId())).willReturn(false);
        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> depositService.closeDeposit(bankDeposit.getId()));
    }

    @Test
    public void DepositServiceImpl_closeDeposit_ReturnsVoid_2() {
        doAnswer(invocation -> true).when(depositRepository).existsById(any());
        doAnswer(invocation -> Assertions.assertThat(
                        (Integer) invocation
                                .getArgument(0))
                .isEqualTo(bankDeposit.getId())
        ).when(depositRepository)
                .deleteById(any());
        depositService.closeDeposit(bankDeposit.getId());
    }

}
