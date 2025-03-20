package ru.kukulo1.test_assignment.client.records;

import ru.kukulo1.test_assignment.client.Client;

public record GetClientsRecord(Long id, String name) {
    public GetClientsRecord(Client client) {
        this(client.getId(), client.getName());
    }
}
