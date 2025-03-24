package ru.kukulo1.test_assignment.reservation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record AddReservationDTO(Long clientId, @JsonProperty("datetime") LocalDateTime dateTime) {
}
