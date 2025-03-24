package ru.kukulo1.test_assignment.sessionhour;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessionHourRepository extends JpaRepository<SessionHour, Long> {
    Optional<SessionHour> findByDateTime(LocalDateTime dateTime);

    @Query("SELECT sh FROM SessionHour sh WHERE DATE(sh.dateTime) = :date")
    List<SessionHour> findByDate(@Param("date") LocalDate date);

    @Query("SELECT CASE WHEN COUNT(sh) > 0 THEN true ELSE false END FROM SessionHour sh WHERE DATE(sh.dateTime) = :date")
    boolean existsByDate(@Param("date") LocalDate date);
}
