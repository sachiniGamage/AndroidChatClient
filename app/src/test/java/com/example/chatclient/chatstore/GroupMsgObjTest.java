package com.example.chatclient.chatstore;

import static org.junit.jupiter.api.Assertions.*;

class GroupMsgObjTest {

    @org.junit.jupiter.api.Test
    void getMsg() {
        GroupMsgObj g = new GroupMsgObj("abcTest","larry@g.com");
        assertEquals("abcTest",g.getMsg());
    }

    @org.junit.jupiter.api.Test
    void getFriendemail() {
    }
}