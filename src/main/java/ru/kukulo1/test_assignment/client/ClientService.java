package ru.kukulo1.test_assignment.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kukulo1.test_assignment.client.records.AddClientRecord;
import ru.kukulo1.test_assignment.client.records.GetClientsRecord;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<GetClientsRecord> getAllClients() {
        return clientRepository.findAll().stream()
                .map(GetClientsRecord::new)
                .collect(Collectors.toList());
    }

    public ResponseEntity<Client> getClient(Long clientId) {
        Optional<Client> clientOptional = clientRepository.findById(clientId);

        return clientOptional.isPresent() ?
                new ResponseEntity<>(
                        clientOptional.get(),
                        HttpStatus.OK
                ) :
                new ResponseEntity<>(
                        null,
                        HttpStatus.NOT_FOUND
                );

    }


    public ResponseEntity<String> addClient(AddClientRecord clientRecord) {
        Client client = new Client(clientRecord);
        if (client.isValid()) {
            clientRepository.save(client);
            return new ResponseEntity<>("Клиент успешно добавлен!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Одно или несколько полей входных данных некорректны!", HttpStatus.BAD_REQUEST);
    }
    public ResponseEntity<String> updateClient(Client client) {
        if (clientRepository.findById(client.getId()).isEmpty()) {
            return new ResponseEntity<>("Клиента с представленным ID не существует :(", HttpStatus.BAD_REQUEST);
        }
        if (!client.isValid()) {
            return new ResponseEntity<>("Одно или несколько полей входных данных некорректны!", HttpStatus.BAD_REQUEST);
        }
        clientRepository.save(client);
        return new ResponseEntity<>("Клиент успешно обновлён!", HttpStatus.OK);
    }
}
