package com.example.shraddha.handyman;

public class notificationInfo {
    private String toUser;
    private String fromHandyman;
    private String notification;

    public notificationInfo(){}

    public notificationInfo(String toUser, String fromHandyman, String notification) {
        this.toUser = toUser;
        this.fromHandyman = fromHandyman;
        this.notification = notification;
    }

    public String getToUser() {
        return toUser;
    }

    public String getFromHandyman() {
        return fromHandyman;
    }

    public String getNotification() {
        return notification;
    }
}
