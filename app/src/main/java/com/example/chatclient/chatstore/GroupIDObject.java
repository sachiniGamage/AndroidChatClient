package com.example.chatclient.chatstore;

public class GroupIDObject {
    private String grpID;
    private String grpName;

    public GroupIDObject(String grpID, String grpName) {
        this.grpID = grpID;
        this.grpName = grpName;
    }

    public String getGrpName() {
        return grpName;
    }

    public String getGrpID() {
        return grpID;
    }

    @Override
    public String toString(){
        return grpName;
    }
}
