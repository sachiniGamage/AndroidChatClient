package com.example.chatclient.messageutil;

import com.example.chatclient.stub.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class ChatClient implements Runnable {
    private ChatServiceGrpc.ChatServiceStub chatStub;
    private static ChatClient chatClient = new ChatClient();
    private Channel channel;
    private StreamObserver<ChatMessage> reqObserver;
    private Queue<String> msgQueue = new LinkedBlockingQueue<>();
    private List<String> msgList = new ArrayList<>();

    @Override
    public void run() {
        initConnection();
        watchMessages();
    }

    public static ChatClient getInstance() {
        return chatClient;
    }

    public void addMsgToQueue(String message) {
        System.out.println("Messge Added to the queue" + message);
        msgQueue.add(message);
    }

    private void initConnection() {
        String target = "10.0.2.2:50052";
        this.channel =  ManagedChannelBuilder.forTarget(target).usePlaintext().build();
        this.chatStub = ChatServiceGrpc.newStub(channel);
    }

    private void watchMessages() {
        reqObserver = chatStub.chat(new StreamObserver<ChatMessageFromServer>() {
            @Override
            public void onNext(ChatMessageFromServer value) {
                System.out.println("recieved message " + value.getMessage().getMessage());
                getMsgList().add(value.getMessage().getMessage());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("completed");
            }
        });

        try {
            processMessageQueue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void processMessageQueue() throws InterruptedException {
        while (msgQueue.isEmpty()) {
                Thread.sleep(5000);
        }
        String msg = msgQueue.poll();
        ChatMessage message = ChatMessage.newBuilder().setMessage("sdfsdfadf").setFrom("Ann").build();
        reqObserver.onNext(message);
        Thread.sleep(5000);
        if (!msgQueue.isEmpty()) {
            processMessageQueue();
        }
    }

    public List<String> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<String> msgList) {
        this.msgList = msgList;
    }
}
