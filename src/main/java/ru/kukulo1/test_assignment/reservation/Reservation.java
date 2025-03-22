package ru.kukulo1.test_assignment.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kukulo1.test_assignment.client.Client;
import ru.kukulo1.test_assignment.sessionhour.SessionHour;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("orderId")
    private Long id;

    @ManyToOne
    private Client client;

    @ManyToOne
    private SessionHour sessionHour;

    public Reservation(Client client, SessionHour sessionHour) {
        this.client = client;
        this.sessionHour = sessionHour;
    }
}
