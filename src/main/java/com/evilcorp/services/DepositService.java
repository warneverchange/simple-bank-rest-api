package com.evilcorp.services;

import com.evilcorp.entities.Bank;
import com.evilcorp.entities.BankDeposit;
import com.evilcorp.entities.Client;

import java.util.List;

public interface DepositService {
    List<BankDeposit> getAllDeposits();

    BankDeposit getDepositById(Integer depositId);

    List<BankDeposit> getClientsDeposits(Integer clientId);
    List<BankDeposit> getAllDepositsInBank(Integer bankId);

    BankDeposit createNewDeposit(BankDeposit bankDeposit);
    void closeDeposit(Integer depositId);

}
