package ru.kukulo1.test_assignment.sessionhour;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class SessionHourService {

    @Autowired
    private SessionHourRepository sessionHourRepository;

    public ResponseEntity<String> addRegularWorkingDay(LocalDate date) {
        final int START_TIME_OF_DAY = 9;
        final int END_TIME_OF_DAY = 21;

        if (sessionHourRepository.existsByDate(date)) {
            return new ResponseEntity<>(String.format("As of date %s, the reservation slots have already been slotted!", date), HttpStatus.BAD_REQUEST);
        }

        for (int hour = START_TIME_OF_DAY; hour < END_TIME_OF_DAY; hour++) {
            LocalTime time = LocalTime.of(hour, 0, 0, 0);
            LocalDateTime localDateTime = LocalDateTime.of(date, time);
            SessionHour sessionHour = new SessionHour(localDateTime);
            sessionHourRepository.save(sessionHour);
        }
        return new ResponseEntity<>(String.format("The date %s is designated as a business day. Working hours: %s:00 - %s:00", date, START_TIME_OF_DAY, END_TIME_OF_DAY), HttpStatus.OK);
    }

    public ResponseEntity<String> addStandardHoliday(LocalDate date) {
        int START_TIME_OF_DAY = 9;
        int END_TIME_OF_DAY = 18;

        if (sessionHourRepository.existsByDate(date)) {
            return new ResponseEntity<>(String.format("As of date %s, the reservation slots have already been slotted!", date), HttpStatus.BAD_REQUEST);
        }
        for (int hour = START_TIME_OF_DAY; hour < END_TIME_OF_DAY; hour++) {
            LocalTime time = LocalTime.of(hour, 0, 0, 0);
            LocalDateTime localDateTime = LocalDateTime.of(date, time);
            SessionHour sessionHour = new SessionHour(localDateTime);
            sessionHourRepository.save(sessionHour);
        }
        return new ResponseEntity<>(String.format("The date %s is designated as a holiday. Working hours: %s:00 - %s:00", date, START_TIME_OF_DAY, END_TIME_OF_DAY), HttpStatus.OK);
    }

    public ResponseEntity<String> addCustomHoliday(LocalDate date, int START_TIME_OF_DAY, int END_TIME_OF_DAY) {
        if (sessionHourRepository.existsByDate(date)) {
            return new ResponseEntity<>(String.format("As of date %s, the record slots have already been slotted!", date), HttpStatus.BAD_REQUEST);
        }
        for (int hour = START_TIME_OF_DAY; hour < END_TIME_OF_DAY; hour++) {
            LocalTime time = LocalTime.of(hour, 0, 0, 0);
            LocalDateTime localDateTime = LocalDateTime.of(date, time);
            SessionHour sessionHour = new SessionHour(localDateTime);
            sessionHourRepository.save(sessionHour);
        }
        return new ResponseEntity<>(String.format("The date %s is designated as a holiday. Working hours: %s:00 - %s:00", date, START_TIME_OF_DAY, END_TIME_OF_DAY), HttpStatus.OK);
    }
}
