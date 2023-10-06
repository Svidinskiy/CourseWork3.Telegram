package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.classes.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.service.NotificationService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationTaskRepository notificationTaskRepository;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            if (update.message() != null) {
                long chatId = update.message().chat().id();
                String messageText = update.message().text();

                if ("/start".equals(messageText)) {
                    String welcomeMessage = "Добро пожаловать! Я ваш телеграм-бот!";
                    telegramBot.execute(new SendMessage(chatId, welcomeMessage));
                } else if (messageText != null && messageText.matches("\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2} .+")) {
                    try {
                        notificationService.parseAndSaveNotification(chatId, messageText);
                    } catch (IllegalArgumentException e) {
                        telegramBot.execute(new SendMessage(chatId, "Ошибка: " + e.getMessage()));
                    }
                } else {
                    // Обработка некорректного сообщения
                    telegramBot.execute(new SendMessage(chatId, "Некорректное сообщение. Используйте: /start или дд.мм.гггг чч:мм Текст уведомления"));
                }


            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }



}
