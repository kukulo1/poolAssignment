package ru.kukulo1.test_assignment.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kukulo1.test_assignment.reservation.records.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v0/pool/timetable")
public class ReservationController {

    @Autowired
    public ReservationService reservationService;

    @GetMapping("/all")
    public ResponseEntity<List<GetReservationsByDateRecord>> getAllReservationsByDate(@RequestParam Date date) {
        return reservationService.getAllReservationsByDate(date);
    }
    @GetMapping("/available")
    public ResponseEntity<List<GetReservationsByDateInterface>> getAvailableReservationByDate(@RequestParam Date date) {
        return reservationService.getAvailableSlotsByDate(date);
    }
    @PostMapping("/reserve")
    public ResponseEntity<String> addReservation(@RequestBody AddReservationRecord addReservationRecord) {
        return reservationService.reserveSessionHour(addReservationRecord);
    }
    @DeleteMapping("/cancel")
    public ResponseEntity<String> cancelReservation(@RequestBody CancelReservationRecord cancelReservationRecord) {
        return reservationService.cancelReservation(cancelReservationRecord);
    }

    @GetMapping("/byname")
    public ResponseEntity<List<GetReservationByParameterRecord>> getReservationsByName(@RequestParam String name) {
        return reservationService.getReservationsByName(name);
    }

    @GetMapping("/bydate")
    public ResponseEntity<List<GetReservationByParameterRecord>> getReservationsByDate(@RequestParam Date date) {
        return reservationService.getReservationsByDate(date);
    }
}
