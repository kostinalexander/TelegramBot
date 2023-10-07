package com.example.shelterBot.config;

import com.example.shelterBot.listener.ShelterBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Slf4j
public class BotInitializer {

     @Autowired
    ShelterBot bot;

     @EventListener({ContextRefreshedEvent.class})
     public void init() throws TelegramApiException{
         TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
         try {
              botsApi.registerBot(bot);
         }catch (TelegramApiException e){
             log.error("Ошибка регистрации" + e.getMessage());
         }
     }
}
