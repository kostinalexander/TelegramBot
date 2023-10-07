package com.example.shelterBot.repository;

import com.example.shelterBot.model.animals.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  DogRepository extends JpaRepository<Dog,Long> {
}
