package com.example.shelterBot.listener;

import com.example.shelterBot.config.BotConfig;
import com.example.shelterBot.model.report.ReportCat;
import com.example.shelterBot.model.report.ReportDog;
import com.example.shelterBot.repository.UsersRepository;
import com.example.shelterBot.service.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.shelterBot.listener.Constants.DOG_BUTTON;

@Service
@Slf4j
public class ShelterBot extends TelegramLongPollingBot {

    private final String volunteerChatId;
    final BotConfig bot;

    @Autowired
    private final UsersRepository userRepository;
    private final UsersService usersService;
    private final CatService catService;
    private final DogService dogService;
    private final MenuServiceCat menuServiceCat;
    private final MenuServiceDog menuServiceDog;
    private final ReportCatService reportCatService;
    private final ReportDogService reportDogService;
    private final InlineService inlineService;



    private Boolean isCat = null;
    private boolean isNextMessageReport = false;

    /**
     * Конструктор, в котором содержится меню для бота с коммандами.
     *
     * @param bot
     * @param dogService
     * @param menuServiceCat
     * @param menuServiceDog
     * @param reportCatService
     * @param reportDogService
     */
    public ShelterBot(
            @Value("${shelter.volunteer.id}") String volunteerChatId,
            BotConfig bot, UsersRepository userRepository, UsersService usersService, CatService catService, DogService dogService, MenuServiceCat menuServiceCat, MenuServiceDog menuServiceDog, ReportCatService reportCatService, ReportDogService reportDogService, InlineService inlineService) {
        this.volunteerChatId = volunteerChatId;
        this.bot = bot;
        this.userRepository = userRepository;
        this.usersService = usersService;
        this.catService = catService;
        this.dogService = dogService;
        this.menuServiceCat = menuServiceCat;
        this.menuServiceDog = menuServiceDog;
        this.reportCatService = reportCatService;
        this.reportDogService = reportDogService;
        this.inlineService = inlineService;

        List<BotCommand> commandList = new ArrayList<>();
        commandList.add(new BotCommand("/start", "обновить"));
        commandList.add(new BotCommand("/shelter", "выбрать приют"));
        commandList.add(new BotCommand("/volunteer", "волонтёр"));
        commandList.add(new BotCommand("/menu_cat ", "menu приюта для кошек"));
        commandList.add(new BotCommand("/menu_dog", "menu приюта для собак"));
        commandList.add(new BotCommand("/datauser", "оставить данные"));


        try {
            this.execute(new SetMyCommands(commandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot commands" + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return bot.getBotName();
    }

    @Override
    public String getBotToken() {
        return bot.getToken();
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

        if (isNextMessageReport) {
            var messageText = update.getMessage().getCaption();
            Long chatId = update.getMessage().getChatId();
            if (isCat) {
                String[] args = messageText.split("\n");
                var cat = catService.findCat(Long.valueOf(args[0]));
                if (cat == null) {
                    sendMessage(chatId,"Такой кошки не найдено");
                }
                byte[] photo = savePhoto(update);
                if (photo.length == 0) {
                    sendMessage(chatId,"нет фото");
                    return;
                }
                reportCatService.saveReport(cat, args[1], photo);
            } else {
                String[] ar = messageText.split("\n");
                var dog = dogService.findDog(Long.valueOf(ar[0]));
                if(dog==null){
                    sendMessage(chatId,"Такой собаки не найдено");
                    return;
                }
                byte[] photo = savePhoto(update);
                reportDogService.saveReport(dog,ar[1],photo);
            }
            isNextMessageReport = false;
            return;
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            switch (messageText) {
                case "/start":
                    registerUsers(update);
                    break;
                case Constants.ABOUT_SHELTER: {
                    if (isCat == null) {
                        sendMessage(chatId, "сначала выберите приют!");
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
                    if (isCat == null) {
                        sendMessage(chatId, "Сначала выберите приют");
                    } else if (isCat) {
                        sendMessage(chatId, Constants.TAKE_CAT);
                    } else {
                        sendMessage(chatId, Constants.TAKE_DOG);
                    }
                }
                break;

                case Constants.REPORT_NOW:
                    sendMessage(chatId, Constants.REPORT);
                    isNextMessageReport = true;
                    break;

                case Constants.VOLUNTEER_HELP:
                    volunteerCommand(chatId);
                    break;

                case "/shelter":
                    shelterCommand(chatId);
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
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callBackData.equals(Constants.CAT_BUTTON)) {
                isCat = true;
                sendMessage(chatId, Constants.CAT_SHELTER, menuServiceCat.getMenuKeyboard());
            } else if (callBackData.equals(DOG_BUTTON)) {
                isCat = false;
                sendMessage(chatId, Constants.DOG_SHELTER, menuServiceDog.getMenuKeyboard());
            } else if (callBackData.contains(":")) {
                String[] arguments = callBackData.split(":");
                var reportId = Long.parseLong((arguments[0]));
                var userId = Long.parseLong(arguments[1]);
                var isCat = arguments[3].equals("cat");
                if (arguments[2].equals(Constants.ACCEPTED)) {
                    if (isCat) {
                        reportCatService.markReport(reportId, true);
                        var telegeramUSer = usersService.findUser(userId);
                        SendMessage reportAccepted = new SendMessage(String.valueOf(telegeramUSer.getTelegramUserId()),
                                "Ваш отчёт принят");
                        execute(reportAccepted);
                    } else {
                        reportDogService.markReport(reportId, true);
                        var telegramUser = usersService.findUser(userId);
                        SendMessage reportAccepted = new SendMessage(String.valueOf(telegramUser.getTelegramUserId()),
                                "Ваш отчёт принят");
                        execute(reportAccepted);
                    }
                } else {
                    var telegramUser = usersService.findUser(userId);
                    SendMessage reportNotAcceptedMessage = new SendMessage(String.valueOf(telegramUser.getTelegramUserId()),
                            "Ваш отчет о питомце не был принят! Пришлите новый!");
                    execute(reportNotAcceptedMessage);
                    if (isCat) {
                        reportCatService.markReport(reportId, false);
                    } else {
                        reportDogService.markReport(reportId, false);
                    }
                }

            }

        }

    }


    /**
     * Метод отвечающий за комманду start. Бот, приветствует пользователя в зависимости от его данных(имени, фамилии и никнейма)
     *
     * @param chatId
     */

    private void startCommand(long chatId, String userName, String firstName, String lastName) {
        String fullName = userName + " " + firstName + " " + lastName;
        if (userName == null & lastName == null) {
            var formattedText = String.format(Constants.START_TEXT, firstName);
            sendMessage(chatId, formattedText, menuServiceCat.getMenuKeyboard());
        } else if (fullName == null) {
            sendMessage(chatId, Constants.START_TEXT, menuServiceCat.getMenuKeyboard());
        } else {
            var formattedText = String.format(Constants.START_TEXT, userName);
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
        sendMessage(chatId, Constants.VOLUNTEER);
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

    private byte[] savePhoto(Update update) throws TelegramApiException {
        if (update.getMessage().getPhoto() != null) {
            var photo = update.getMessage().getPhoto().get(3); // 3 - самое лучшее качество
            var getFile = execute(new GetFile(photo.getFileId()));
            try (var in = new URL(getFile.getFileUrl(bot.getToken())).openStream();//здесь ругался на эту строчку пришлось в конфинг добалять fileId
                 var out = new FileOutputStream(photo.getFileId())) {
                byte[] photoBytes = in.readAllBytes();
                out.write(photoBytes);
                return photoBytes;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new byte[0];
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

//    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
//    public void getUncheckedReport() throws TelegramApiException, RuntimeException {
//        var unchecked = reportCatService.getAllUncheckedReports();
//        for (ReportCat unch : unchecked) {
//            SendPhoto report = new SendPhoto();
//            report.setChatId(volunteerChatId);
//
//            var bytes = unch.getPhoto();
//            var photoFile = new File(unch.getId() + "_" + unch.getCat().getId() + "_" + unch.getCat().getName());
//            try (var out = new FileOutputStream(photoFile)) {
//                out.write(bytes);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            report.setPhoto(new InputFile(new ByteArrayInputStream(bytes), "photo.jpg"));
//            execute(report);
//            photoFile.delete();
//
//            SendMessage reportMessage = new SendMessage(volunteerChatId, unch.getText());
//            inlineService.inlineCheck(unch, reportMessage);
//            execute(reportMessage);
//        }
//    }
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    public void getUncheckedReport() throws TelegramApiException, RuntimeException {
        var unchecked = reportCatService.getAllUncheckedReports();
        for (ReportCat unch : unchecked) {
            SendMessage reportMessage = new SendMessage(volunteerChatId, unch.getText());
            inlineService.inlineCheck(unch, reportMessage);
            execute(reportMessage);
        }
    }
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    public void getUncheckedReportDog() throws TelegramApiException, RuntimeException {
        var unchecked = reportDogService.getAllUncheckedReportsDog();
        for (ReportDog unch : unchecked) {
            SendMessage reportMessage = new SendMessage(volunteerChatId, unch.getText());
            inlineService.inlineDogCheck(unch, reportMessage);
            execute(reportMessage);
        }
    }
}
