package ru.kukulo1.test_assignment.client.records;

import ru.kukulo1.test_assignment.client.Client;

public record AddClientRecord(String name, String phone, String email) {
    public AddClientRecord(Client client) {
        this(client.getName(), client.getPhone(), client.getEmail());
    }
}
