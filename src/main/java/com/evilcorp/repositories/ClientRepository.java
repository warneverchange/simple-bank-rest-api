package com.evilcorp.repositories;

import com.evilcorp.entities.Client;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByName(String name);
    Optional<Client> findByShortName(String shortName);

    boolean existsByNameOrShortName(String clientName, String shortClientName);
}
