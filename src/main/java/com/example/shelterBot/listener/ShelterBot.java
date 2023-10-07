package com.example.shelterBot.listener;

import com.example.shelterBot.config.BotConfig;
import com.example.shelterBot.model.Users;
import com.example.shelterBot.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ShelterBot extends TelegramLongPollingBot {

    final BotConfig config;

    @Autowired
    private UsersRepository userRepository;

    public ShelterBot(BotConfig config) {
        this.config = config;
        List<BotCommand>commandList = new ArrayList<>();
        commandList.add(new BotCommand("/start", "комманда для старта"));
        commandList.add(new BotCommand("/shelter", "команда для выбора приюта"));
        commandList.add(new BotCommand("/volunteer", "команда для вызова волонтёра"));

        try {
            this.execute(new SetMyCommands(commandList, new BotCommandScopeDefault(), null));
        }catch (TelegramApiException e){
            log.error("Error setting bot commands"+e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    registerUser(update.getMessage());
                    startCommand(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/shelter":
                    shelterCommand(chatId);
                    break;
                case "/volunteer":
                    volunteerCommand(chatId);
                    break;
                default:sendMessage(chatId,"Пока, что команда не поддерживается ");

            }
        }else if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callBackData.equals("CAT_BUTTON")){
                String text = "Поздравляем, вы выбрали приют для кошек";
                EditMessageText message = new EditMessageText();
                message.setChatId(String.valueOf(chatId));
                message.setText(text);
                message.setMessageId(Integer.valueOf((int) messageId));

                try {
                    execute(message);
                }catch (TelegramApiException e){
                    log.info("Inline button complete" + e.getMessage());
                }

            } else if (callBackData.equals("DOG_BUTTON")) {
                String text1 = "Отлично, вы выбрали приют для собак";
                EditMessageText message1 = new EditMessageText();
                message1.setChatId(String.valueOf(chatId));
                message1.setText(text1);
                message1.setMessageId(Integer.valueOf((int) messageId));

                try {
                    execute(message1);
                }catch (TelegramApiException e){
                    log.info("Inline button complete" + e.getMessage());
                }
            }

        }

    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
        }

    }


    private void startCommand(long chatId, String name) {
        String answer = "Добро пожаловать в бот, " +
                "Здесь Вы сможете узнать о приютах для животных, а так же связаться с волонтером, " +
                "все необходимые команды вы сможете найти в меню";
        log.info("Replied to user " + name);
        sendMessage(chatId, answer);
    }


    private void registerUser(Message message) {

        Long id = message.getChatId();
        Chat chat = message.getChat();
        Users user = new Users();
        user.setId(id);
        user.setFirstName(chat.getFirstName());
        user.setLastName(chat.getLastName());
        user.setFirstLoginDate(LocalDateTime.now());
        if (userRepository.findById(id).isEmpty()) {
            userRepository.save(user);
            log.info("user saved " + user);
        }
    }

    private void shelterCommand(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите приют");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var button = new InlineKeyboardButton();
        button.setText("Приют для кошек");
        button.setCallbackData("CAT_BUTTON");

        var noButton = new InlineKeyboardButton();
        noButton.setText("Приют для собак");
        noButton.setCallbackData("DOG_BUTTON");

        rowInline.add(button);
        rowInline.add(noButton);

        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        }catch (TelegramApiException e){
            log.info("Inline button complete" + e.getMessage());
        }
    }

     private void volunteerCommand(long chatId){
       var text ="Повсем вопросам обращайтесь к https://t.me/Axekill93";
        sendMessage(chatId,text);
     }
}
