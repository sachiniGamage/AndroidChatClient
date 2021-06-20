package com.example.chatclient.messageutil;

import com.example.chatclient.stub.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class ChatClient implements Runnable {
    private ChatServiceGrpc.ChatServiceStub chatStub;
    private AuthenticateUserGrpc.AuthenticateUserBlockingStub authStub;
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
        System.out.println("Message Added to the queue" + message);
        msgQueue.add(message);
    }

    public void initConnection() {
        String target = "10.0.2.2:50052";
        if (channel == null) {
            synchronized (new Object()) {
                if (channel == null) {
                    this.channel =  ManagedChannelBuilder.forTarget(target).usePlaintext().build();
                    this.chatStub = ChatServiceGrpc.newStub(channel);
                }
            }
        }

        System.out.println("Connection established.");
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

    public String getMsgFromQueue(){
        return msgQueue.peek();
    }


    public void processMessageQueue() throws InterruptedException {
        while (msgQueue.isEmpty()) {
                Thread.sleep(1000);
        }
        String msg = msgQueue.poll();
//        Iterator<String> iterator = msgQueue.iterator();
//        while(iterator.hasNext()) {
//            String element = iterator.next();

            String email = "abc@gmail.com";
            ChatMessage message = ChatMessage.newBuilder().setMessage(msg).setFrom(email).build();
            reqObserver.onNext(message);
            Thread.sleep(5000);
//            if (!msgQueue.isEmpty()) {
//                processMessageQueue();
//            }
//        }
    }

    public List<String> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<String> msgList) {
        this.msgList = msgList;
    }

    public  void register(String email,String password,String username){
        initConnection();
        if (authStub == null) {
            this.authStub = AuthenticateUserGrpc.newBlockingStub(channel);
        }
        System.out.println(email);
        System.out.println(password);
        System.out.println(username);
        RegisterUser user = RegisterUser.newBuilder().setEmail(email).setPassword(password).setUsername(username).build();
        authStub.register(user);
        System.out.println("inside register");
    }
}
