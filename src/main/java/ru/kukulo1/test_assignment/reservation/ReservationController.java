package ru.kukulo1.test_assignment.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kukulo1.test_assignment.reservation.dto.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v0/pool/timetable")
public class ReservationController {

    @Autowired
    public ReservationService reservationService;

    @GetMapping("/all")
    public ResponseEntity<List<GetReservationsByDateDTO>> getAllReservationsByDate(@RequestParam LocalDate date) {
        return reservationService.getAllReservationsByDate(date);
    }
    @GetMapping("/available")
    public ResponseEntity<List<GetReservationsByDateDTO>> getAvailableReservationByDate(@RequestParam LocalDate date) {
        return reservationService.getAvailableSlotsByDate(date);
    }
    @PostMapping("/reserve")
    public ResponseEntity<String> addReservation(@RequestBody AddReservationDTO addReservationDTO) {
        return reservationService.reserveSessionHour(addReservationDTO);
    }
    @PostMapping("/reserveInterval")
    public ResponseEntity<String> addReservation(@RequestBody AddReservationForSeveralHoursDTO addReservationForSeveralHoursDTO) {
        return reservationService.reserveSessionHourInterval(addReservationForSeveralHoursDTO);
    }
    @DeleteMapping("/cancel")
    public ResponseEntity<String> cancelReservation(@RequestBody CancelReservationDTO cancelReservationDTO) {
        return reservationService.cancelReservation(cancelReservationDTO);
    }

    @GetMapping("/byname")
    public ResponseEntity<List<GetReservationByParameterDTO>> getReservationsByName(@RequestParam String name) {
        return reservationService.getReservationsByName(name);
    }

    @GetMapping("/bydate")
    public ResponseEntity<List<GetReservationByParameterDTO>> getReservationsByDate(@RequestParam LocalDate date) {
        return reservationService.getReservationsByDate(date);
    }
}
