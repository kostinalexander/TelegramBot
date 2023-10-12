package com.example.shelterBot.listener;

import com.example.shelterBot.config.BotConfig;
import com.example.shelterBot.repository.UsersRepository;
import com.example.shelterBot.service.UsersService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
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
    static final String DOG_SHELTER = "Отлично, вы выбрали приют для собак";
    static final String CAT_SHELTER = "Отлично, вы выбрали приют для кошек";
    static final String FAQ = """
            приют находится по адресу-г.Москва, ул.Искры 23А.
                        
            время работы приюта 9:00-18:00 без выходных.
                        
            """;
    static final String FAQD = """
            приют находится по адресу-г.Москва, ул.Брусилова 32Б.
                        
            время работы приюта 9:00-18:00 без выходных.
                        
            """;
    static final String SAFETY = """
            На территории приюта запрещено:
                            Распивать алкогольные напитки
                            Дразнить животных
                            Воровать животных
                            Проносить предметы угрожающие здоровью животных
            """;
    private static final String START = "/start";
    private static final String SHELTERS = "/shelters";
    private static final String VOLUNTEER = "/volunteer";

    static final String ERROR_TEXT = "Error occurred: ";


    /**
     * Конструктор, в котором содержится меню для бота с коммандами.
     *
     * @param config
     */
    public ShelterBot(BotConfig config, UsersRepository userRepository, UsersService usersService) {
        this.config = config;
        this.userRepository = userRepository;
        this.usersService = usersService;

        List<BotCommand> commandList = new ArrayList<>();
        commandList.add(new BotCommand("/start", "обновить"));
        commandList.add(new BotCommand("/shelter", "выбрать приют"));
        commandList.add(new BotCommand("/volunteer", "волонтёр"));
        commandList.add(new BotCommand("/addresscat ", "адрес приюта для кошек"));
        commandList.add(new BotCommand("/addressdog", "команда адрес приюта для собак"));
        commandList.add(new BotCommand("/savecar", "оформление пропуска"));
        commandList.add(new BotCommand("/safety", "техникой безопасности"));
        commandList.add(new BotCommand("/datauser", "оставить данные"));


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

    /**
     * Метод process, основной метод нашего бота. В нём будут содержаться, базовые ответы бота, пользователю
     * и обработка сообщений.
     *
     * @param update
     */
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    var textMessage = update.getMessage();
                    var telegramUser = textMessage.getFrom();
                    registerUsers(telegramUser);
                    String userName = update.getMessage().getChat().getUserName();
                    String firstName = update.getMessage().getChat().getFirstName();
                    String lastName = update.getMessage().getChat().getLastName();
                    startCommand(chatId, userName, firstName, lastName);
                    break;
                case "/shelter":
                    shelterCommand(chatId);
                    break;
                case "/volunteer":
                    volunteerCommand(chatId);
                    break;
                case "/addresscat":
                    addressCat(chatId);
                    break;
                case "/addressdog":
                    addressDog(chatId);
                    break;
                case "/savecar":
                    saveCar(chatId);
                    break;
                case "/safety":
                    safety(chatId);
                    break;
                case "/datauser":
                    if (dataUser(chatId)) {
                        sendMessage(chatId, "Укажите, пожалуйста, телефон для связи");
                    }
                    sendMessage(1298936886, update.getMessage().getText());
                    break;

                default:
                    sendMessage(chatId, "Извините, мы не можем ответить на этот вопрос.");

            }
        } else if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String queryId=update.getCallbackQuery().getId();

            if (callBackData.equals(CAT_BUTTON)) {
                executeEditMessageText(CAT_SHELTER, chatId, messageId);

            } else if (callBackData.equals(DOG_BUTTON)) {
                executeEditMessageText(DOG_SHELTER, chatId, messageId);
            }
            buttonTap(chatId, queryId, callBackData, (int) messageId);

        }

    }


    /**
     * Метод отвечающий за комманду start. Бот, приветствует пользователя в зависимости от его данных(имени, фамилии и никнейма)
     *
     * @param chatId
     */

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


    /**
     * Метод определяющий какой приют выбрал пользователь
     *
     * @param chatId
     */
    private void shelterCommand(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите приют");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var button1 = new InlineKeyboardButton();
        button1.setText("Приют для кошек");
        button1.setCallbackData(CAT_BUTTON);

        var button2 = new InlineKeyboardButton();
        button2.setText("Приют для собак");
        button2.setCallbackData(DOG_BUTTON);

        rowInline.add(button1);
        rowInline.add(button2);

        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        executeMessage(message);

    }


    /**
     * Метод вызывающий волонтёра.
     *
     * @param chatId
     */
    private void volunteerCommand(long chatId) {
        var text = "Повсем вопросам обращайтесь к https://t.me/Axekill93";
        sendMessage(chatId, text);
    }

    /**
     * метод указывающий на адрес приюта для собак
     *
     * @param chatId
     */
    private void addressDog(long chatId) {
        var text = "Приют находится по адресу г.Москва, ул.Брусилова 32Б";
        sendMessage(chatId, text);
    }

    /**
     * метод указывающий на адрес приюта для кошек
     *
     * @param chatId
     */
    private void addressCat(long chatId) {
        var text = "Приют находится по адресу г.Москва, ул.Искры 23А ";
        sendMessage(chatId, text);
    }

    /**
     * Метод для демонстрации контактного адреса для связи
     *
     * @param chatId
     */
    private void saveCar(long chatId) {
        var text = "Для оформления пропуска позвоните по номеру 89204579697, Александр";
        sendMessage(chatId, text);
    }

    /**
     * Метод с техникой безопасности
     *
     * @param chatId
     */
    private void safety(long chatId) {
        var text = "На территории приюта запрещено: " +
                "Распивать алкогольные напитки" +
                "Дразнить животных" +
                "Воровать животных" +
                "Проносить предметы угрожающие здоровью животных";
        sendMessage(chatId, text);
    }

    /**
     * Метод для записи контактов пользователя
     *
     * @param chatId
     * @return
     */
    private boolean dataUser(long chatId) {
        return true;
    }

    private InlineKeyboardMarkup cat;
    private InlineKeyboardMarkup dog;

    private void buttonTap(Long id, String queryId, String data, int msgId) throws TelegramApiException {


        List<List<InlineKeyboardButton>> catsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        var button1 = new InlineKeyboardButton();
        button1.setText("FAQ");
        button1.setCallbackData(FAQ);

        var button2 = new InlineKeyboardButton();
        button2.setText("ТБ");
        button2.setCallbackData(SAFETY);

        rowInline.add(button1);
        rowInline.add(button2);
        catsInline.add(rowInline);
        cat.setKeyboard(catsInline);

        List<List<InlineKeyboardButton>> dogsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

        var bt = new InlineKeyboardButton();
        button1.setText("FAQ");
        button1.setCallbackData(FAQ);

        var bt2 = new InlineKeyboardButton();
        button2.setText("ТБ");
        button2.setCallbackData(SAFETY);

        rowInline.add(bt);
        rowInline.add(bt2);
        catsInline.add(rowInline2);
        dog.setKeyboard(dogsInline);

        EditMessageText newTxt = EditMessageText.builder()
                .chatId(id.toString())
                .messageId(msgId).text("").build();

        EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder()
                .chatId(id.toString()).messageId(msgId).build();
        if (data.equals(CAT_BUTTON)) {
            newTxt.setText(CAT_SHELTER);
            newKb.setReplyMarkup(cat);
        } else if (data.equals(DOG_BUTTON)) {
            newTxt.setText(DOG_SHELTER);
            newKb.setReplyMarkup(dog);
        }
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId).build();

        execute(close);
        execute(newTxt);
        execute(newKb);
    }

    /**
     * Метод отвечающий за отправку сообщений
     *
     * @param chatId
     * @param textToSend
     */
    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);
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


    private void registerUsers(User telegramUser) {
        usersService.findOrSaveUsers(telegramUser);
    }
}
