package ru.kukulo1.test_assignment.reservation.dto;

import java.time.LocalDateTime;

public interface GetReservationByParameterDTO {
    Long getClientId();
    String getName();
    LocalDateTime getLocalDateTime();
    Long getReservationId();
}

