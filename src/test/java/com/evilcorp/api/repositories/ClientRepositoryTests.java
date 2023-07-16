package com.evilcorp.api.repositories;

import com.evilcorp.entities.Client;
import com.evilcorp.entities.LegalType;
import com.evilcorp.repositories.ClientRepository;
import com.evilcorp.repositories.LegalTypeRepository;
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
public class ClientRepositoryTests {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LegalTypeRepository legalTypeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void init() {
        var legalType = LegalType
                .builder()
                .name("Phisical")
                .build();
        legalType = legalTypeRepository.save(legalType);

        var client = Client
                .builder()
                .name("Konstantin")
                .shortName("Kostya")
                .address("Chkalova, 44")
                .legalType(legalType)
                .build();
        clientRepository.save(client);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void ClientRepository_findByName_ReturnsOptionalClient_1() {
        var result = clientRepository.findByName("Konstantin");
        Assertions.assertThat(result).isPresent();
    }

    @Test
    public void ClientRepository_findByName_ReturnsOptionalClient_2() {
        var result = clientRepository.findByName("Konstanti");
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void ClientRepository_findByShortName_ReturnsOptionalClient_1() {
        var result = clientRepository.findByShortName("Kostya");
        Assertions.assertThat(result).isPresent();
    }

    @Test
    public void ClientRepository_findByShortName_ReturnsOptionalClient_2() {
        var result = clientRepository.findByShortName("Kstya");
        Assertions.assertThat(result).isEmpty();
    }


    @Test
    public void ClientRepository_existsByNameOrShortName_ReturnsBoolean_1() {
        var result = clientRepository.existsByNameOrShortName("Konstantin", "Kstya");
        Assertions.assertThat(result).isTrue();
    }
    @Test
    public void ClientRepository_existsByNameOrShortName_ReturnsBoolean_2() {
        var result = clientRepository.existsByNameOrShortName("Knstantin", "Kostya");
        Assertions.assertThat(result).isTrue();
    }
    @Test
    public void ClientRepository_existsByNameOrShortName_ReturnsBoolean_3() {
        var result = clientRepository.existsByNameOrShortName("Konstantin", "Kostya");
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void ClientRepository_existsByNameOrShortName_ReturnsBoolean_4() {
        var result = clientRepository.existsByNameOrShortName("Knstantin", "ostya");
        Assertions.assertThat(result).isFalse();
    }
}
