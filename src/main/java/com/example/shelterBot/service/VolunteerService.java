package com.example.shelterBot.service;

import com.example.shelterBot.model.people.Volunteer;
import com.example.shelterBot.repository.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class VolunteerService {

    private final VolunteerRepository repository;

    @Autowired
    public VolunteerService(VolunteerRepository repository) {
        this.repository = repository;
    }

    public Volunteer add(Volunteer volunteer) {
        return repository.save(volunteer);
    }

    public Volunteer edit(Volunteer volunteer) {
        return repository.findById(volunteer.getId())
                .map(i -> {
                    i.setName(volunteer.getName());
                    i.setLastName(volunteer.getLastName());
                    i.setEmail(volunteer.getEmail());
                    return repository.save(i);
                }).orElse(null);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public Volunteer find(long id) {
        return repository.findById(id).orElse(null);
    }

    public Collection<Volunteer> allVolunteer() {
        return repository.findAll();
    }
}