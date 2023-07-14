package com.evilcorp.services;

import com.evilcorp.entities.Client;

import java.util.List;

public interface ClientService {

    Client getClientById(Integer clientId);
    Client registerNewClient(Client client);
    List<Client> getAllClients();
    Client getClientByClientName(String clientName);
    Client getClientByClientShortName(String clientShortName);
}
