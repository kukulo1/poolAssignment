package ru.kukulo1.test_assignment.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kukulo1.test_assignment.client.dto.AddClientDTO;
import ru.kukulo1.test_assignment.client.dto.GetClientsDTO;

import java.util.List;


@RestController
@RequestMapping("/api/v0/pool/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/all")
    public ResponseEntity<List<GetClientsDTO>> getAllClients() {
        return new ResponseEntity<>(clientService.getAllClients(), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<Client> getClient(@RequestParam Long id) {
        return clientService.getClient(id);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addClient(@RequestBody AddClientDTO client) {
        return clientService.addClient(client);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateClient(@RequestBody Client client) {
        return clientService.updateClient(client);
    }


}
