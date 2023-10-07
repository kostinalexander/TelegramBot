package com.example.shelterBot.controller;

import com.example.shelterBot.model.animals.Cat;
import com.example.shelterBot.service.CatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/cats")
public class CatController {


    private final CatService catService;

    public CatController(CatService catService) {
        this.catService = catService;
    }

    @PostMapping
    public ResponseEntity<Cat> add(@RequestBody Cat cat) {
        Cat add = catService.addCat(cat);
        return ResponseEntity.ok(add);
    }

    @PutMapping
    public ResponseEntity<Cat> editCat(@RequestBody Cat cat) {
        Cat editCat = catService.editCat(cat);
        return ResponseEntity.ok(editCat);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteCat(@PathVariable long id) {
        catService.deleteCat(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Cat> findCat(@PathVariable long id) {
        Cat cat = catService.findCat(id);
        if (cat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cat);
    }

    @GetMapping
    public ResponseEntity<Collection<Cat>> allCats() {
        return ResponseEntity.ok(catService.getAll());
    }


}
