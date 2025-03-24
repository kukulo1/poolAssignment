package ru.kukulo1.test_assignment.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kukulo1.test_assignment.client.dto.AddClientDTO;
import ru.kukulo1.test_assignment.client.dto.GetClientsDTO;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<GetClientsDTO> getAllClients() {
        return clientRepository.findAllClients();
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


    public ResponseEntity<String> addClient(AddClientDTO clientDTO) {
        Client client = new Client(clientDTO);
        if (client.isValid()) {
            clientRepository.save(client);
            return new ResponseEntity<>("Client has been successfully added!", HttpStatus.OK);
        }
        return new ResponseEntity<>(String.join("\n", client.getInvalidFields()), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> updateClient(Client client) {
        if (clientRepository.findById(client.getId()).isEmpty()) {
            return new ResponseEntity<>("The client with the submitted ID does not exist :(", HttpStatus.BAD_REQUEST);
        }
        if (!client.isValid()) {
            return new ResponseEntity<>(String.join("\n", client.getInvalidFields()), HttpStatus.BAD_REQUEST);
        }
        clientRepository.save(client);
        return new ResponseEntity<>("The client has been successfully updated!", HttpStatus.OK);
    }
}
