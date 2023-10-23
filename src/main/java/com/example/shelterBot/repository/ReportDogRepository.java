package com.example.shelterBot.repository;

import com.example.shelterBot.model.report.ReportDog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReportDogRepository extends JpaRepository<ReportDog, Long> {

    Optional<ReportDog> findByLocalDateEquals(LocalDate date);

    List<ReportDog> findByUserId(Long id);
    List<ReportDog> findAllByReportCheckedIsNull();
}
