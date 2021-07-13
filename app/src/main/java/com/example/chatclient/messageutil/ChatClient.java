package com.example.chatclient.messageutil;

import android.app.Dialog;

import com.example.chatclient.stub.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;


public class ChatClient implements Runnable {
    private ChatServiceGrpc.ChatServiceStub chatStub;
    private AuthenticateUserGrpc.AuthenticateUserBlockingStub authStub;
    private UpdateUserGrpc.UpdateUserBlockingStub updateStub;
    private static ChatClient chatClient = new ChatClient();
    private Channel channel;
    private StreamObserver<ChatMessage> reqObserver;
    private Queue<String> msgQueue = new LinkedBlockingQueue<>();
    private ArrayList<String> messagingList = new ArrayList<String>();
    private List<String> msgList = new ArrayList<>();
    private  ArrayList<String> friendArr = new ArrayList<String>();
    private static final Logger logger = Logger.getLogger(ChatClient.class.getName());


    @Override
    public void run() {
        initConnection();
        watchMessages();
    }

    public static ChatClient getInstance() {
        return chatClient;
    }


    //add messages to queue
    public void addMsgToQueue(String message) {
        System.out.println("Message Added to the queue" + message);
        msgQueue.add(message);
//        messagingList.add(message);
    }


    //channel
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

    //check whether the messages are received to the server side
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


    //get messages from queue(peek value)
//    public String getMsgFromQueue(){
//        return messagingList.get(0);
//    }

    public String getMsgFromQueue(){
        return msgQueue.peek();
    }


    //pass message and email to server
    public void processMessageQueue() throws InterruptedException {
        while (msgQueue.isEmpty()) {
                Thread.sleep(1000);
        }


        String msg = msgQueue.poll();
//        String message1 = messagingList.set(0,msg);
//        Iterator<String> iterator = msgQueue.iterator();
//        while(iterator.hasNext()) {
//            String element = iterator.next();
            String email = "abc@gmail.com";
            ChatMessage message = ChatMessage.newBuilder().setMessage(msg).setFrom(email).build();
            reqObserver.onNext(message);
//            messagingList.add(message);
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


    //pass register details to server
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


    //login
    public boolean login(String email, String password){
        initConnection();
        if (authStub == null) {
            this.authStub = AuthenticateUserGrpc.newBlockingStub(channel);
        }
        String encodedEmail = Base64.getEncoder().encodeToString(email.getBytes());
        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());

        System.out.println("login");

        LoginUser user = LoginUser.newBuilder().setEmail(email).setPassword(password).build();
        System.out.println("login1");
        Token token = authStub.login(user);
        if(token.getToken().equals("")){
            System.out.println("User not available");
            return false;
        }else{
            System.out.println("login successful");
            return true;
        }
    }

    public void updateName(String name){
        initConnection();
        if (updateStub == null) {
            this.updateStub = UpdateUserGrpc.newBlockingStub(channel);
        }
        System.out.println("UpdateStub");

        Edit edit1 = Edit.newBuilder().setUsername(name).build();
        RegisterUser username = updateStub.updateName(edit1);

        System.out.println("update name");

    }

    public String updateFriendList(String email){
        initConnection();
        if (updateStub == null) {
            this.updateStub = UpdateUserGrpc.newBlockingStub(channel);
            System.out.println("UpdateStub");
        }
        FriendList friendrequest = FriendList.newBuilder().setFriendsEmail(email).build();
//        FriendList friendList = updateStub.addFriend(friendrequest);
//        FriendList.getDefaultInstance().getUsername();
        FriendList response = updateStub.addFriend(friendrequest);;

//        return response.getUsername().toString();
        if (response.getUsername().equals(null)){
            System.out.println("Friend is not available");
            return null;
        }else{
            System.out.println("Friend is available - chatClient");
            System.out.println(response.getUsername());
            String frndName  = response.getUsername().getUsername().toString();
            return frndName;
        }


//        FriendList response ;
//        try {
//            response = updateStub.addFriend(friendrequest);
//        }catch (Exception e){
//            System.out.println(e);
//        }
//        logger.info(response.getUsername().toString());


    }







}