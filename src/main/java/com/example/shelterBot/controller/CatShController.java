package com.example.shelterBot.controller;

import com.example.shelterBot.model.shelter.ShelterForCats;
import com.example.shelterBot.service.CatShService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/cats_shelter")
public class CatShController {


    private final CatShService service;

    public CatShController(CatShService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ShelterForCats> addCatShelter(@RequestBody ShelterForCats catSh) {
        ShelterForCats add = service.addCatSh(catSh);
        return ResponseEntity.ok(add);

    }

    @PutMapping
    public ResponseEntity<ShelterForCats> editCatShelter(@RequestBody ShelterForCats catSh) {
        ShelterForCats editCatSh = service.editCatShelter(catSh);
        return ResponseEntity.ok(editCatSh);


    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteCatShelter(@PathVariable long id) {
        service.deleteCatShelter(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<ShelterForCats> findCatShelter(@PathVariable long id) {
        ShelterForCats catsSh = service.findShelterForCats(id);
        if (catsSh == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(catsSh);
    }

    @GetMapping
    public ResponseEntity<Collection<ShelterForCats>> allCats() {
        return ResponseEntity.ok(service.getAll());
    }


}
