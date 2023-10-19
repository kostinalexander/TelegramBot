package com.example.shelterBot.listener;

import com.example.shelterBot.config.BotConfig;
import com.example.shelterBot.repository.UsersRepository;
import com.example.shelterBot.service.MenuServiceCat;
import com.example.shelterBot.service.MenuServiceDog;
import com.example.shelterBot.service.UsersService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static com.example.shelterBot.listener.Constants.DOG_BUTTON;

@Service
@Slf4j
public class ShelterBot extends TelegramLongPollingBot {

    final BotConfig config;

    @Autowired
    private final UsersRepository userRepository;
    private final UsersService usersService;
    private final MenuServiceCat menuServiceCat;
    private final MenuServiceDog menuServiceDog;

    private Boolean isCat = null;

    /**
     * Конструктор, в котором содержится меню для бота с коммандами.
     *
     * @param config
     * @param menuServiceCat
     * @param menuServiceDog
     */
    public ShelterBot(BotConfig config, UsersRepository userRepository, UsersService usersService, MenuServiceCat menuServiceCat, MenuServiceDog menuServiceDog) {
        this.config = config;
        this.userRepository = userRepository;
        this.usersService = usersService;
        this.menuServiceCat = menuServiceCat;
        this.menuServiceDog = menuServiceDog;

        List<BotCommand> commandList = new ArrayList<>();
        commandList.add(new BotCommand("/start", "обновить"));
        commandList.add(new BotCommand("/shelter", "выбрать приют"));
        commandList.add(new BotCommand("/volunteer", "волонтёр"));
        commandList.add(new BotCommand("/menu_cat ", "menu приюта для кошек"));
        commandList.add(new BotCommand("/menu_dog", "menu приюта для собак"));
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
                    registerUsers(update);
                    break;
                case Constants.ABOUT_SHELTER: {
                    if (isCat == null) {
                        sendMessage(chatId,"сначала выберите приют!");
                    } else {
                        if (isCat) {
                            sendMessage(chatId, Constants.FAQ);
                        } else {
                            sendMessage(chatId, Constants.FAQD);
                        }
                    }
                }
                break;
                case Constants.TAKE: {
                    if(isCat==null){
                        sendMessage(chatId,"Сначала выберите приют");
                    } else if (isCat) {
                        sendMessage(chatId,Constants.TAKE_CAT);
                    }else{
                        sendMessage(chatId,Constants.TAKE_DOG);
                    }
                }
                break;

                case Constants.REPORT_HOW:
                    sendMessage(chatId,Constants.REPORT);
                    break;

                case Constants.VOLUNTEER_HELP:
                    volunteerCommand(chatId);
                    break;

                case "/shelter":
                    shelterCommand(chatId);
                    menuCat(chatId);
                    menuDog(chatId);
                    break;
                case "/volunteer":
                    volunteerCommand(chatId);
                    break;
                case "/menu_cat":
                    menuCat(chatId);
                    break;
                case "/menu_dog":
                    menuDog(chatId);
                    break;
                case "/savecar":
                    saveCar(chatId);
                    break;
                case "/safety":
                    safety(chatId);
                    break;
                case "/datauser":
                    if (dataUser(chatId)) {
                        sendMessage(chatId, "Укажите, пожалуйста, телефон для связи", menuServiceCat.getMenuKeyboard());
                    }
                    sendMessage(1298936886, update.getMessage().getText(), menuServiceCat.getMenuKeyboard());
                    break;

                default:
                    sendMessage(chatId, "Извините, мы не можем ответить на этот вопрос.", menuServiceCat.getMenuKeyboard());

            }
        } else if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callBackData.equals(Constants.CAT_BUTTON)) {
                isCat = true;
                sendMessage(chatId, Constants.CAT_SHELTER, menuServiceCat.getMenuKeyboard());
            } else if (callBackData.equals(DOG_BUTTON)) {
                isCat = false;
                sendMessage(chatId, Constants.DOG_SHELTER, menuServiceDog.getMenuKeyboard());
            }
        }

    }


    /**
     * Метод отвечающий за комманду start. Бот, приветствует пользователя в зависимости от его данных(имени, фамилии и никнейма)
     *
     * @param chatId
     */

    private void startCommand(long chatId, String userName, String firstName, String lastName) {
        String textChat = "Добро пожаловать в бот, %s! %n" +
                "Здесь Вы сможете узнать о приютах для животных, а так же связаться с волонтером. %n" +
                "Все необходимые команды вы сможете найти в меню";
        String fullName = userName + " " + firstName + " " + lastName;
        if (userName == null & lastName == null) {
            var formattedText = String.format(textChat, firstName);
            sendMessage(chatId, formattedText, menuServiceCat.getMenuKeyboard());
        } else if (fullName == null) {
            sendMessage(chatId, textChat, menuServiceCat.getMenuKeyboard());
        } else {
            var formattedText = String.format(textChat, userName);
            sendMessage(chatId, formattedText, menuServiceCat.getMenuKeyboard());
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
        button1.setCallbackData(Constants.CAT_BUTTON);

        var button2 = new InlineKeyboardButton();
        button2.setText("Приют для собак");
        button2.setCallbackData(Constants.DOG_BUTTON);

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

    private void sendMessage(long chatId, String textToSend) {
        sendMessage(chatId, textToSend, null);
    }

    /**
     * Метод отвечающий за отправку сообщений
     *
     * @param chatId
     * @param textToSend
     */
    private void sendMessage(long chatId, String textToSend, ReplyKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        message.setReplyMarkup(keyboard);
        executeMessage(message);
    }

  /*  private void executeEditMessageText(String text, long chatId, long messageId) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(chatId));
        editMessage.setText(text);
        editMessage.setMessageId((int) messageId);
        sendMessage(chatId, text, menuServiceCat.getMenuKeyboard());
    }*/

    private void menuCat(long chatId) throws TelegramApiException {
        execute(menuServiceCat.getMenuMessage(chatId, "Воспользуйтесь меню"));
    }

    private void menuDog(long chatId) throws TelegramApiException {
        execute(menuServiceDog.getMenuMessage(chatId, "Воспользуйтесь меню "));
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(Constants.ERROR_TEXT + e.getMessage());
        }
    }

    private void registerUsers(Update update) {
        var textMessage = update.getMessage();
        var telegramUser = textMessage.getFrom();
        var chatId = update.getMessage().getChatId();
        if (!usersService.ifUserExists(telegramUser)) {
            String userName = update.getMessage().getChat().getUserName();
            String firstName = update.getMessage().getChat().getFirstName();
            String lastName = update.getMessage().getChat().getLastName();
            startCommand(chatId, userName, firstName, lastName);
        }
        shelterCommand(chatId);
    }
}
