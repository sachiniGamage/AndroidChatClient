package com.example.chatclient.chatstore;

public class ChatStore {
    // static friendlist // TODO: Later
    // static email
    // static token
    // static username

    static String email;
    static String username;
    static String token;

    public ChatStore() {
    }

    public static String getEmail() {
        return email;
    }

    public static String getUsername() {
        return username;
    }

    public static String getToken() {
        return token;
    }

    public static void setEmail(String email) {
        ChatStore.email = email;
    }

    public static void setUsername(String username) {
        ChatStore.username = username;
    }

    public static void setToken(String token) {
        ChatStore.token = token;
    }
}
