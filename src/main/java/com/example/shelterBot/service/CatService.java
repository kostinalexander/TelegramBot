package com.example.shelterBot.service;

import com.example.shelterBot.model.animals.Cat;
import com.example.shelterBot.repository.CatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CatService {
    private final CatRepository repository;

    @Autowired
    public CatService(CatRepository repository) {
        this.repository = repository;
    }
    public Cat addCat(Cat cat) {
        return repository.save(cat);
    }
    public Cat findCat(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Cat editCat(Cat cat) {
        return repository.findById(cat.getId())
                .map(i -> {
                    i.setName(cat.getName());
                    i.setAge(cat.getAge());
                    return repository.save(i);
                }).orElse(null);
    }
    public void deleteCat(Long id) {
         repository.deleteById(id);
    }

    public Collection<Cat> getAll(){
        return repository.findAll();
    }

}
