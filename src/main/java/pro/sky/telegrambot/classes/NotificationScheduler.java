package pro.sky.telegrambot.classes;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
    private final TelegramBot telegramBot;

    @Autowired
    public NotificationScheduler(NotificationTaskRepository taskRepository, NotificationService notificationService,  TelegramBot telegramBot) {
        this.taskRepository = taskRepository;
        this.notificationService = notificationService;
        this.telegramBot = telegramBot;
    }

    @Scheduled(cron = "0 0/1 * * * *")
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

        SendMessage message = new SendMessage(chatId, messageText);

        try {
            SendResponse response = telegramBot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

