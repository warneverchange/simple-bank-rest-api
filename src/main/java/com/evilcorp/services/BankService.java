package com.evilcorp.services;

import com.evilcorp.entities.Bank;
import com.evilcorp.entities.BankDeposit;

import java.util.List;

public interface BankService {
    List<Bank> getAllBanks();
    Bank getBankById(Integer bankId);
    Bank registerNewBank(Bank bank);
    Bank getBankByName(String bankName);

    Bank getBankByBin(String bin);

    void updateBankInfo(Bank bankForUpdate);

    void deleteBankById(Integer bankId);
}
