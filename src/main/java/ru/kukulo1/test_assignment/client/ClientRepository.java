package ru.kukulo1.test_assignment.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kukulo1.test_assignment.client.dto.GetClientsDTO;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c.id AS id, c.name AS name FROM Client c")
    List<GetClientsDTO> findAllClients();
}
