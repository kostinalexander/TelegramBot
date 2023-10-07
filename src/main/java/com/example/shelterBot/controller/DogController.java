package com.example.shelterBot.controller;

import com.example.shelterBot.model.animals.Dog;
import com.example.shelterBot.service.DogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/dogs")
public class DogController {

    private final DogService service;

    public DogController(DogService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Dog> addDog(@RequestBody Dog dog) {
        Dog add = service.addDog(dog);
        return ResponseEntity.ok(add);
    }

    @PutMapping
    public ResponseEntity<Dog> editDog(@RequestBody Dog dog) {
        Dog editDog = service.editDog(dog);
        return ResponseEntity.ok(editDog);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteDog(@PathVariable long id) {
        service.deleteDog(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Dog> findDog(@PathVariable long id) {
        Dog dog = service.findDog(id);
        if (dog == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dog);
    }

    @GetMapping
    public ResponseEntity<Collection<Dog>> allDogs() {
        return ResponseEntity.ok(service.getAll());
    }

}