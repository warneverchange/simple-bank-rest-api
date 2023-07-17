package com.evilcorp.services;

import com.evilcorp.entities.LegalType;
import org.springframework.stereotype.Service;

import java.util.List;

public interface LegalTypesService {
    LegalType registerNewLegalType(LegalType legalType);
    List<LegalType> getAllLegalTypes();

    void updateLegalTypeInfo(LegalType legalType);

    LegalType getLegalTypeById(Integer legalTypeId);

    void deleteLegalTypeById(Integer legalTypeId);
}
