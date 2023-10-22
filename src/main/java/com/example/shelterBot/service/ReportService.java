package com.example.shelterBot.service;

import com.example.shelterBot.config.BotConfig;
import com.example.shelterBot.listener.Constants;
import com.example.shelterBot.listener.ShelterBot;
import com.example.shelterBot.model.Report;
import com.example.shelterBot.model.Users;
import com.example.shelterBot.repository.ReportRepository;
import com.example.shelterBot.repository.UsersRepository;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {
   // private final ShelterBot bot;
    private final String volunteerChatId;
    private final UsersRepository usersRepository;
    private final ReportRepository reportRepository;

    public ReportService(@Value("${shelter.volunteer.id}")String volunteerChatId,
                         UsersRepository usersRepository, ReportRepository reportRepository) {
        this.volunteerChatId = String.valueOf(volunteerChatId);
      //  this.bot = bot;
        this.usersRepository = usersRepository;
        this.reportRepository = reportRepository;
    }

    public void getUncheckedReport() throws TelegramApiException {
        var unchecked = reportRepository.findAllByReportCheckedIsNull();
        for(Report unch: unchecked){
            SendMessage reportMessage = new SendMessage(volunteerChatId,unch.getText());
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            var button1 = new InlineKeyboardButton();
            button1.setText(Constants.ACCEPTED);
            button1.setCallbackData(unch.getId()+ ":"+Constants.ACCEPTED);

            var button2 = new InlineKeyboardButton();
            button2.setText(Constants.NOT_ACCEPTED);
            button2.setCallbackData(unch.getId()+":"+Constants.NOT_ACCEPTED);

            rowInline.add(button1);
            rowInline.add(button2);

            rowsInline.add(rowInline);

            markupInline.setKeyboard(rowsInline);
            reportMessage.setReplyMarkup(markupInline);

        //    bot.execute(reportMessage);
        }
    }
    public void martReport(long reportId, boolean accepted){
        if (accepted==true){
            reportRepository.findById(reportId).get().setReportChecked(true);

        }else if(accepted==false){
            reportRepository.findById(reportId).get().setReportChecked(false);
        }

    }


}
