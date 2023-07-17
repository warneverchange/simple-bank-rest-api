package com.evilcorp.services;

import com.evilcorp.entities.LegalType;
import com.evilcorp.exceptions.EntityAlreadyExistException;
import com.evilcorp.exceptions.EntityNotFoundException;
import com.evilcorp.repositories.LegalTypeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LegalTypeServiceImpl implements LegalTypesService {
    private final LegalTypeRepository legalTypeRepository;

    @Override
    public LegalType registerNewLegalType(LegalType legalType) {
        if (legalTypeRepository.existsByName(legalType.getName())) {
            throw new EntityAlreadyExistException("Such legal type already exist", legalType);
        }
        return legalTypeRepository.save(legalType);
    }

    @Override
    public List<LegalType> getAllLegalTypes() {
        return legalTypeRepository.findAll();
    }

    @Override
    public void updateLegalTypeInfo(LegalType legalType) {
        if (legalType.getId() == null
                || legalTypeRepository.findById(legalType.getId()).isEmpty()) {
            throw new EntityNotFoundException("Legal type isn't present", legalType.getId());
        }
        legalTypeRepository.save(legalType);
    }

    @Override
    public LegalType getLegalTypeById(Integer legalTypeId) {
        Optional<LegalType> foundLegalType = legalTypeRepository.findById(legalTypeId);
        if (foundLegalType.isEmpty()) {
            throw new EntityNotFoundException("Such legal type isn't exist", legalTypeId);
        }
        return foundLegalType.orElseThrow();
    }

    @Override
    public void deleteLegalTypeById(Integer legalTypeId) {
        if (legalTypeId == null || !legalTypeRepository.existsById(legalTypeId)) {
            throw new EntityNotFoundException("Entity with such identify not found", legalTypeId);
        }
        legalTypeRepository.deleteById(legalTypeId);
    }
}
