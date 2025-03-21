package ru.kukulo1.test_assignment.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kukulo1.test_assignment.client.Client;
import ru.kukulo1.test_assignment.client.ClientRepository;
import ru.kukulo1.test_assignment.reservation.records.*;
import ru.kukulo1.test_assignment.sessionhour.SessionHourRepository;

import java.sql.Date;
import java.sql.Timestamp;
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

    public ResponseEntity<List<GetReservationsByDateRecord>> getAllReservationsByDate(Date date) {
        return new ResponseEntity<>(reservationRepository.findOccupiedSlotsByDate(date.toLocalDate()), HttpStatus.OK);
    }
    public ResponseEntity<List<GetReservationsByDateInterface>> getAvailableSlotsByDate(Date date) {
        return new ResponseEntity<>(reservationRepository.findAvailableSlotsByDate(date.toLocalDate()), HttpStatus.OK);
    }
    public ResponseEntity<String> reserveSessionHour(AddReservationRecord addReservationRecord) {
        if (clientRepository.findById(addReservationRecord.clientID()).isEmpty()) {
            return new ResponseEntity<>("Клиента с таким ID не найдено!", HttpStatus.BAD_REQUEST);
        }

        if (reservationRepository.isSlotAvailable(addReservationRecord.timestamp())) {
            reservationRepository.save(new Reservation(
                    clientRepository.findById(addReservationRecord.clientID()).get(),
                    sessionHourRepository.findByDateTime(addReservationRecord.timestamp()).get()));
            return new ResponseEntity<>(String.format("Клиент с ID: %s записан на %s", addReservationRecord.clientID(), addReservationRecord.timestamp().toString()), HttpStatus.OK);
        } else {
            if (doesTimestampHasMinutes(addReservationRecord.timestamp())) {
                return new ResponseEntity<>("Попытка записаться на неполный час!", HttpStatus.BAD_REQUEST);
            }
            else {
                return new ResponseEntity<>("Попытка записаться на нерабочее время!", HttpStatus.BAD_REQUEST);
            }
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

    public ResponseEntity<List<GetReservationByParameterRecord>> getReservationsByDate(Date date) {
        List<Reservation> reservations = reservationRepository.findByDate(date.toLocalDate());

        if (reservations.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        List<GetReservationByParameterRecord> reservationsRecords = new ArrayList<>();

        for (Reservation r : reservations) {
            reservationsRecords.add(new GetReservationByParameterRecord(r));
        }
        return new ResponseEntity<>(reservationsRecords, HttpStatus.OK);
    }

    private boolean doesTimestampHasMinutes(Timestamp timestamp) {
        return timestamp.toLocalDateTime().toLocalTime().getMinute() != 0;
    }
}
