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
import java.time.LocalTime;
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
        if (clientRepository.findById(addReservationRecord.clientId()).isEmpty()) {
            return new ResponseEntity<>("Клиента с таким ID не найдено!", HttpStatus.BAD_REQUEST);
        }

        if (addReservationRecord.dateTime().toLocalDate().isBefore(LocalDate.now())) {
            return new ResponseEntity<>("Невозможно записаться на прошедшую дату!", HttpStatus.BAD_REQUEST);
        }

        if (!reservationRepository.findByClientIdAndDate(addReservationRecord.clientId(), addReservationRecord.dateTime().toLocalDate()).isEmpty()) {
            return new ResponseEntity<>(String.format("Попытка второй записи клиента с ID: %s за день!", addReservationRecord.clientId()), HttpStatus.BAD_REQUEST);
        }


        if (reservationRepository.isSlotAvailable(addReservationRecord.dateTime())) {
            Optional<SessionHour> sessionHourOpt = sessionHourRepository.findByDateTime(addReservationRecord.dateTime());

            if (sessionHourOpt.isPresent()) {
                reservationRepository.save(new Reservation(
                        clientRepository.findById(addReservationRecord.clientId()).get(),
                        sessionHourOpt.get()
                ));
                return new ResponseEntity<>(String.format("Клиент с ID: %s записан на %s",
                        addReservationRecord.clientId(), addReservationRecord.dateTime()), HttpStatus.OK);
            }
        }

        if (doesTimestampHasMinutes(addReservationRecord.dateTime())) {
            return new ResponseEntity<>("Попытка записаться на неполный час!", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Попытка записаться на нерабочее время!", HttpStatus.BAD_REQUEST);
        }

    }
    public ResponseEntity<String> cancelReservation(CancelReservationRecord cancelReservationRecord) {
        Optional<Reservation> reservation = reservationRepository.findById(cancelReservationRecord.reservationId());
        Optional<Client> client = clientRepository.findById(cancelReservationRecord.clientId());

        if (reservation.isEmpty()) {
            return new ResponseEntity<>(String.format("Не удалось найти запись с ID: %s для отмены!", cancelReservationRecord.reservationId()), HttpStatus.BAD_REQUEST);
        }
        if (client.isEmpty()) {
            return new ResponseEntity<>(String.format("Не удалось найти клиента с ID: %s для отмены!", cancelReservationRecord.clientId()), HttpStatus.BAD_REQUEST);
        }
        if (!reservation.get().getClient().getId().equals(cancelReservationRecord.clientId())) {
            return new ResponseEntity<>(String.format("Запись с ID: %s не принадлежит клиенту с ID: %s. Отмена невозможна!", cancelReservationRecord.reservationId(), cancelReservationRecord.clientId()), HttpStatus.BAD_REQUEST);
        }

        if (reservation.get().getSessionHour().getDateTime().toLocalDate().isBefore(LocalDate.now())) {
            return new ResponseEntity<>("Невозможно отменить запись с прошедшей даты!", HttpStatus.BAD_REQUEST);
        }

        reservationRepository.delete(reservation.get());
        return new ResponseEntity<>(String.format("Запись с ID: %s успешно отменена!", cancelReservationRecord.reservationId()), HttpStatus.OK);
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

    public ResponseEntity<String> reserveSessionHourInterval(AddReservationForSeveralHoursRecord addReservationForSeveralHoursRecord) {
        if (clientRepository.findById(addReservationForSeveralHoursRecord.clientId()).isEmpty()) {
            return new ResponseEntity<>("Клиента с таким ID не найдено!", HttpStatus.BAD_REQUEST);
        }

        if (addReservationForSeveralHoursRecord.dateTimeStart().toLocalDate().isBefore(LocalDate.now())) {
            return new ResponseEntity<>("Невозможно записаться на прошедшую дату!", HttpStatus.BAD_REQUEST);
        }

        if (!reservationRepository.findByClientIdAndDate(addReservationForSeveralHoursRecord.clientId(), addReservationForSeveralHoursRecord.dateTimeStart().toLocalDate()).isEmpty()) {
            return new ResponseEntity<>(String.format("Попытка второй записи клиента с ID: %s за день!", addReservationForSeveralHoursRecord.clientId()), HttpStatus.BAD_REQUEST);
        }

        if (addReservationForSeveralHoursRecord.dateTimeStart().isAfter(addReservationForSeveralHoursRecord.dateTimeEnd())) {
            return new ResponseEntity<>("Время окончания записи раньше чем время её начала!", HttpStatus.BAD_REQUEST);
        }

        if (!addReservationForSeveralHoursRecord.dateTimeStart().toLocalDate().equals(addReservationForSeveralHoursRecord.dateTimeEnd().toLocalDate())) {
            return new ResponseEntity<>("Дата начала записи и дата ёё окончания не совпадают!", HttpStatus.BAD_REQUEST);
        }

        List<LocalDateTime> sessionHours = getSlots(addReservationForSeveralHoursRecord.dateTimeStart(), addReservationForSeveralHoursRecord.dateTimeEnd());

        for (LocalDateTime sessionHour : sessionHours) {
            if (!reservationRepository.isSlotAvailable(sessionHour)) {
                if (doesTimestampHasMinutes(addReservationForSeveralHoursRecord.dateTimeStart())) {
                    return new ResponseEntity<>("Попытка записаться на неполный час!", HttpStatus.BAD_REQUEST);
                } else {
                    return new ResponseEntity<>("Попытка записаться на нерабочее время!", HttpStatus.BAD_REQUEST);
                }
            }
        }

        for (LocalDateTime sessionHour : sessionHours) {
            Optional<SessionHour> sessionHourOpt = sessionHourRepository.findByDateTime(sessionHour);
            reservationRepository.save(new Reservation(
                    clientRepository.findById(addReservationForSeveralHoursRecord.clientId()).get(),
                    sessionHourOpt.get()
            ));
        }
        return new ResponseEntity<>(String.format("Клиент с ID: %s записан на %s - %s",
                addReservationForSeveralHoursRecord.clientId(), addReservationForSeveralHoursRecord.dateTimeStart(), addReservationForSeveralHoursRecord.dateTimeEnd()), HttpStatus.OK);
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
