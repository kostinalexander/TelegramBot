package com.example.shelterBot.service;

import com.example.shelterBot.listener.Constants;
import com.example.shelterBot.model.report.ReportCat;
import com.example.shelterBot.repository.ReportCatRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class InlineService {
    final ReportCatRepository repository;

    public InlineService(ReportCatRepository repository) {
        this.repository = repository;
    }

    public SendMessage inlineCheck(ReportCat unch, SendMessage reportMessage) {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        var button1 = new InlineKeyboardButton();
        button1.setText(Constants.ACCEPTED);
        button1.setCallbackData(unch.getId() + ":"+unch.getCat().getUsers() + Constants.ACCEPTED);

        var button2 = new InlineKeyboardButton();
        button2.setText(Constants.NOT_ACCEPTED);
        button2.setCallbackData(unch.getId() + ":" + Constants.NOT_ACCEPTED);

        rowInline.add(button1);
        rowInline.add(button2);

        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);
        reportMessage.setReplyMarkup(markupInline);
        return reportMessage;
    }
}
