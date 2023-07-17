package com.evilcorp.api.servcies;

import com.evilcorp.entities.Client;
import com.evilcorp.entities.LegalType;
import com.evilcorp.exceptions.EntityAlreadyExistException;
import com.evilcorp.exceptions.EntityNotFoundException;
import com.evilcorp.repositories.ClientRepository;
import com.evilcorp.services.ClientServiceImpl;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTests {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    private List<Client> clientList;

    private Client client;
    private static final int legalTypeId = 1;
    private static final int clientId = 1;

    @BeforeEach
    public void init() {
        var legalType = LegalType
                .builder()
                .id(legalTypeId)
                .name("Phisical face")
                .build();

        var client = Client
                .builder()
                .id(clientId)
                .name("Konstantin")
                .shortName("Kostya")
                .address("Chkalova, 44")
                .legalType(legalType)
                .build();
        this.client = client;
        this.clientList = new LinkedList<>();
        clientList.add(client);
    }

    @Test
    public void ClientServiceImpl_getClientById_ReturnsClient_1() {
        given(clientRepository.findById(clientId)).willReturn(Optional.of(client));
        Assertions.assertThat(clientService.getClientById(client.getId())).isEqualTo(client);
    }

    @Test
    public void ClientServiceImpl_getClientById_ReturnsClient_2() {
        given(clientRepository.findById(clientId)).willReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> clientService.getClientById(client.getId()));
    }

    @Test
    public void ClientServiceImpl_registerNewClient_ReturnsClient_1() {
        given(clientRepository.existsByNameOrShortName(client.getName(), client.getShortName())).willReturn(true);
        org.junit.jupiter.api.Assertions.assertThrows(EntityAlreadyExistException.class, () -> clientService.registerNewClient(client));
    }

    @Test
    public void ClientServiceImpl_registerNewClient_ReturnsClient_2() {
        given(clientRepository.existsByNameOrShortName(client.getName(), client.getShortName())).willReturn(false);
        var passedClient = Client
                .builder()
                .name(client.getName())
                .shortName(client.getShortName())
                .address(client.getAddress())
                .legalType(client.getLegalType())
                .build();
        given(clientRepository.save(passedClient)).willReturn(client);
        Assertions.assertThat(clientService.registerNewClient(passedClient)).isEqualTo(client);
    }

    @Test
    public void ClientServiceImpl_getAllClients_ReturnsListOfClients() {
        given(clientRepository.findAll()).willReturn(clientList);
        Assertions.assertThat(clientService.getAllClients()).isEqualTo(clientList);
    }

    @Test
    public void ClientServiceImpl_getClientByClientName_ReturnsClient_1() {
        given(clientRepository.findByName(client.getName())).willReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> clientService.getClientByClientName(client.getName()));
    }

    @Test
    public void ClientServiceImpl_getClientByClientName_ReturnsClient_2() {
        given(clientRepository.findByName(client.getName())).willReturn(Optional.of(client));
        Assertions.assertThat(clientService.getClientByClientName(client.getName())).isEqualTo(client);
    }

    @Test
    public void ClientServiceImpl_getClientByClientShortName_ReturnsClient_1() {
        given(clientRepository.findByShortName(client.getShortName())).willReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> clientService.getClientByClientShortName(client.getShortName()));
    }
    @Test
    public void ClientServiceImpl_getClientByClientShortName_ReturnsClient_2() {
        given(clientRepository.findByShortName(client.getShortName())).willReturn(Optional.of(client));
        Assertions.assertThat(clientService.getClientByClientShortName(client.getShortName())).isEqualTo(client);
    }

    @Test
    public void ClientServiceImpl_deleteClientById_ReturnsVoid_1() {
        given(clientRepository.existsById(client.getId())).willReturn(true);
        doAnswer(invocation -> Assertions.assertThat(invocation.getArgument(0).equals(client.getId())))
                .when(clientRepository)
                .deleteById(any());
        clientService.deleteClientById(client.getId());
    }

    @Test
    public void ClientServiceImpl_deleteClientById_ReturnsVoid_2() {
        doThrow(EntityNotFoundException.class)
                .when(clientRepository)
                .existsById(any());
        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> clientService.deleteClientById(client.getId()));
    }

    @Test
    public void ClientServiceImpl_updateClientInfo_ReturnsVoid_1() {
        given(clientRepository.existsById(any())).willReturn(true);
        doAnswer(invocation -> {
            Assertions.assertThat(((Client) invocation.getArgument(0)).getId()).isEqualTo(client.getId());
            return invocation.getArgument(0);
        })
                .when(clientRepository)
                .save(client);
        clientService.updateClientInfo(client);
    }

    @Test
    public void ClientServiceImpl_updateClientInfo_ReturnsVoid_2() {
        given(clientRepository.existsById(any())).willReturn(false);
        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> clientService.updateClientInfo(client));
    }
}
