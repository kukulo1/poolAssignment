package ru.kukulo1.test_assignment.reservation.records;

import java.sql.Timestamp;

public record AddReservationRecord(Long clientID, Timestamp timestamp) {
}
