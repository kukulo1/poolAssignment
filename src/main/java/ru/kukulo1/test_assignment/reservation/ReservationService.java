package ru.kukulo1.test_assignment.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kukulo1.test_assignment.client.Client;
import ru.kukulo1.test_assignment.client.ClientRepository;
import ru.kukulo1.test_assignment.reservation.dto.*;
import ru.kukulo1.test_assignment.sessionhour.SessionHour;
import ru.kukulo1.test_assignment.sessionhour.SessionHourRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private SessionHourRepository sessionHourRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ClientRepository clientRepository;

    public ResponseEntity<List<GetReservationsByDateDTO>> getAllReservationsByDate(LocalDate date) {
        return new ResponseEntity<>(reservationRepository.findOccupiedSlotsByDate(date), HttpStatus.OK);
    }
    public ResponseEntity<List<GetReservationsByDateDTO>> getAvailableSlotsByDate(LocalDate date) {
        return new ResponseEntity<>(reservationRepository.findAvailableSlotsByDate(date), HttpStatus.OK);
    }
    public ResponseEntity<String> reserveSessionHour(AddReservationDTO addReservationDTO) {
        if (clientRepository.findById(addReservationDTO.clientId()).isEmpty()) {
            return new ResponseEntity<>("No client with this ID was found!", HttpStatus.BAD_REQUEST);
        }

        if (addReservationDTO.dateTime().toLocalDate().isBefore(LocalDate.now())) {
            return new ResponseEntity<>("Unable to reserve for a past date!", HttpStatus.BAD_REQUEST);
        }

        if (!reservationRepository.findByClientIdAndDate(addReservationDTO.clientId(), addReservationDTO.dateTime().toLocalDate()).isEmpty()) {
            return new ResponseEntity<>(String.format("Attempting the second client's(ID:%s) reservation for the day!", addReservationDTO.clientId()), HttpStatus.BAD_REQUEST);
        }


        if (reservationRepository.isSlotAvailable(addReservationDTO.dateTime())) {
            Optional<SessionHour> sessionHourOpt = sessionHourRepository.findByDateTime(addReservationDTO.dateTime());

            if (sessionHourOpt.isPresent()) {
                reservationRepository.save(new Reservation(
                        clientRepository.findById(addReservationDTO.clientId()).get(),
                        sessionHourOpt.get()
                ));
                return new ResponseEntity<>(String.format("Client with ID: %s has reservation on %s",
                        addReservationDTO.clientId(), addReservationDTO.dateTime()), HttpStatus.OK);
            }
        }

        if (doesTimestampHasMinutes(addReservationDTO.dateTime())) {
            return new ResponseEntity<>("Attempting to reserve partial hour!", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Attempting to reserve at non-working hours!", HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<String> cancelReservation(CancelReservationDTO cancelReservationDTO) {
        Optional<Reservation> reservation = reservationRepository.findById(cancelReservationDTO.reservationId());
        Optional<Client> client = clientRepository.findById(cancelReservationDTO.clientId());

        if (reservation.isEmpty()) {
            return new ResponseEntity<>(String.format("Could not find a reservation with ID: %s to cancel!", cancelReservationDTO.reservationId()), HttpStatus.BAD_REQUEST);
        }
        if (client.isEmpty()) {
            return new ResponseEntity<>(String.format("Could not find a client with ID: %s to cancel!", cancelReservationDTO.clientId()), HttpStatus.BAD_REQUEST);
        }
        if (!reservation.get().getClient().getId().equals(cancelReservationDTO.clientId())) {
            return new ResponseEntity<>(String.format("The reservation with ID: %s does not belong to the client with ID: %s!", cancelReservationDTO.reservationId(), cancelReservationDTO.clientId()), HttpStatus.BAD_REQUEST);
        }

        if (reservation.get().getSessionHour().getDateTime().toLocalDate().isBefore(LocalDate.now())) {
            return new ResponseEntity<>("Unable to cancel a reservation from a past date!", HttpStatus.BAD_REQUEST);
        }

        reservationRepository.delete(reservation.get());
        return new ResponseEntity<>(String.format("Reservation with ID: %s successfully canceled!", cancelReservationDTO.reservationId()), HttpStatus.OK);
    }
    public ResponseEntity<List<GetReservationByParameterDTO>> getReservationsByName(String name) {
        List<GetReservationByParameterDTO> reservations = reservationRepository.findByClientName(name);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    public ResponseEntity<List<GetReservationByParameterDTO>> getReservationsByDate(LocalDate date) {
        List<GetReservationByParameterDTO> reservations = reservationRepository.findByDate(date);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    public ResponseEntity<String> reserveSessionHourInterval(AddReservationForSeveralHoursDTO addReservationForSeveralHoursDTO) {
        if (clientRepository.findById(addReservationForSeveralHoursDTO.clientId()).isEmpty()) {
            return new ResponseEntity<>("No client with this ID was found!", HttpStatus.BAD_REQUEST);
        }

        if (addReservationForSeveralHoursDTO.dateTimeStart().toLocalDate().isBefore(LocalDate.now())) {
            return new ResponseEntity<>("Unable to reserve for a past date!", HttpStatus.BAD_REQUEST);
        }

        if (!reservationRepository.findByClientIdAndDate(addReservationForSeveralHoursDTO.clientId(), addReservationForSeveralHoursDTO.dateTimeStart().toLocalDate()).isEmpty()) {
            return new ResponseEntity<>(String.format("Attempting the second client's(ID:%s) reservation for the day!", addReservationForSeveralHoursDTO.clientId()), HttpStatus.BAD_REQUEST);
        }

        if (addReservationForSeveralHoursDTO.dateTimeStart().isAfter(addReservationForSeveralHoursDTO.dateTimeEnd())) {
            return new ResponseEntity<>("The end time of the reservation is earlier than the start time!", HttpStatus.BAD_REQUEST);
        }

        if (!addReservationForSeveralHoursDTO.dateTimeStart().toLocalDate().equals(addReservationForSeveralHoursDTO.dateTimeEnd().toLocalDate())) {
            return new ResponseEntity<>("The reservation start date and end date do not match!", HttpStatus.BAD_REQUEST);
        }

        List<LocalDateTime> sessionHours = getSlots(addReservationForSeveralHoursDTO.dateTimeStart(), addReservationForSeveralHoursDTO.dateTimeEnd());

        for (LocalDateTime sessionHour : sessionHours) {
            if (!reservationRepository.isSlotAvailable(sessionHour)) {
                if (doesTimestampHasMinutes(addReservationForSeveralHoursDTO.dateTimeStart()) || doesTimestampHasMinutes(addReservationForSeveralHoursDTO.dateTimeEnd())) {
                    return new ResponseEntity<>("Attempting to reserve partial hour!", HttpStatus.BAD_REQUEST);
                } else {
                    return new ResponseEntity<>("Attempting to reserve at non-working hours!", HttpStatus.BAD_REQUEST);
                }
            }
        }

        for (LocalDateTime sessionHour : sessionHours) {
            Optional<SessionHour> sessionHourOpt = sessionHourRepository.findByDateTime(sessionHour);
            reservationRepository.save(new Reservation(
                    clientRepository.findById(addReservationForSeveralHoursDTO.clientId()).get(),
                    sessionHourOpt.get()
            ));
        }
        return new ResponseEntity<>(String.format("The client with ID: %s has reservation on %s - %s",
                addReservationForSeveralHoursDTO.clientId(), addReservationForSeveralHoursDTO.dateTimeStart(), addReservationForSeveralHoursDTO.dateTimeEnd()), HttpStatus.OK);
    }

    private boolean doesTimestampHasMinutes(LocalDateTime localDateTime) {
        return localDateTime.toLocalTime().getMinute() != 0;
    }
    private List<LocalDateTime> getSlots(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd) {
        List<LocalDateTime> slots = new ArrayList<>();
        LocalDateTime current = dateTimeStart;

        while (current.isBefore(dateTimeEnd)) {
            slots.add(current);
            current = current.plusHours(1);
        }

        return slots;
    }
}
