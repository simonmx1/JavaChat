package com.example.javachat;

public class Text {
    private String content;
    private String user;
    private boolean you;

    public Text(String content, String user, boolean you) {
        this.content = content;
        this.user = user;
        this.you = you;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isYou() {
        return you;
    }

    public void setYou(boolean you) {
        this.you = you;
    }
}
