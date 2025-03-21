package ru.kukulo1.test_assignment.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kukulo1.test_assignment.reservation.records.GetReservationsByDateInterface;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT sh.dateTime AS time, COUNT(r.id) AS count " +
            "FROM SessionHour sh " +
            "JOIN Reservation r ON sh.id = r.sessionHour.id " +
            "WHERE DATE(sh.dateTime) = :date " +
            "GROUP BY sh.dateTime")
    List<GetReservationsByDateInterface> findOccupiedSlotsByDate(@Param("date") LocalDate date);

    @Query("SELECT sh.dateTime AS time, (10 - COUNT(r.id)) AS count " +
            "FROM SessionHour sh " +
            "LEFT JOIN Reservation r ON sh.id = r.sessionHour.id " +
            "WHERE DATE(sh.dateTime) = :date " +
            "GROUP BY sh.dateTime " +
            "HAVING COUNT(r.id) < 10")
    List<GetReservationsByDateInterface> findAvailableSlotsByDate(@Param("date") LocalDate date);

    @Query("SELECT CASE WHEN sh.id IS NOT NULL THEN COUNT(r.id) < 10 ELSE false END " +
            "FROM SessionHour sh " +
            "LEFT JOIN Reservation r ON sh.id = r.sessionHour.id " +
            "WHERE sh.dateTime = :datetime")
    boolean isSlotAvailable(@Param("datetime") LocalDateTime localDateTime);

    List<Reservation> findByClientName(String name);

    @Query("SELECT r FROM Reservation r WHERE DATE(r.sessionHour.dateTime) = :date")
    List<Reservation> findByDate(@Param("date") LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE r.client.name = :name AND DATE(r.sessionHour.dateTime) = :date")
    List<Reservation> findByClientNameAndDate(@Param("name") String name, @Param("date") LocalDate date);
}
