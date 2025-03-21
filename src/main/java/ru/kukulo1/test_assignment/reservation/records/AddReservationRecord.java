package ru.kukulo1.test_assignment.reservation.records;

import java.time.LocalDateTime;

public record AddReservationRecord(Long clientID, LocalDateTime localDateTime) {
}
