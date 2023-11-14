package com.example.shelterBot.service;

import com.example.shelterBot.model.animals.Dog;
import com.example.shelterBot.model.report.ReportCat;
import com.example.shelterBot.model.report.ReportDog;
import com.example.shelterBot.repository.ReportDogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportDogService {


    private final String volunteerChatId;
    private final ReportDogRepository reportRepository;
    private final InlineService inlineService;

    public ReportDogService(@Value("${shelter.volunteer.id}")String volunteerChatId, ReportDogRepository reportRepository, InlineService inlineService) {
        this.volunteerChatId = volunteerChatId;
        this.reportRepository = reportRepository;
        this.inlineService = inlineService;
    }
    public ReportDog saveReport (Dog dog, String message,byte[] photo){
        ReportDog reportDog=new ReportDog();
        reportDog.setText(message);
        reportDog.setLocalDate(LocalDate.now());
        reportDog.setReportChecked(null);
        reportDog.setPhoto(photo);
        reportDog.setDog(dog);
        return reportRepository.save(reportDog);
    }
    public void markReport(long reportId, boolean accepted) {
        var report = reportRepository.findById(reportId).orElseThrow(() -> new IllegalStateException("Репорт не найдет!"));
        report.setReportChecked(accepted);
        reportRepository.save(report);
    }


    public List<ReportDog> getAllUncheckedReportsDog() {
        return reportRepository.findAllByReportCheckedIsNull();
    }
}
