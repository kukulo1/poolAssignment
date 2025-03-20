package ru.kukulo1.test_assignment.reservation.records;

import java.sql.Time;

public record GetReservationsByDateRecord(Time time, Integer count) {
}
