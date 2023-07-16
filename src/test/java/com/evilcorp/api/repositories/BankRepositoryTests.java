package com.evilcorp.api.repositories;

import com.evilcorp.entities.Bank;
import com.evilcorp.repositories.BankRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BankRepositoryTests {
    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void init() {
        var bank = Bank
                .builder()
                .bin("044525716")
                .name("Evil corp bank")
                .build();
        bankRepository.save(bank);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void BankRepository_findBankByBin_ReturnsOptionalBank_1() {
        var result = bankRepository.findBankByBin("044525716");
        Assertions.assertThat(result).isPresent();
    }
    @Test
    public void BankRepository_findBankByBin_ReturnsOptionalBank_2() {
        var result = bankRepository.findBankByBin("043525716");
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void BankRepository_findByName_ReturnsOptionalBank_1() {
        var result = bankRepository.findByName("Evil corp bank");
        Assertions.assertThat(result).isPresent();
    }
    @Test
    public void BankRepository_findByName_ReturnsOptionalBank_2() {
        var result = bankRepository.findByName("Evil crop bank");
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void BankRepository_existsByBinOrName_ReturnsBoolean_1() {
        var result = bankRepository.existsByBinOrName("044525716", "Evil crop bank");
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void BankRepository_existsByBinOrName_ReturnsBoolean_2() {
        var result = bankRepository.existsByBinOrName("043525716", "Evil corp bank");
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void BankRepository_existsByBinOrName_ReturnsBoolean_3() {
        var result = bankRepository.existsByBinOrName("044525716", "Evil corp bank");
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void BankRepository_existsByBinOrName_ReturnsBoolean_4() {
        var result = bankRepository.existsByBinOrName("043525716", "Evil crop bank");
        Assertions.assertThat(result).isFalse();
    }
}

