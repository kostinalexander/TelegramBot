package com.example.shelterBot.service;

import com.example.shelterBot.model.report.ReportCat;
import com.example.shelterBot.model.report.ReportDog;
import com.example.shelterBot.repository.ReportDogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
    public ReportDog saveReport (String message){
        ReportDog reportDog=new ReportDog();
        reportDog.setText(reportDog.getText());
        reportDog.setPhoto(reportDog.getPhoto());
        return reportRepository.save(reportDog);
    }
    public void markReport(long reportId, boolean accepted) {
        ReportDog reportDog=new ReportDog();
        if (accepted == true) {
            reportRepository.findById(reportId).get().setReportChecked(true);
            reportRepository.save(reportDog);
        } else {
            reportRepository.findById(reportId).get().setReportChecked(false);
        }


    }

    @Scheduled()
    public void getUncheckedReport() throws TelegramApiException {
        var unchecked = reportRepository.findAllByReportCheckedIsNull();
        for (ReportDog unch : unchecked) {
            SendMessage reportMessage = new SendMessage(volunteerChatId, unch.getText());
            inlineService.inlineDogCheck(unch, reportMessage);
        }
    }
}
