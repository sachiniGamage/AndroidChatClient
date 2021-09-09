package com.example.chatclient.chatstore;

import android.content.Context;

import com.google.protobuf.StringValue;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatStore {
    // static friendlist // TODO: Later
    // static email
    // static token
    // static username
    static ArrayList<String> friendList ;
    static ArrayList<String> groupList;
    static Map<String, String> emailFriendNameMap = new HashMap<String, String>();
    static Map<String, String> emailSymmetricKeyMap = new HashMap<String, String>();
    static Map<String, String> friendEmailDecryptedKeyMap = new HashMap<String, String>();
    static Map<String, String> grpIDAndGroupNameMap = new HashMap<String, String>();
    static String email;
    static String username;
    static String token;
    static PublicKey publicKey;
    static PrivateKey privateKey;
    static Context c;

    public ChatStore() {
    }

    public static void addGrpIdGrpNameToMap(String grpId, String grpName){
        grpIDAndGroupNameMap.put(grpId,grpName);
    }

    public static String getGrpNameFromGrpIdToMap(String grpId){
        return grpIDAndGroupNameMap.get(grpId);
    }

    public static Map<String, String> getGrpIDAndGroupNameMap() {
        return grpIDAndGroupNameMap;
    }

    public static PublicKey getPublicKey() {
        return publicKey;
    }

    public static PrivateKey getPrivateKey() {
        return privateKey;
    }

    public static void setPublicKey(PublicKey publicKey) {
        ChatStore.publicKey = publicKey;
    }

    public static void setPrivateKey(PrivateKey privateKey) {
        ChatStore.privateKey = privateKey;
    }

    public static void addFriendNameEmailToMap(String name, String email){
        emailFriendNameMap.put(name,email);
    }

    public static void addFriendEmailDecryptedKeyMap(String emailF, String decryptedKey){
        friendEmailDecryptedKeyMap.put(emailF,decryptedKey);
    }

    public static String getFriendEmailFromNameToMap(String name){

        return emailFriendNameMap.get(name);
    }

    public static String getDecryptedKeyFromFriendEmail(String emailF){
        return friendEmailDecryptedKeyMap.get(emailF);
    }

    public static void addEmailSymmetricKeyToMap(String email, String symmetricKey){
        emailSymmetricKeyMap.put(email,symmetricKey);
    }

    public static String getSymmetricKeyFromEmailToMap(String email){
        return emailSymmetricKeyMap.get(email);
    }

    public static String getFriendEmailToNameToMap(String name){

        return emailFriendNameMap.get(name);
    }

    public static ArrayList<String> getFriendList() {
        return friendList;
    }

    public static ArrayList<String> getGroupList(){
        return groupList;
    }

    public static void setGroupList(ArrayList<String> groupList){
        ChatStore.groupList = groupList;
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
