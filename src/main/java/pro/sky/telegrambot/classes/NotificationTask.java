package pro.sky.telegrambot.classes;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_task")
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "notification_text", nullable = false)
    private String notificationText;

    @Column(name = "notification_time", nullable = false)
    private LocalDateTime notificationTime;

    public Long getId() {
        return id;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public LocalDateTime getNotificationTime() {
        return notificationTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public void setNotificationTime(LocalDateTime notificationTime) {
        this.notificationTime = notificationTime;
    }

}
