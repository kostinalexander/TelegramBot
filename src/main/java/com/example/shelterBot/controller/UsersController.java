package com.example.shelterBot.controller;

import com.example.shelterBot.model.people.Users;
import com.example.shelterBot.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService service;

    public UsersController(UsersService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Users> add(@RequestBody Users users) {
        Users add = service.addUser(users);
        return ResponseEntity.ok(add);
    }

    @PutMapping
    public ResponseEntity<Users> editUser(@RequestBody Users users) {
        Users edit = service.editUser(users);
        return ResponseEntity.ok(edit);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteUser(@PathVariable long id) {
        service.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Users> findUser(@PathVariable long id) {
        Users users = service.findUser(id);
        if (users == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping
    public ResponseEntity<Collection<Users>> all() {
        return ResponseEntity.ok(service.allUsers());
    }
}