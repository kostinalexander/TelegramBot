package com.example.shelterBot.service;

import com.example.shelterBot.model.report.ReportCat;
import com.example.shelterBot.repository.ReportCatRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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

    public ReportCat saveReport (String message){
        ReportCat reportCat=new ReportCat();
        reportCat.setText(reportCat.getText());
        reportCat.setPhoto(reportCat.getPhoto());
        return reportRepository.save(reportCat);
    }



    public void markReport(long reportId, boolean accepted) {
        ReportCat reportCat=new ReportCat();
        if (accepted == true) {
            reportRepository.findById(reportId).get().setReportChecked(true);
            reportRepository.save(reportCat);
        } else {
            reportRepository.findById(reportId).get().setReportChecked(false);
        }


    }

    @Scheduled()
    public void getUncheckedReport() throws TelegramApiException {
        var unchecked = reportRepository.findAllByReportCheckedIsNull();
        for (ReportCat unch : unchecked) {
            SendMessage reportMessage = new SendMessage(volunteerChatId, unch.getText());
            inlineService.inlineCheck(unch, reportMessage);
        }
    }
}


