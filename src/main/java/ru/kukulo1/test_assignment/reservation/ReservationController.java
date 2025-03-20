package ru.kukulo1.test_assignment.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kukulo1.test_assignment.reservation.records.GetReservationsByDateRecord;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v0/pool/timetable")
public class ReservationController {

    @Autowired
    public ReservationService reservationService;

    @GetMapping("/all")
    public ResponseEntity<List<GetReservationsByDateRecord>> getAllReservationsByDate(Date date) {
        return reservationService.getAllReservationsByDate(date);
    }
    @GetMapping("/available")
    public ResponseEntity<List<GetReservationsByDateRecord>> getAvailableReservationByDate(Date date) {
        return reservationService.getAvailableSlotsByDate(date);
    }
}
