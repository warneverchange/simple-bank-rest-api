package com.evilcorp.api.repositories;

import com.evilcorp.entities.Bank;
import com.evilcorp.entities.BankDeposit;
import com.evilcorp.entities.Client;
import com.evilcorp.entities.LegalType;
import com.evilcorp.repositories.BankRepository;
import com.evilcorp.repositories.ClientRepository;
import com.evilcorp.repositories.DepositRepository;
import com.evilcorp.repositories.LegalTypeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.sql.Timestamp;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DepositRepositoryTests {
    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private LegalTypeRepository legalTypeRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    TestEntityManager entityManager;

    public static double annualRate = 1.54;
    public static Timestamp openingDate = new Timestamp(System.currentTimeMillis());
    public static Integer period = 12;


    @BeforeEach
    public void init() {
        var firstBank = Bank.builder()
                .name("Evil corp bank")
                .bin("044525716")
                .build();

        firstBank = bankRepository.save(firstBank);

        var firstLegalType = LegalType
                .builder()
                .name("Phisical")
                .build();
        firstLegalType = legalTypeRepository.save(firstLegalType);

        var firstClient = Client.builder().name("Konstantin")
                .shortName("Kostya")
                .address("Chkalova, 44")
                .legalType(firstLegalType)
                .build();
        firstClient = clientRepository.save(firstClient);

        var firstDeposit = BankDeposit.builder()
                .bank(firstBank)
                .client(firstClient)
                .annualRate(annualRate)
                .openingDate(openingDate)
                .period(period)
                .build();

        depositRepository.save(firstDeposit);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void DepositRepository_findAllByClient_ReturnsListOfBankDeposit() {
        var client = clientRepository.findByName("Konstantin").orElseThrow();
        List<BankDeposit> depositsByClient = depositRepository.findAllByClient(client);
        Assertions.assertThat(depositsByClient.size()).isEqualTo(client.getDeposits().size());
    }

    @Test
    public void DepositRepository_findAllByBank_ReturnsListOfBankDeposit() {
        var bank = bankRepository.findBankByBin("044525716").orElseThrow();
        List<BankDeposit> depositsByBank = depositRepository.findAllByBank(bank);
        Assertions.assertThat(depositsByBank.size()).isEqualTo(bank.getDeposits().size());
    }

    @Test
    public void DepositRepository_existsByClientAndBankAndOpeningDateAndAnnualRateAndPeriod_ReturnsBoolean_1() {
        var client = clientRepository.findByName("Konstantin").orElseThrow();
        var bank = bankRepository.findBankByBin("044525716").orElseThrow();
        var result = depositRepository.existsByClientAndBankAndOpeningDateAndAnnualRateAndPeriod(
                client,
                bank,
                openingDate,
                annualRate,
                period
        );
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void DepositRepository_existsByClientAndBankAndOpeningDateAndAnnualRateAndPeriod_ReturnsBoolean_2() {
        var client = clientRepository.findByName("Konstantin").orElseThrow();
        var bank = bankRepository.findBankByBin("044525716").orElseThrow();
        var result = depositRepository.existsByClientAndBankAndOpeningDateAndAnnualRateAndPeriod(
                client,
                bank,
                openingDate,
                annualRate + 1,
                period
        );
        Assertions.assertThat(result).isFalse();
    }

}
