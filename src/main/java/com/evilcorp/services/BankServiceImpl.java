package com.evilcorp.services;

import com.evilcorp.entities.Bank;
import com.evilcorp.exceptions.EntityAlreadyExistException;
import com.evilcorp.exceptions.EntityNotFoundException;
import com.evilcorp.repositories.BankRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;

    @Override
    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }

    @Override
    public Bank getBankById(Integer bankId) {
        Optional<Bank> bank = bankRepository.findById(bankId);
        if (bank.isEmpty()) {
            throw new EntityNotFoundException("Bank with such id's not found", bankId);
        }
        return bank.get();
    }

    @Override
    public Bank registerNewBank(Bank bank) {
        if (bankRepository.existsByBinOrName(bank.getBin(), bank.getName())) {
            throw new EntityAlreadyExistException("Bank with such bin or name already exist", bank);
        }
        return bankRepository.save(bank);
    }

    @Override
    public Bank getBankByName(String bankName) {
        Optional<Bank> bank = bankRepository.findByName(bankName);
        if (bank.isEmpty()) {
            throw new EntityNotFoundException("Bank with such name not found", bankName);
        }
        return bank.get();
    }

    @Override
    public Bank getBankByBin(String binNumber) {
        Optional<Bank> bank = bankRepository.findBankByBin(binNumber);
        if (bank.isEmpty()) {
            throw new EntityNotFoundException("Bank with such bin not found", binNumber);
        }
        return bank.get();
    }

    @Override
    public void updateBankInfo(Bank bankForUpdate) {
        if (bankForUpdate.getId() == null
                || !bankRepository.existsById(bankForUpdate.getId())) {
            throw new EntityNotFoundException("Bank isn't present", bankForUpdate.getId());
        }
        bankRepository.save(bankForUpdate);
    }

    @Override
    public void deleteBankById(Integer bankId) {
        if (bankId == null || !bankRepository.existsById(bankId)) {
            throw new EntityNotFoundException("Banks with such id isn't exist", bankId);
        }
        bankRepository.deleteById(bankId);
    }
}
