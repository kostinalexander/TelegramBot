package com.example.shelterBot.listener;

public interface Constants {

    String ABOUT_SHELTER = "О приюте";

    String ACCEPTED = "Принято";
    String NOT_ACCEPTED = "Не принято";

    String CAT_BUTTON = "CAT_BUTTON";
    String DOG_BUTTON = "DOG_BUTTON";
    String DOG_SHELTER = "Отлично, вы выбрали приют для собак";
    String CAT_SHELTER = "Отлично, вы выбрали приют для кошек";
    String FAQ = """
            приют находится по адресу-г.Москва, ул.Искры 23А.
                        
            время работы приюта 9:00-18:00 без выходных.
                        
            Техника безопасноти на территории приюта.
            На территории приюта запрещено:
            Распивать алкогольные напитки,
            Дразнить животных,
            Воровать животных,
            Проносить предметы угрожающие здоровью животных
                        
            Контактные данные для оформления пропуска:
            Позвоните по этому телефону чтобы оформить пропуск - 89204579697            
            """;
    String FAQD = """
            приют находится по адресу- г.Москва, ул.Брусилова 32Б
                        
            время работы приюта 9:00-18:00 без выходных.
                        
            Техника безопасноти на территории приюта.
            На территории приюта запрещено:
            Распивать алкогольные напитки,
            Дразнить животных,
            Воровать животных,
            Проносить предметы угрожающие здоровью животных
                        
            Контактные данные для оформления пропуска:
            Позвоните по этому телефону чтобы оформить пропуск - 89204579697            
            """;

    String ERROR_TEXT = "Error occurred: ";

    String TAKE_CAT = """
            Чтобы забрать животное, вам необходимо приехать непосредственно 
            в наш приют, имея при себе необходимые документы, а именно - 
            Паспорт
            Снилс
            Страховка
                        
            Котёнка необходимо будет перевозить держа аккуратно в руках 
            и придерживая его голову. 
                   
            Маленькие новорождённые котята, ещё слепые и хрупкие
            за ними необходим особый уход и контроль. Будьте внимательнее 
            и берегите их.
            Необходимо поить молоком 6 раз в день, купать аккуратно 1 раз в неделю
            также необходимо постоянно развивать маленького котёнка.
                        
            Для котёнка необходимо купить миску, лоток и когтеточку.
            Необходимо спрятать все провода, что бы он их не погрыз.
                        
            Вам могут отказать в приобритении животного, если вы будете:
            В нетрезвом состоянии
            Очень занятой человек, у которого нет времени на животное
            Не будет необходимых документов.
                        
            Вы можете оставить нам контактные данные для обратной связи 
            или можете связаться с нашим волонтёром самостоятельно.
                        
            Желаем всего доброго и отличных взаимоотношений с вашим питомцем.
                        
                        
            """;

    String TAKE_DOG = """
            Чтобы забрать животное, вам необходимо приехать непосредственно 
            в наш приют, имея при себе необходимые документы, а именно - 
            Паспорт
            Снилс
            Страховка
                        
            Щенка необходимо перевозить в переноске и присматривать за ним 
            по пути
                    
            Будьте аккуратны, собаки могут кусаться , а щенки бояться вас.
            Подходите к животным, как можно ласковее.
            Животное необходимо кормить 3-4 раза в день, купать 2 раза в неделю
            сушить феном и уделять внимание.
                        
            Для щенка необходимо купить миску, когтеточку, игрушки желательно в виде
            косточки.
                        
            Опытные кинологи рекомендуют кормить собаку купленным и приготовленным мясом
            не используя сухой корм.
                        
            Вам могут отказать в приобритении животного, если вы будете:
            В нетрезвом состоянии
            Очень занятой человек, у которого нет времени на животное
            Не будет необходимых документов.
                        
            Вы можете оставить нам контактные данные для обратной связи 
            или можете связаться с нашим волонтёром самостоятельно.
                        
            Желаем всего доброго и отличных взаимоотношений с вашим питомцем.   
                    
            """;
    String TAKE = "Как взять животное из приюта";

    String REPORT_NOW = "Прислать отчет о питомце";

    String REPORT = """
            Пришлите, пожалуйста, отчёт в виде
            фотография + описание животного
            """;

    String VOLUNTEER_HELP = "позвать волонтера";
    String VOLUNTEER = "Повсем вопросам обращайтесь к https://t.me/Axekill93";
    String START_TEXT="Добро пожаловать в бот, %s! %n" +
            "Здесь Вы сможете узнать о приютах для животных, а так же связаться с волонтером. %n" +
            "Все необходимые команды вы сможете найти в меню";



}
