package ru.kukulo1.test_assignment.reservation.records;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record AddReservationRecord(Long clientId, @JsonProperty("datetime") LocalDateTime dateTime) {
}
