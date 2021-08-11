package com.example.chatclient.messageutil;

import com.example.chatclient.chat;
import com.example.chatclient.chatstore.ChatStore;
import com.example.chatclient.stub.*;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import javax.crypto.Cipher;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import io.grpc.stub.StreamObserver;


public class ChatClient implements Runnable {
    private ChatServiceGrpc.ChatServiceStub chatStub;
    private AuthenticateUserGrpc.AuthenticateUserBlockingStub authStub;
    private UpdateUserGrpc.UpdateUserBlockingStub updateStub;
    private static ChatClient chatClient = new ChatClient();
    private Channel channel;
    private StreamObserver<ChatMessage> reqObserver;
    private Queue<String> msgQueue = new LinkedBlockingQueue<>();
    private ArrayList<String> msgArr = new ArrayList<String>();
    private List<String> msgList = new ArrayList<>();
    private  ArrayList<String> friendArr = new ArrayList<String>();
//    private  ArrayList<String> chatFrnds = new ArrayList<String>();
    private ArrayList<String> recievedMsgArr = new ArrayList<>();
    private Map<String,chat> ChatObserver = new HashMap<String, chat>();
    private Map<String,ArrayList<String>> chatFrndsMap = new HashMap<String, ArrayList<String>>();
    String currentChatFriendName,currentToEmail;
    chat chatUI;
//    static Map<String, String> emailFriendNameMap = new HashMap<String, String>();
    private static final Logger logger = Logger.getLogger(ChatClient.class.getName());

//    private chat currentChatUser;

    @Override
    public void run() {
        initConnection();
        watchMessages();


    }


//    Metadata.Key<String> jwtKey = Metadata.Key.of("jwt", Metadata.ASCII_STRING_MARSHALLER);
//                    headers.put(jwtKey, jwt);
//                    metadataApplier.apply(headers);
//} catch (Throwable e) {
//        metadataApplier.fail(Status.UNAUTHENTICATED.withCause(e));
//        }

    public static ChatClient getInstance() {
        return chatClient;
    }


    //add messages to queue
    public void addMsgToQueue(String message) {
        System.out.println("Message Added to the queue" + message);
        msgQueue.add(message);

        msgArr.add(message);
    }


    //channel
    public void initConnection() {
        String target = "192.168.8.104:50052";
        if (channel == null) {
            synchronized (new Object()) {
                if (channel == null) {
                    this.channel =  ManagedChannelBuilder.forTarget(target).usePlaintext().build();
                    this.chatStub = ChatServiceGrpc.newStub(channel);

                    Metadata headers = new Metadata();
                    Metadata.Key<String> metaKey = Metadata.Key.of("fromuser", Metadata.ASCII_STRING_MARSHALLER);
                    if(ChatStore.getEmail() != (null)) {
                        headers.put(metaKey, ChatStore.getEmail());
                        chatStub = MetadataUtils.attachHeaders(chatStub, headers);
                    }
                }
            }
        }


        System.out.println("Connection established.");
    }

