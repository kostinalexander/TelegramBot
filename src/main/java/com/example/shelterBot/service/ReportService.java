package com.example.shelterBot.service;

import com.example.shelterBot.config.BotConfig;
import com.example.shelterBot.model.Users;
import com.example.shelterBot.repository.ReportRepository;
import com.example.shelterBot.repository.UsersRepository;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Service
public class ReportService {

    private final BotConfig bot;
    private final UsersRepository usersRepository;
    private final ReportRepository reportRepository;

    public ReportService(BotConfig bot, UsersRepository usersRepository, ReportRepository reportRepository) {
        this.bot = bot;
        this.usersRepository = usersRepository;
        this.reportRepository = reportRepository;
    }

    public SendMessage postReport(final Long chatId, final String textToSend, Update update) {

        List<Users> users = (List<Users>) usersRepository.findUsersByTelegramUserId(chatId);

        if (users.)
    }


}
