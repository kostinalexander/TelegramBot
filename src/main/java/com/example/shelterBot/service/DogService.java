package com.example.shelterBot.service;

import com.example.shelterBot.model.animals.Dog;
import com.example.shelterBot.repository.DogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DogService {

    private final DogRepository repository;

    @Autowired
    public DogService(DogRepository repository) {
        this.repository = repository;
    }
    public Dog addDog(Dog dog){
        return repository.save(dog);
    }

    public Dog findDog(Long id){
        return repository.findById(id).orElse(null);
    }

    public Dog editDog(Dog dog) {
        return repository.findById(dog.getId())
                .map(i -> {
                    i.setName(i.getName());
                    i.setAge(i.getAge());
                    return repository.save(i);
                }).orElse(null);
    }

    public void deleteDog(Long id) {
        repository.deleteById(id);
    }

    public Collection<Dog> getAll(){
        return repository.findAll();
    }

}