    //check whether the messages are received to the server side
    public void watchMessages() {
        reqObserver = chatStub.chat(new StreamObserver<ChatMessageFromServer>() {
            @Override
            public void onNext(ChatMessageFromServer value) {

                System.out.println("recieved message " + value.getMessage().getMessage() );
                getMsgList().add(value.getMessage().getMessage());
                String msgs = value.getMessage().getMessage();
                String from = value.getMessage().getFrom();
                String to = value.getMessage().getTo();
                System.out.println(from +" is the key");
                if (chatFrndsMap.get(from)== null) {
                    chatFrndsMap.put(from, new ArrayList<String>());
                }
                System.out.println("display chat3");
                    chatFrndsMap.get(from).add(msgs);

                    String currentFriendEmail = ChatStore.getFriendEmailFromNameToMap(currentChatFriendName);

                if(currentFriendEmail.equals(from) ) {
                    System.out.println("display chat5");
                    chatUI.DisplayChatMsgs(msgs);
                }else if(currentFriendEmail.equals(to)){
                    System.out.println("messages equal to :" + msgs);
                    chatUI.displayToMsg(msgs);
                }
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
//        try {
////            processMessageQueue();
////            processMsg();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    public void addChat(String user, chat observer){
        this.chatUI = observer;
        this.currentChatFriendName = user;
    }


    //get messages from queue(peek value)
//    public String getMsgFromQueue(){
//        return messagingList.get(0);
//    }

    public void getCurrentUser(String user){

    }

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

    public void processMsg(String touser,String msg){
        initConnection();
        if (updateStub == null) {
            this.updateStub = UpdateUserGrpc.newBlockingStub(channel);
            System.out.println("UpdateStub");
        }
        String fromuser = ChatStore.getEmail();
//        String msg = null;

        ChatMessage message = ChatMessage.newBuilder().setFrom(fromuser).setTo(touser).setMessage(msg).build();

        reqObserver.onNext(message);


//            Metadata headers = new Metadata();
//            Metadata.Key<String> metaKey = Metadata.Key.of("fromuser", Metadata.ASCII_STRING_MARSHALLER);
//            headers.put(metaKey,fromuser);
//            chatStub = MetadataUtils.attachHeaders(chatStub,headers);

//        msgArr.add(message.toString());
        System.out.println(msgArr.add(message.toString()));

    }

    public List<String> getMsgList() {
        return msgList;
    }


    public void setMsgList(List<String> msgList) {
        this.msgList = msgList;
    }


    //pass register details to server
    public  void register(String email,String password,String pblcKey,String username){
        initConnection();
        if (authStub == null) {
            this.authStub = AuthenticateUserGrpc.newBlockingStub(channel);
        }
        String pblicKey = "a";
        System.out.println(email);
        System.out.println(password);
        System.out.println(username);
        RegisterUser user = RegisterUser.newBuilder().setEmail(email).setPassword(password).setPublicKey(pblcKey).setUsername(username).build();
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
            //mage email address ekata adaala friendsla
            getFriendlist(email);

            return true;
        }
    }

    // mage email address ekata adaala friendsla
    private void getFriendlist(String email){
        initConnection();
        if (updateStub == null) {
            this.updateStub = UpdateUserGrpc.newBlockingStub(channel);
            System.out.println("UpdateStub");
        }
        //This should be GetFriends in proto
        ViewFriends friendrequest = ViewFriends.newBuilder().setMyemail(email).build();

        ViewFriends response = updateStub.getFriends(friendrequest);
        System.out.println("getFriendsInListCount"+response.getFriendsInListCount());
        System.out.println("getFriendsNameInListCount"+response.getFriendsNameInListCount());
        ArrayList arrayList = new ArrayList();
        for (RegisterUser registerUser : response.getFriendsInListList()) {
            System.out.println("*************************GetFriendlist************************");
            System.out.println("***************************************************************");

            arrayList.add(registerUser.getUsername());

            ChatStore.addFriendNameEmailToMap(registerUser.getUsername(),registerUser.getEmail());
            System.out.println(arrayList);
        }

        ChatStore.setFriendList(arrayList);

    }
    


    // profile name
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

    //add new friend
    public String updateFriendList(String emailf){
        initConnection();
        if (updateStub == null) {
            this.updateStub = UpdateUserGrpc.newBlockingStub(channel);
            System.out.println("UpdateStub");
        }
        String myemail = ChatStore.getEmail();
        AddFriendReq friendrequest = AddFriendReq.newBuilder().setDetail(FriendList.newBuilder().setFriendsEmail(emailf).build()).setMyemail(myemail).build();
//        FriendList friendList = updateStub.addFriend(friendrequest);
//        FriendList.getDefaultInstance().getUsername();
        AddFriendReq response = updateStub.addFriend(friendrequest);;

//        return response.getUsername().toString();
        if (response.getDetail().equals(null)){
            System.out.println("Friend is not available");
            return null;
        }else{
            System.out.println("Friend is available - chatClient");
            System.out.println(response.getDetail().getUsername().getUsername());
            String frndName  = response.getDetail().getUsername().getUsername().toString();

            return frndName;
        }
    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();

        return pair;
    }

//    //First generate a public/private key pair
//    KeyPair pair = generateKeyPair();
//
//    //Our secret message
//    String message = "the answer to life the universe and everything";
//
//    //Encrypt the message
//    String cipherText = encryption(message, pair.getPublic());
//
//    //Now decrypt it
//    String decipheredMessage = decryption(cipherText, pair.getPrivate());
//
//System.out.println(decipheredMessage);

//encrypt
    public static String encryption(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes());

        return Base64.getEncoder().encodeToString(cipherText);
    }


    //decrypt
    public static String decryption(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);

        Cipher decriptCipher = Cipher.getInstance("RSA");
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decriptCipher.doFinal(bytes));
    }







}
