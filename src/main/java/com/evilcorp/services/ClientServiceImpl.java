package com.evilcorp.services;

import com.evilcorp.entities.Client;
import com.evilcorp.exceptions.ClientNotFoundException;
import com.evilcorp.exceptions.EntityAlreadyExistException;
import com.evilcorp.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService{
    public final ClientRepository clientRepository;

    @Override
    public Client getClientById(Integer clientId) {
        Optional<Client> foundClient = clientRepository.findById(clientId);
        if (foundClient.isEmpty()) {
            throw new ClientNotFoundException("Client with such id not found", clientId);
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
            throw new ClientNotFoundException("Client with such name not found", clientName);
        }
        return foundClient.get();
    }

    @Override
    public Client getClientByClientShortName(String clientShortName) {
        Optional<Client> foundClient = clientRepository.findByShortName(clientShortName);
        if (foundClient.isEmpty()) {
            throw new ClientNotFoundException("Client with such short name not found", clientShortName);
        }
        return foundClient.get();
    }
}
