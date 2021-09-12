package com.example.chatclient.chatstore;

public class GroupMsgObj {
    private String msg;
    private String friendemail;

    public GroupMsgObj(String msg, String friendemail) {
        this.msg = msg;
        this.friendemail = friendemail;
    }

    public String getMsg() {
        return msg;
    }

    public String getFriendemail() {
        return friendemail;
    }
}
