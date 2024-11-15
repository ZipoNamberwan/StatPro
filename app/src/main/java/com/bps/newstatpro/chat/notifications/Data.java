package com.bps.newstatpro.chat.notifications;

public class Data {

    private String user;
    private int icon;
    private String body;
    private String title;
    private String photo;
    private String username;
    private String click_action;

    public Data() {
    }

    public Data(String title, String body, String click_action) {
        this.body = body;
        this.title = title;
        this.click_action = click_action;
    }

    public Data(String user, String username, String photo, int icon, String body, String title, String sented) {

        this.user = user;
        this.icon = icon;

        this.body = body;
        this.title = title;
        this.sented = sented;
        this.photo = photo;
        this.username = username;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }

    private String sented;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
