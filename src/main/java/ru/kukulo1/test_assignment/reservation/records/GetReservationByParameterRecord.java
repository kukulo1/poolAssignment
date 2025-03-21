package ru.kukulo1.test_assignment.reservation.records;

import ru.kukulo1.test_assignment.reservation.Reservation;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public record GetReservationByParameterRecord(Long clientID, String name, LocalDateTime localDateTime, Long reservationID) {
    public GetReservationByParameterRecord(Reservation reservation) {
        this(reservation.getClient().getId(),
                reservation.getClient().getName(),
                reservation.getSessionHour().getDateTime(),
                reservation.getId());
    }
}
