package pro.sky.telegrambot.classes;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.service.NotificationService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class NotificationScheduler {

    private final NotificationTaskRepository taskRepository;
    private final NotificationService notificationService;

    public NotificationScheduler(NotificationTaskRepository taskRepository, NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void checkDueNotifications() {
        LocalDateTime currentMinute = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> dueTasks = taskRepository.findByNotificationTime(currentMinute);
        for (NotificationTask task : dueTasks) {
            sendNotification(task);
        }
    }

    private void sendNotification(NotificationTask task) {
        String chatId = task.getChatId().toString();
        String messageText = "Время для напоминания: " + task.getNotificationText();

        TelegramBot bot = new TelegramBot("6412658293:AAGGjfUcDKrHfkSatLyv0L0j4OEdddEtCTU");

        SendMessage message = new SendMessage(chatId, messageText);

        try {
            SendResponse response = bot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

