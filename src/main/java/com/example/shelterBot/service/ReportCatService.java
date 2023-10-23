package com.example.shelterBot.service;

import com.example.shelterBot.listener.ShelterBot;
import com.example.shelterBot.repository.ReportCatRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ReportCatService {

    private final ShelterBot bot;
    private final String volunteerChatId;
    private final ReportCatRepository reportRepository;


    public ReportCatService(ShelterBot bot, @Value("${shelter.volunteer.id}") String volunteerChatId,
                            ReportCatRepository reportRepository) {
        this.bot = bot;
        this.volunteerChatId = volunteerChatId;
        this.reportRepository = reportRepository;
    }

    public  void markReport(long reportId,boolean accepted) {

        reportRepository.findById(reportId);
    }
}


