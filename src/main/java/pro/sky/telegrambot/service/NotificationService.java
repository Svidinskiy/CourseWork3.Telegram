package pro.sky.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.classes.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NotificationService {

    private final NotificationTaskRepository taskRepository;

    @Autowired
    public NotificationService(NotificationTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void parseAndSaveNotification(Long chatId, String message) {

        Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String dateAndTime = matcher.group(1);
            String text = matcher.group(3);

            LocalDateTime dueDateTime = LocalDateTime.parse(dateAndTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

            NotificationTask task = new NotificationTask();
            task.setChatId(653215522L);
            task.setNotificationText(text);
            task.setNotificationTime(dueDateTime);

            taskRepository.save(task);
        }
    }
}

