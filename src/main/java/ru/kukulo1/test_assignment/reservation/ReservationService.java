package ru.kukulo1.test_assignment.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kukulo1.test_assignment.client.Client;
import ru.kukulo1.test_assignment.client.ClientRepository;
import ru.kukulo1.test_assignment.reservation.records.*;
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

    public ResponseEntity<List<GetReservationsByDateInterface>> getAllReservationsByDate(LocalDate date) {
        return new ResponseEntity<>(reservationRepository.findOccupiedSlotsByDate(date), HttpStatus.OK);
    }
    public ResponseEntity<List<GetReservationsByDateInterface>> getAvailableSlotsByDate(LocalDate date) {
        return new ResponseEntity<>(reservationRepository.findAvailableSlotsByDate(date), HttpStatus.OK);
    }
    public ResponseEntity<String> reserveSessionHour(AddReservationRecord addReservationRecord) {
        if (clientRepository.findById(addReservationRecord.clientID()).isEmpty()) {
            return new ResponseEntity<>("Клиента с таким ID не найдено!", HttpStatus.BAD_REQUEST);
        }

        if (!reservationRepository.findByClientIdAndDate(addReservationRecord.clientID(), addReservationRecord.localDateTime().toLocalDate()).isEmpty()) {
            return new ResponseEntity<>(String.format("Попытка второй записи клиента с ID: %s за день!", addReservationRecord.clientID()), HttpStatus.BAD_REQUEST);
        }


        if (reservationRepository.isSlotAvailable(addReservationRecord.localDateTime())) {
            Optional<SessionHour> sessionHourOpt = sessionHourRepository.findByDateTime(addReservationRecord.localDateTime());

            if (sessionHourOpt.isPresent()) {
                reservationRepository.save(new Reservation(
                        clientRepository.findById(addReservationRecord.clientID()).get(),
                        sessionHourOpt.get()
                ));
                return new ResponseEntity<>(String.format("Клиент с ID: %s записан на %s",
                        addReservationRecord.clientID(), addReservationRecord.localDateTime()), HttpStatus.OK);
            }
        }

        if (doesTimestampHasMinutes(addReservationRecord.localDateTime())) {
            return new ResponseEntity<>("Попытка записаться на неполный час!", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Попытка записаться на нерабочее время!", HttpStatus.BAD_REQUEST);
        }

    }
    public ResponseEntity<String> cancelReservation(CancelReservationRecord cancelReservationRecord) {
        Optional<Reservation> reservation = reservationRepository.findById(cancelReservationRecord.reservationID());
        Optional<Client> client = clientRepository.findById(cancelReservationRecord.clientID());

        if (reservation.isEmpty()) {
            return new ResponseEntity<>(String.format("Не удалось найти запись с ID: %s для отмены!", cancelReservationRecord.reservationID()), HttpStatus.BAD_REQUEST);
        }
        if (client.isEmpty()) {
            return new ResponseEntity<>(String.format("Не удалось найти клиента с ID: %s для отмены!", cancelReservationRecord.clientID()), HttpStatus.BAD_REQUEST);
        }
        if (!reservation.get().getClient().getId().equals(cancelReservationRecord.clientID())) {
            return new ResponseEntity<>(String.format("Запись с ID: %s не принадлежит клиенту с ID: %s. Отмена невозможна!", cancelReservationRecord.reservationID(), cancelReservationRecord.clientID()), HttpStatus.BAD_REQUEST);
        }

        reservationRepository.delete(reservation.get());
        return new ResponseEntity<>(String.format("Запись с ID: %s успешно отменена!", cancelReservationRecord.reservationID()), HttpStatus.OK);
    }
    public ResponseEntity<List<GetReservationByParameterRecord>> getReservationsByName(String name) {
        List<Reservation> reservations = reservationRepository.findByClientName(name);

        if (reservations.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        List<GetReservationByParameterRecord> reservationsRecords = new ArrayList<>();

        for (Reservation r : reservations) {
            reservationsRecords.add(new GetReservationByParameterRecord(r));
        }
        return new ResponseEntity<>(reservationsRecords, HttpStatus.OK);
    }

    public ResponseEntity<List<GetReservationByParameterRecord>> getReservationsByDate(LocalDate date) {
        List<Reservation> reservations = reservationRepository.findByDate(date);

        if (reservations.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        List<GetReservationByParameterRecord> reservationsRecords = new ArrayList<>();

        for (Reservation r : reservations) {
            reservationsRecords.add(new GetReservationByParameterRecord(r));
        }
        return new ResponseEntity<>(reservationsRecords, HttpStatus.OK);
    }

    private boolean doesTimestampHasMinutes(LocalDateTime localDateTime) {
        return localDateTime.toLocalTime().getMinute() != 0;
    }
}
