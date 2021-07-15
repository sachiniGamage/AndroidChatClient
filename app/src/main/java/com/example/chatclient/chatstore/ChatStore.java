package com.example.chatclient.chatstore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatStore {
    // static friendlist // TODO: Later
    // static email
    // static token
    // static username
    static ArrayList<String> friendList ;
    static Map<String, String> emailFriendNameMap = new HashMap<String, String>();
    static String email;
    static String username;
    static String token;

    public ChatStore() {
    }

    public static void addFriendNameEmailToMap(String name,String email){
        emailFriendNameMap.put(name,email);
    }

    public static String getFriendEmailFromNameToMap(String name){

        return emailFriendNameMap.get(name);
    }

    public static ArrayList<String> getFriendList() {
        return friendList;
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

    public static void setFriendList(ArrayList<String> friendList) {
        ChatStore.friendList = friendList;
    }
}
