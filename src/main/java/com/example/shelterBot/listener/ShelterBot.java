package com.example.shelterBot.listener;

import com.example.shelterBot.config.BotConfig;
import com.example.shelterBot.repository.UsersRepository;
import com.example.shelterBot.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ShelterBot extends TelegramLongPollingBot {

    final BotConfig config;

    @Autowired
    private final UsersRepository userRepository;
    private final UsersService usersService;

    static final String CAT_BUTTON = "CAT_BUTTON";
    static final String DOG_BUTTON = "DOG_BUTTON";
    private static final String START = "/start";
    private static final String SHELTERS = "/shelters";
    private static final String VOLUNTEER = "/volunteer";

    static final String ERROR_TEXT = "Error occurred: ";

    public ShelterBot(BotConfig config, UsersRepository userRepository, UsersService usersService) {
        this.config = config;
        this.userRepository = userRepository;
        this.usersService = usersService;
        List<BotCommand> commandList = new ArrayList<>();
        commandList.add(new BotCommand("/start", "комманда для старта"));
        commandList.add(new BotCommand("/shelters", "команда для выбора приюта"));
        commandList.add(new BotCommand("/volunteer", "команда для вызова волонтёра"));
// пока оставлю так
        List<BotCommand> catShelters = new ArrayList<>();
        catShelters.add(new BotCommand("/shelter_data","расписание работы приюта и адрес, схема проезда"));
        catShelters.add(new BotCommand("/security","контактные данные охраны для оформления пропуска на машину"));
        catShelters.add(new BotCommand("/safety_precautions","рекомендации о технике безопасности на территории приюта"));



        try {
            this.execute(new SetMyCommands(commandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot commands" + e.getMessage());
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
                case START -> {
                    var textMessage = update.getMessage();
                    var telegramUser = textMessage.getFrom();
                    registerUsers(telegramUser);
                    String userName = update.getMessage().getChat().getUserName();
                    String firstName = update.getMessage().getChat().getFirstName();
                    String lastName = update.getMessage().getChat().getLastName();
                    startCommand(chatId, userName, firstName, lastName);
                }
                case SHELTERS -> shelterCommand(chatId);

                case VOLUNTEER -> volunteerCommand(chatId);

                default -> sendMessage(chatId, "Пока, что команда не поддерживается ");

            }
        } else if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callBackData.equals(CAT_BUTTON)) {
                String text = "Поздравляем, вы выбрали приют для кошек";
                executeEditMessageText(text, chatId, messageId);


            } else if (callBackData.equals(DOG_BUTTON)) {
                String text = "Отлично, вы выбрали приют для собак";
                executeEditMessageText(text, chatId, messageId);
            }

        }

    }


    private void startCommand(long chatId, String userName, String firstName, String lastName) {
        String textChat = "Добро пожаловать в бот, %s! %n " +
                "Здесь Вы сможете узнать о приютах для животных, а так же связаться с волонтером. %n" +
                "Все необходимые команды вы сможете найти в меню";
        String fullName = userName + " " + firstName + " " + lastName;
        if (userName == null & lastName == null) {
            var formattedText = String.format(textChat, firstName);
            sendMessage(chatId, formattedText);
        } else if (fullName == null) {
            sendMessage(chatId, textChat);
        } else {
            var formattedText = String.format(textChat, userName);
            sendMessage(chatId, formattedText);
            log.info("Replied to user " + fullName);
        }
    }


    private void shelterCommand(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите приют");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var button1 = new InlineKeyboardButton();
        button1.setText("Приют для кошек");
        button1.setCallbackData("CAT_BUTTON");

        var button2 = new InlineKeyboardButton();
        button2.setText("Приют для собак");
        button2.setCallbackData("DOG_BUTTON");

        rowInline.add(button1);
        rowInline.add(button2);

        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        executeMessage(message);

    }

    private void volunteerCommand(long chatId) {
        var text = "Повсем вопросам обращайтесь к https://t.me/Axekill93";
        sendMessage(chatId, text);
    }

    private void executeEditMessageText(String text, long chatId, long messageId) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(chatId));
        editMessage.setText(text);
        editMessage.setMessageId((int) messageId);
        sendMessage(chatId, text);

    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);
    }


    private void registerUsers(User telegramUser) {
        usersService.findOrSaveUsers(telegramUser);
    }
}
