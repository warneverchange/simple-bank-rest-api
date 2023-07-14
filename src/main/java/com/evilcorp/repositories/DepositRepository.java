package com.evilcorp.repositories;

import com.evilcorp.entities.Bank;
import com.evilcorp.entities.BankDeposit;
import com.evilcorp.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<BankDeposit, Integer> {
    List<BankDeposit> findAllByClient(Client client);
    List<BankDeposit> findAllByBank(Bank bank);

    boolean existsByClientAndBankAndOpeningDateAndAnnualRateAndPeriod(
            Client client,
            Bank bank,
            Timestamp openingDate,
            Double annualRate,
            Integer period);
}
