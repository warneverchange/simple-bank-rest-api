package com.evilcorp.services;

import com.evilcorp.entities.Client;
import com.evilcorp.exceptions.EntityAlreadyExistException;
import com.evilcorp.exceptions.EntityNotFoundException;
import com.evilcorp.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {
    public final ClientRepository clientRepository;

    @Override
    public Client getClientById(Integer clientId) {
        Optional<Client> foundClient = clientRepository.findById(clientId);
        if (foundClient.isEmpty()) {
            throw new EntityNotFoundException("Client with such id not found", clientId);
        }
        return foundClient.get();
    }

    @Override
    public Client registerNewClient(Client client) {
        if (clientRepository.existsByNameOrShortName(client.getName(), client.getShortName())) {
            throw new EntityAlreadyExistException("Client with such name or short name already exist", client);
        }
        return clientRepository.save(client);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client getClientByClientName(String clientName) {
        Optional<Client> foundClient = clientRepository.findByName(clientName);
        if (foundClient.isEmpty()) {
            throw new EntityNotFoundException("Client with such name not found", clientName);
        }
        return foundClient.get();
    }

    @Override
    public Client getClientByClientShortName(String clientShortName) {
        Optional<Client> foundClient = clientRepository.findByShortName(clientShortName);
        if (foundClient.isEmpty()) {
            throw new EntityNotFoundException("Client with such short name not found", clientShortName);
        }
        return foundClient.get();
    }

    @Override
    public void updateClientInfo(Client clientForUpdate) {
        if (clientForUpdate.getId() == null
                || !clientRepository.existsById(clientForUpdate.getId())) {
            throw new EntityNotFoundException("Client isn't present", clientForUpdate.getId());
        }
        clientRepository.save(clientForUpdate);
    }

    @Override
    public void deleteClientById(Integer clientId) {
        if (clientId == null || !clientRepository.existsById(clientId)) {
            throw new EntityNotFoundException("Entity with such id isn't exist", clientId);
        }
        clientRepository.deleteById(clientId);
    }
}
