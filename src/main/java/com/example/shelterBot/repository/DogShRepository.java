package com.example.shelterBot.repository;

import com.example.shelterBot.model.shelter.DogShelter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogShRepository extends JpaRepository<DogShelter,Long> {
}
