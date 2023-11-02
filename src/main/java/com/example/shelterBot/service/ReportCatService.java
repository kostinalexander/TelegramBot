package com.example.shelterBot.service;

import com.example.shelterBot.model.animals.Cat;
import com.example.shelterBot.model.report.ReportCat;
import com.example.shelterBot.repository.ReportCatRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportCatService {

    private final String volunteerChatId;
    private final ReportCatRepository reportRepository;
    private final InlineService inlineService;


    public ReportCatService(@Value("${shelter.volunteer.id}") String volunteerChatId,
                            ReportCatRepository reportRepository, InlineService inlineService) {
        this.volunteerChatId = volunteerChatId;
        this.reportRepository = reportRepository;
        this.inlineService = inlineService;
    }

    public ReportCat saveReport (Cat cat, String message, byte[] photo){

        ReportCat reportCat=new ReportCat();
        reportCat.setText(message);
        reportCat.setLocalDate(LocalDate.now());
        reportCat.setReportChecked(null);
        reportCat.setPhoto(photo);
        reportCat.setCat(cat);
        return reportRepository.save(reportCat);
    }



    public void markReport(long reportId, boolean accepted) {
        var report = reportRepository.findById(reportId).orElseThrow(() -> new IllegalStateException("Репорт не найдет!"));
        report.setReportChecked(accepted);
        reportRepository.save(report);
    }
    public List<ReportCat> getAllUncheckedReports() {
        return reportRepository.findAllByReportCheckedIsNull();
    }


}


