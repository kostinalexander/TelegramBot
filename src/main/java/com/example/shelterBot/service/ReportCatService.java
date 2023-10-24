package com.example.shelterBot.service;

import com.example.shelterBot.listener.ShelterBot;
import com.example.shelterBot.model.report.ReportCat;
import com.example.shelterBot.repository.ReportCatRepository;
import com.example.shelterBot.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class ReportCatService {

    private final ShelterBot bot;
    private final String volunteerChatId;
    private  final UsersRepository usersRepository;
    private final ReportCatRepository reportRepository;
    private final InlineService inlineService;


    public ReportCatService(ShelterBot bot, @Value("${shelter.volunteer.id}") String volunteerChatId,
                            UsersRepository usersRepository, ReportCatRepository reportRepository, InlineService inlineService) {
        this.bot = bot;
        this.volunteerChatId = volunteerChatId;
        this.usersRepository = usersRepository;
        this.reportRepository = reportRepository;
        this.inlineService = inlineService;
    }
    public void markReport(long reportId, boolean accepted) {
        reportRepository.findById(reportId).get().setReportChecked(accepted);
    }

@Scheduled()
    public void getUncheckedReport() throws TelegramApiException {
        var unchecked = reportRepository.findAllByReportCheckedIsNull();
        for (ReportCat unch : unchecked) {
            SendMessage reportMessage = new SendMessage(volunteerChatId, unch.getText());
            inlineService.inlineCheck(unch,reportMessage);
        }
    }
}


