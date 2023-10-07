package com.example.shelterBot.repository;

import com.example.shelterBot.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRepository  extends JpaRepository<Volunteer,Long> {
}

