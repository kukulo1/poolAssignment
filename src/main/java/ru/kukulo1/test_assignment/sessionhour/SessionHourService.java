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
        //time in hours (24-hour time format)
        //regular working day - 9:00 - 21:00
        int startTimeOfDay = 9;
        int endTimeOfDay = 21;

        if (sessionHourRepository.existsByDate(date)) {
            return new ResponseEntity<>(String.format("На дату %s уже проставлены слоты записей!", date), HttpStatus.BAD_REQUEST);
        }

        for (int hour = startTimeOfDay; hour < endTimeOfDay; hour++) {
            LocalTime time = LocalTime.of(hour, 0, 0, 0);
            LocalDateTime localDateTime = LocalDateTime.of(date, time);
            SessionHour sessionHour = new SessionHour(localDateTime);
            sessionHourRepository.save(sessionHour);
        }
        return new ResponseEntity<>(String.format("Дата %s назначена рабочим днем. Время работы: %s:00 - %s:00", date, startTimeOfDay, endTimeOfDay), HttpStatus.OK);
    }
    public ResponseEntity<String> addStandardHoliday(LocalDate date) {
        //time in hours (24-hour time format)
        //regular working day - 9:00 - 18:00
        int startTimeOfDay = 9;
        int endTimeOfDay = 18;

        if (sessionHourRepository.existsByDate(date)) {
            return new ResponseEntity<>(String.format("На дату %s уже проставлены слоты записей!", date), HttpStatus.BAD_REQUEST);
        }
        for (int hour = startTimeOfDay; hour < endTimeOfDay; hour++) {
            LocalTime time = LocalTime.of(hour, 0, 0, 0);
            LocalDateTime localDateTime = LocalDateTime.of(date, time);
            SessionHour sessionHour = new SessionHour(localDateTime);
            sessionHourRepository.save(sessionHour);
        }
        return new ResponseEntity<>(String.format("Дата %s назначена праздничным днем. Время работы: %s:00 - %s:00", date, startTimeOfDay, endTimeOfDay), HttpStatus.OK);
    }
    public ResponseEntity<String> addCustomHoliday(LocalDate date, int startTimeOfDay, int endTimeOfDay) {
        if (sessionHourRepository.existsByDate(date)) {
            return new ResponseEntity<>(String.format("На дату %s уже проставлены слоты записей!", date), HttpStatus.BAD_REQUEST);
        }
        for (int hour = startTimeOfDay; hour < endTimeOfDay; hour++) {
            LocalTime time = LocalTime.of(hour, 0, 0, 0);
            LocalDateTime localDateTime = LocalDateTime.of(date,time);
            SessionHour sessionHour = new SessionHour(localDateTime);
            sessionHourRepository.save(sessionHour);
        }
        return new ResponseEntity<>(String.format("Дата %s назначена праздничным днем. Время работы: %s:00 - %s:00", date, startTimeOfDay, endTimeOfDay), HttpStatus.OK);
    }
}
