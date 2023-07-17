package com.evilcorp.services;

import com.evilcorp.entities.Bank;
import com.evilcorp.entities.BankDeposit;
import com.evilcorp.entities.Client;
import com.evilcorp.exceptions.*;
import com.evilcorp.repositories.BankRepository;
import com.evilcorp.repositories.ClientRepository;
import com.evilcorp.repositories.DepositRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DepositServiceImpl implements DepositService {
    private final DepositRepository depositRepository;
    private final ClientRepository clientRepository;
    private final BankRepository bankRepository;
    @Override
    public List<BankDeposit> getAllDeposits() {
        return depositRepository.findAll();
    }

    @Override
    public BankDeposit getDepositById(Integer depositId) {
        if (!depositRepository.existsById(depositId)) {
            throw new EntityNotFoundException("Deposit with such id not found", depositId);
        }
        return depositRepository.findById(depositId).get();
    }

    @Override
    public List<BankDeposit> getClientsDeposits(Integer clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new EntityNotFoundException("Client with such id not found", clientId);
        }
        Optional<Client> client = clientRepository.findById(clientId);
        return depositRepository.findAllByClient(client.orElseThrow());
    }

    @Override
    public List<BankDeposit> getAllDepositsInBank(Integer bankId) {
        if (!bankRepository.existsById(bankId)) {
            throw new EntityNotFoundException("Bank with such id not found", bankId);
        }
        Optional<Bank> bank = bankRepository.findById(bankId);
        return depositRepository.findAllByBank(bank.orElseThrow());
    }

    @Override
    public BankDeposit createNewDeposit(BankDeposit bankDeposit) {
        Integer clientId = bankDeposit.getClient().getId();
        Integer bankId = bankDeposit.getBank().getId();
        if (!clientRepository.existsById(clientId) || !bankRepository.existsById(bankId)) {
            throw new EntityNotFoundException("Client & bank must be exists", clientId);
        }
        var client = clientRepository.findById(clientId).orElseThrow();
        var bank = bankRepository.findById(bankId).orElseThrow();

        if (depositRepository.existsByClientAndBankAndOpeningDateAndAnnualRateAndPeriod(
                client,
                bank,
                bankDeposit.getOpeningDate(),
                bankDeposit.getAnnualRate(),
                bankDeposit.getPeriod()
        )) {
            throw new EntityAlreadyExistException("Deposit can't be created, cause already exist", bankDeposit);
        }
        return depositRepository.save(bankDeposit);
    }

    @Override
    public void closeDeposit(Integer depositId) {
        if (!depositRepository.existsById(depositId)) {
            throw new EntityNotFoundException("Not such deposit for closing", depositId);
        }
        depositRepository.deleteById(depositId);
    }

    @Override
    public void updateDepositInfo(BankDeposit depositForUpdate) {
        if (depositForUpdate.getId() == null
                || depositRepository.findById(depositForUpdate.getId()).isEmpty()) {
            throw new EntityNotFoundException("Bank deposit isn't present", depositForUpdate.getId());
        }
        depositRepository.save(depositForUpdate);
    }
}
