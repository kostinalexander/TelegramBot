package com.example.shelterBot.controller;

import com.example.shelterBot.model.Volunteer;
import com.example.shelterBot.service.VolunteerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController {

    private final VolunteerService service;

    public VolunteerController(VolunteerService service) {
        this.service = service;
    }
    @PostMapping
    public ResponseEntity<Volunteer> add(@RequestBody Volunteer volunteer) {
        Volunteer add = service.add(volunteer);
        return ResponseEntity.ok(add);
    }

    @PutMapping
    public ResponseEntity<Volunteer> editVolunteer(@RequestBody Volunteer volunteer) {
        Volunteer edit = service.edit(volunteer);
        return ResponseEntity.ok(edit);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteVolunteer(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Volunteer> findVolunteer(@PathVariable long id) {
        Volunteer volunteer = service.find(id);
        if (volunteer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(volunteer);
    }
    @GetMapping
    public Collection<Volunteer> allVolunteers(){
        return service.allVolunteer();
    }
}
