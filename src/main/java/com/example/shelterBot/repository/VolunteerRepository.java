package com.example.shelterBot.repository;

import com.example.shelterBot.model.people.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRepository  extends JpaRepository<Volunteer,Long> {
}

