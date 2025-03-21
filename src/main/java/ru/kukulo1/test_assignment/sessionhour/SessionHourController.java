package ru.kukulo1.test_assignment.sessionhour;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v0/pool/day")
public class SessionHourController {

    @Autowired
    private SessionHourService sessionHourService;

    @PostMapping("/add/working")
    public ResponseEntity<String> addRegularWorkingDay(@RequestParam LocalDate date) {
        return sessionHourService.addRegularWorkingDay(date);
    }

    @PostMapping("/add/holiday/standard")
    public ResponseEntity<String> addStandardHoliday(@RequestParam LocalDate date) {
        return sessionHourService.addStandardHoliday(date);
    }

    @PostMapping("/add/holiday/custom")
    public ResponseEntity<String> addCustomHoliday(@RequestParam LocalDate date, @RequestParam Integer startTime, @RequestParam Integer endTime) {
        return sessionHourService.addCustomHoliday(date, startTime, endTime);
    }
}
