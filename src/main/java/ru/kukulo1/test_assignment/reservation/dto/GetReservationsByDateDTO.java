package ru.kukulo1.test_assignment.reservation.dto;

import java.time.LocalDateTime;

public interface GetReservationsByDateDTO {
    LocalDateTime getTime();
    int getCount();
}
