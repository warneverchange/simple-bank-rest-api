package com.evilcorp.api.controllers;


import com.evilcorp.controllers.BankController;
import com.evilcorp.controllers.LegalTypeController;
import com.evilcorp.entities.LegalType;
import com.evilcorp.exceptions.EntityAlreadyExistException;
import com.evilcorp.responses.EntityAlreadyExistResponse;
import com.evilcorp.responses.EntityNotFoundResponse;
import com.evilcorp.services.LegalTypeServiceImpl;
import com.evilcorp.services.LegalTypesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = LegalTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class LegalTypeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LegalTypeServiceImpl legalTypesService;

    private LegalType legalType;
    private List<LegalType> legalTypes;

    @BeforeEach
    public void init() {
        legalType = LegalType
                .builder()
                .id(1)
                .name("OOO")
                .build();
        legalTypes = new LinkedList<>();
        legalTypes.add(legalType);
    }

    @Test
    public void LegalTypeController_getAllLegalTypes_ReturnsListOfLegalTypes() throws Exception {
        when(legalTypesService.getAllLegalTypes()).thenReturn(legalTypes);
        var response = mockMvc.perform(
                get("/api/v1/legal_types/all")
                        .contentType(MediaType.APPLICATION_JSON)
        );
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        legalTypes
                )));
    }

    @Test
    public void LegalTypeController_registerNewLegalType_ReturnsLegalType_1() throws Exception {
        when(legalTypesService.registerNewLegalType(legalType)).thenReturn(legalType);
        var response = mockMvc.perform(
                post("/api/v1/legal_types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(legalType))
        );
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/legal_types/1"))
                .andExpect(content().json(objectMapper
                        .writeValueAsString(
                                legalType
                        )));
    }

    @Test
    public void LegalTypeController_registerNewLegalType_ReturnsLegalType_2() throws Exception {
        var msg = "Such legal type already exist";
        when(legalTypesService.registerNewLegalType(legalType)).thenThrow(new EntityAlreadyExistException(msg, legalType));
        var response = mockMvc.perform(
                post("/api/v1/legal_types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(legalType))
        );
        response.andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new EntityAlreadyExistResponse(msg, legalType)
                )));
    }
}
