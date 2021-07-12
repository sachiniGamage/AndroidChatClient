package com.example.chatclient;

public class Messages {
    private String username, sendmsg;

    public Messages(String username, String sendmsg) {
        this.username = username;
        this.sendmsg = sendmsg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSendmsg() {
        return sendmsg;
    }

    public void setSendmsg(String sendmsg) {
        this.sendmsg = sendmsg;
    }
}
