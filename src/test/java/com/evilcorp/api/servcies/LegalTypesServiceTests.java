package com.evilcorp.api.servcies;

import com.evilcorp.entities.LegalType;
import com.evilcorp.exceptions.EntityAlreadyExistException;
import com.evilcorp.repositories.LegalTypeRepository;
import com.evilcorp.services.LegalTypeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LegalTypesServiceTests {
    @Mock
    private LegalTypeRepository legalTypeRepository;

    @InjectMocks
    private LegalTypeServiceImpl legalTypeService;

    private LegalType legalType;
    private List<LegalType> legalTypes;


    @BeforeEach
    public void init() {
        legalType = LegalType
                .builder()
                .name("OOO")
                .build();
        legalTypes = new LinkedList<>();
        legalTypes.add(legalType);
    }

    @Test
    public void LegalTypeServiceImpl_registerNewLegalType_ReturnsLegalType_1() {
        given(legalTypeRepository.existsByName(legalType.getName())).willReturn(true);
        Assertions.assertThrows(EntityAlreadyExistException.class, () -> legalTypeService.registerNewLegalType(legalType));
    }

    @Test
    public void LegalTypeServiceImpl_registerNewLegalType_ReturnsLegalType_2() {
        given(legalTypeRepository.existsByName(legalType.getName())).willReturn(false);
        var passedLegalType = LegalType
                .builder()
                .name(legalType.getName())
                .build();
        given(legalTypeRepository.save(passedLegalType)).willReturn(legalType);
        org.assertj.core.api.Assertions.assertThat(legalTypeService.registerNewLegalType(passedLegalType)).isEqualTo(legalType);
    }

    @Test
    public void LegalTypeServiceImpl_getAllLegalTypes_ReturnsListOfLegalTypes() {
        given(legalTypeRepository.findAll()).willReturn(legalTypes);
        org.assertj.core.api.Assertions.assertThat(legalTypeService.getAllLegalTypes()).isEqualTo(legalTypes);
    }
}
