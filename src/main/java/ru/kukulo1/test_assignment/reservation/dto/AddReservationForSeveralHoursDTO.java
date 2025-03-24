package ru.kukulo1.test_assignment.reservation.dto;

import java.time.LocalDateTime;


public record AddReservationForSeveralHoursDTO(Long clientId, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd) {
}
