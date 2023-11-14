package com.example.shelterBot.repository;

import com.example.shelterBot.model.animals.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CatRepository extends JpaRepository<Cat,Long> {

}
