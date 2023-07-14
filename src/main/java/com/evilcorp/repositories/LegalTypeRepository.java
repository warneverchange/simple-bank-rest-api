package com.evilcorp.repositories;


import com.evilcorp.entities.LegalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LegalTypeRepository extends JpaRepository<LegalType, Integer> {
    boolean existsByName(String name);
}
