package com.example.shelterBot.service;

import com.example.shelterBot.listener.ShelterBot;
import com.example.shelterBot.model.shelter.DogShelter;
import com.example.shelterBot.repository.DogShRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DogShService {

    private  final DogShRepository repository;

    public DogShService(DogShRepository repository) {
        this.repository = repository;
    }

    public DogShelter addDogSh(DogShelter shelterDogs){
        return repository.save(shelterDogs);
    }

    public DogShelter findShelterForDogs(Long id){
        return repository.getById(id);
    }

    public DogShelter editDogShelter(DogShelter dogShelter) {
        return repository.findById(dogShelter.getId())
                .map(i -> {
                    i.setDogs(i.getDogs());
                    i.setAddress(i.getAddress());
                    i.setNameShelter(i.getNameShelter());
                    i.setWorkingHours(i.getWorkingHours());
                    return repository.save(i);
                }).orElse(null);
    }

    public void deleteDogShelter(Long id) {
        repository.deleteById(id);
    }

    public Collection<DogShelter> getAll(){
        return repository.findAll();
    }
}