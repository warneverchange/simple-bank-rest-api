package com.evilcorp.api.servcies;

import com.evilcorp.entities.Bank;
import com.evilcorp.exceptions.EntityAlreadyExistException;
import com.evilcorp.exceptions.EntityNotFoundException;
import com.evilcorp.repositories.BankRepository;
import com.evilcorp.services.BankServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BankServiceTests {
    @Mock
    private BankRepository bankRepository;


    @InjectMocks
    private BankServiceImpl bankService;

    private List<Bank> banks;
    private Bank bank;

    @BeforeEach
    public void init() {
        var bank = Bank
                .builder()
                .name("Evil corp bank")
                .bin("044525716")
                .build();
        this.bank = bank;
        banks = new LinkedList<>();
        banks.add(bank);
    }

    @Test
    public void BankServiceImpl_getAllBanks_ReturnsListOfAllBanks() {
        given(bankRepository.findAll()).willReturn(banks);
        Assertions.assertThat(bankService.getAllBanks()).isEqualTo(banks);
    }

    @Test
    public void BankServiceImpl_getBankById_ReturnsBank_1() {
        given(bankRepository.findById(banks.get(0).getId())).willReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> bankService.getBankById(banks.get(0).getId()));
    }

    @Test
    public void BankServiceImpl_getBankById_ReturnsBank_2() {
        given(bankRepository.findById(banks.get(0).getId())).willReturn(Optional.of(banks.get(0)));
        Assertions.assertThat(bankService.getBankById(banks.get(0).getId())).isEqualTo(banks.get(0));
    }

    @Test
    public void BankServiceImpl_registerNewBank_ReturnsBank_1() {
        Bank registeredBank = Bank
                .builder()
                .id(1)
                .name(banks.get(0).getName())
                .bin(banks.get(0).getBin())
                .build();
        given(bankRepository.existsByBinOrName(bank.getBin(), bank.getName())).willReturn(false);
        given(bankRepository.save(bank)).willReturn(registeredBank);
        Assertions.assertThat(bankService.registerNewBank(bank)).isEqualTo(registeredBank);
    }

    @Test
    public void BankServiceImpl_registerNewBank_ReturnsBank_2() {
        Bank registeredBank = Bank
                .builder()
                .id(1)
                .name(banks.get(0).getName())
                .bin(banks.get(0).getBin())
                .build();
        given(bankRepository.existsByBinOrName(bank.getBin(), bank.getName())).willReturn(true);
        org.junit.jupiter.api.Assertions.assertThrows(EntityAlreadyExistException.class, () -> bankService.registerNewBank(bank));
    }

    @Test
    public void BankServiceImpl_getBankByName_ReturnsBank_1() {
        given(bankRepository.findByName(bank.getName())).willReturn(Optional.of(bank));
        Assertions.assertThat(bankService.getBankByName(bank.getName())).isEqualTo(bank);
    }

    @Test
    public void BankServiceImpl_getBankByName_ReturnsBank_2() {
        given(bankRepository.findByName(bank.getName())).willReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> bankService.getBankByName(bank.getName()));
    }

    @Test
    public void BankServiceImpl_getBankByBin_ReturnsBank_1() {
        given(bankRepository.findBankByBin(bank.getBin())).willReturn(Optional.of(bank));
        Assertions.assertThat(bankService.getBankByBin(bank.getBin())).isEqualTo(bank);
    }

    @Test
    public void BankServiceImpl_getBankByBin_ReturnsBank_2() {
        given(bankRepository.findBankByBin(bank.getBin())).willReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> bankService.getBankByBin(bank.getBin()));
    }
}
