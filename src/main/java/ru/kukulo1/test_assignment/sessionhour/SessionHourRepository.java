package ru.kukulo1.test_assignment.sessionhour;

import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.Optional;

public interface SessionHourRepository extends JpaRepository<SessionHour, Long> {
    Optional<SessionHour> findByDateTime(Timestamp dateTime);
}
