package com.example.shelterBot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * отображает меню приюта для кошек
 *
 * @author Andrey 
 */
@Service
public class MenuServiceCat {
    public SendMessage getMenuMessage(final long chatId,final String textToSend) {
        final ReplyKeyboardMarkup replyKeyboardMarkup =getMenuKeyboard();
        return createMessageWithKeyboard(chatId, textToSend, replyKeyboardMarkup);
    }
    private ReplyKeyboardMarkup getMenuKeyboard() {
        final ReplyKeyboardMarkup replyKeyboardMarkup =new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1=new KeyboardRow();
        KeyboardRow row2=new KeyboardRow();
        KeyboardRow row3=new KeyboardRow();
        KeyboardRow row4=new KeyboardRow();
        KeyboardRow row5=new KeyboardRow();
        row1.add(new KeyboardButton("О приюте"));
        row2.add(new KeyboardButton("местоположение и график работы"));
        row3.add(new KeyboardButton("оформить пропуск"));
        row4.add(new KeyboardButton("оставить свои контактные данные"));
        row5.add(new KeyboardButton("позвать волонтера"));
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private SendMessage createMessageWithKeyboard(final long chatId, String textToSend, final ReplyKeyboardMarkup replyKeyboardMarkup) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }





}
