package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    @Autowired
    private TimeEntryRepository repo;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.repo = timeEntryRepository;
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(TimeEntry timeEntryToCreate) {
        return new ResponseEntity<>(repo.create(timeEntryToCreate), HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long timeEntryId) {
        TimeEntry thingWeGot = repo.find(timeEntryId);
        if (thingWeGot == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
        return new ResponseEntity<>(thingWeGot, HttpStatus.OK);
    }}

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        return new ResponseEntity<>(repo.list(), HttpStatus.OK);
    }

    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId,@RequestBody TimeEntry expected) {
        TimeEntry timeEntry = repo.update(timeEntryId, expected);
        if (timeEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
        return new ResponseEntity<>(timeEntry, HttpStatus.OK);
    }}

    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity delete(@PathVariable long timeEntryId) {
        repo.delete(timeEntryId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
