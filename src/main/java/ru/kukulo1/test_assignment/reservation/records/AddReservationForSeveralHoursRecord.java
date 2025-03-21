package ru.kukulo1.test_assignment.reservation.records;

import java.time.LocalDateTime;


public record AddReservationForSeveralHoursRecord(Long clientID, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd) {
}
