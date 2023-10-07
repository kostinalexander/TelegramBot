package com.example.shelterBot.controller;

import com.example.shelterBot.model.animals.Dog;
import com.example.shelterBot.model.shelter.DogShelter;
import com.example.shelterBot.service.DogService;
import com.example.shelterBot.service.DogShService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/dog_shelter")
public class DogShController {

    private final DogShService service;

    public DogShController(DogShService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DogShelter> addDogSh(@RequestBody DogShelter dogS) {
        DogShelter add = service.addDogSh(dogS);
        return ResponseEntity.ok(add);
    }

    @PutMapping
    public ResponseEntity<DogShelter> editDogShelter(@RequestBody DogShelter dogS) {
        DogShelter editSh = service.editDogShelter(dogS);
        return ResponseEntity.ok(editSh);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteDogShelter(@PathVariable long id) {
        service.deleteDogShelter(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<DogShelter> findDogShelter(@PathVariable long id) {
        DogShelter dogs = service.findShelterForDogs(id);
        if (dogs == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dogs);
    }

    @GetMapping
    public ResponseEntity<Collection<DogShelter>> allCats() {
        return ResponseEntity.ok(service.getAll());
    }

}

