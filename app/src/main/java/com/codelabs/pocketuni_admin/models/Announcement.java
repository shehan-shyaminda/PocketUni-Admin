package com.codelabs.pocketuni_admin.models;

public class Announcement {    String notificationDate, notificationDesc, notificationTitle;

    public Announcement(String notificationDate, String notificationDesc, String notificationTitle) {
        this.notificationDate = notificationDate;
        this.notificationDesc = notificationDesc;
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getNotificationDesc() {
        return notificationDesc;
    }

    public void setNotificationDesc(String notificationDesc) {
        this.notificationDesc = notificationDesc;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }
}
