package com.evilcorp.api.repositories;

import com.evilcorp.entities.LegalType;
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
public class LegalTypeRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LegalTypeRepository legalTypeRepository;

    @BeforeEach
    public void init() {
        var legalType = LegalType
                .builder()
                .name("OOO")
                .build();
        legalTypeRepository.save(legalType);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void LegalTypeRepository_existsByName_ReturnsBoolean_1() {
        var result = legalTypeRepository.existsByName("OOO");
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void LegalTypeRepository_existsByName_ReturnsBoolean_2() {
        var result = legalTypeRepository.existsByName("OO");
        Assertions.assertThat(result).isFalse();
    }
}
