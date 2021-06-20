package com.example.chatclient.messageutil;

public class ThreadQ extends Thread {
    public void run(){
        System.out.println("thread is running...");
    }

    public static void main(String args[]){

        ThreadQ thread=new ThreadQ();
        thread.start();


    }
}
