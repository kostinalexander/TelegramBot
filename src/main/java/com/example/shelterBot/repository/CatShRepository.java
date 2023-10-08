package com.example.shelterBot.repository;


import com.example.shelterBot.model.shelter.ShelterForCats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatShRepository extends JpaRepository<ShelterForCats, Long> {
}
