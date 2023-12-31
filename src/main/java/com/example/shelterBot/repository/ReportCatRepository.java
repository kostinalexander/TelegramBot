package com.example.shelterBot.repository;

import com.example.shelterBot.model.report.ReportCat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReportCatRepository extends JpaRepository<ReportCat, Long> {

    List<ReportCat> findAllByReportCheckedIsNull();


}
