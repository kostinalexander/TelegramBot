package com.example.shelterBot.repository;

import com.example.shelterBot.model.animals.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatRepository extends JpaRepository<Cat,Long> {
}
