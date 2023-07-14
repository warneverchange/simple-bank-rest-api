package com.evilcorp.repositories;

import com.evilcorp.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> {
    Optional<Bank> findBankByBin(String binNumber);
    Optional<Bank> findByName(String bankName);

    boolean existsByBinOrName(String bin, String name);
}
