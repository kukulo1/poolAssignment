package ru.kukulo1.test_assignment.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kukulo1.test_assignment.reservation.dto.GetReservationByParameterDTO;
import ru.kukulo1.test_assignment.reservation.dto.GetReservationsByDateDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT sh.dateTime AS time, COUNT(r.id) AS count " +
            "FROM SessionHour sh " +
            "JOIN Reservation r ON sh.id = r.sessionHour.id " +
            "WHERE DATE(sh.dateTime) = :date " +
            "GROUP BY sh.dateTime")
    List<GetReservationsByDateDTO> findOccupiedSlotsByDate(@Param("date") LocalDate date);

    @Query("SELECT sh.dateTime AS time, (10 - COUNT(r.id)) AS count " +
            "FROM SessionHour sh " +
            "LEFT JOIN Reservation r ON sh.id = r.sessionHour.id " +
            "WHERE DATE(sh.dateTime) = :date " +
            "GROUP BY sh.dateTime " +
            "HAVING COUNT(r.id) < 10")
    List<GetReservationsByDateDTO> findAvailableSlotsByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(r.id) < 10 " +
            "FROM SessionHour sh " +
            "LEFT JOIN Reservation r ON sh.id = r.sessionHour.id " +
            "WHERE sh.dateTime = :datetime")
    boolean isSlotAvailable(@Param("datetime") LocalDateTime localDateTime);

    @Query("SELECT r.client.id AS clientId, r.client.name AS name, r.sessionHour.dateTime AS localDateTime, r.id AS reservationId " +
            "FROM Reservation r WHERE r.client.name = :name")
    List<GetReservationByParameterDTO> findByClientName(@Param("name") String name);

    @Query("SELECT r.client.id AS clientId, r.client.name AS name, r.sessionHour.dateTime AS localDateTime, r.id AS reservationId " +
            "FROM Reservation r WHERE DATE(r.sessionHour.dateTime) = :date")
    List<GetReservationByParameterDTO> findByDate(@Param("date") LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE r.client.id = :id AND DATE(r.sessionHour.dateTime) = :date")
    List<Reservation> findByClientIdAndDate(@Param("id") Long id, @Param("date") LocalDate date);
}
