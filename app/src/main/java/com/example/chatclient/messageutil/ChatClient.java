package com.example.chatclient.messageutil;

import com.example.chatclient.chat;
import com.example.chatclient.chatstore.ChatStore;
import com.example.chatclient.chatstore.GroupMsgObj;
import com.example.chatclient.groupChat;
import com.example.chatclient.stub.*;
import com.google.protobuf.StringValue;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import io.grpc.stub.StreamObserver;

import static java.nio.charset.StandardCharsets.UTF_8;


public class ChatClient implements Runnable {
    private ChatServiceGrpc.ChatServiceStub chatStub;
    private AuthenticateUserGrpc.AuthenticateUserBlockingStub authStub;
    private UpdateUserGrpc.UpdateUserBlockingStub updateStub;
    private static ChatClient chatClient = new ChatClient();
    private Channel channel;
    private StreamObserver<ChatMessage> reqObserver;
    private StreamObserver<GroupMessage> reqObserverGrp;
    private Queue<String> msgQueue = new LinkedBlockingQueue<>();
    private ArrayList<String> msgArr = new ArrayList<String>();
    private ArrayList<String> grpmsgArr = new ArrayList<String>();
    private List<String> msgList = new ArrayList<>();
    private List<String> grpMsgList = new ArrayList<>();
    private  ArrayList<String> friendArr = new ArrayList<String>();
    private ArrayList<String> grpArr = new ArrayList<String>();
    private ArrayList<String> recievedMsgArr = new ArrayList<>();
    private Map<String,chat> ChatObserver = new HashMap<String, chat>();

    public Map<String, ArrayList<String>> getChatFrndsMap() {
        return chatFrndsMap;
    }
    public Map<String, ArrayList<GroupMsgObj>> getGrpIdMsgsMap() {
        return GrpIdMsgsMap;
    }
    private Map<String,ArrayList<String>> chatFrndsMap = new HashMap<String, ArrayList<String>>();
    private Map<String, ArrayList<GroupMsgObj>> GrpIdMsgsMap = new HashMap<String, ArrayList<GroupMsgObj>>();

    String currentChatFriendName,currentToEmail, grpID;
    chat chatUI;
    groupChat grpChatUI;
    private static final Logger logger = Logger.getLogger(ChatClient.class.getName());
    private final static int CRYPTO_BITS = 2048;
    private final static String CRYPTO_METHOD = "RSA";
    static KeyPairGenerator kpg;

    @Override
    public void run() {
        initConnection();
        watchMessages();
        watchGrpMsg();
    }

    public static ChatClient getInstance() {
        return chatClient;
    }

    //add messages to queue
    public void addMsgToQueue(String message) {
        System.out.println("Message Added to the queue" + message);
        msgQueue.add(message);
        msgArr.add(message);
    }

    public void initConn(){
        String target = "35.244.15.151:50052";
        if (channel == null) {
            synchronized (new Object()) {
                if (channel == null) {
                    this.channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
                }
            }
        }
    }

    //channel
    public void initConnection() {
        initConn();
        if(chatStub == null){
            synchronized (new Object()) {
                    this.chatStub = ChatServiceGrpc.newStub(channel);
                    //metadata
                    Metadata headers = new Metadata();
                    Metadata.Key<String> metaKey = Metadata.Key.of("fromuser", Metadata.ASCII_STRING_MARSHALLER);
                    if (ChatStore.getEmail() != (null)) {
                        headers.put(metaKey, ChatStore.getEmail());
                        chatStub = MetadataUtils.attachHeaders(chatStub, headers);
                    }
            }
        }
        System.out.println("Connection established.");
    }

    //view group messages
    public void watchGrpMsg() {
        reqObserverGrp = chatStub.groupChat(new StreamObserver<GroupMessageFromServer>() {
            @Override
            public void onNext(GroupMessageFromServer value) {
                System.out.println("recieved message " + value.getTimestamp() + "msg : " + value.getGroupList().getMsg() + value.getGroupList().getFriendEmail());
                getGrpMsgList().add(value.getGroupList().getMsg());
                String msg = value.getGroupList().getMsg();
                String id = value.getGroupList().getGroupDetails().getGroupId();
                String friend = value.getGroupList().getGroupDetails().getFriendEmail();

                    if (GrpIdMsgsMap.get(id)== null) {
                        GrpIdMsgsMap.put(id, new ArrayList<GroupMsgObj>());
                    }

                    try {
                        System.out.println("display chat3");
                        GroupMsgObj obj = new GroupMsgObj(msg,friend);
                        GrpIdMsgsMap.get(id).add(obj);
                        if (grpChatUI != null) {
                            if (friend.equals(ChatStore.getEmail())) {
                                grpChatUI.displayToMsg(msg);
                                System.out.println("displayToMsg");
                            } else {
                                System.out.println("displayGrpMsg");
                                grpChatUI.displayGrpMsg(msg);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

                try {
                    //decrypt message
                    String msg = givenPassword_whenDecrpt_thenSuccess(msgs, "baeldung", "12345678");
                    System.out.println("display chat3");
                    chatFrndsMap.get(from).add(msg);

                    String currentFriendEmail = ChatStore.getFriendEmailFromNameToMap(currentChatFriendName);

                    if(from.equals(currentFriendEmail) ) {
                        System.out.println("display chat5");
                        chatUI.DisplayChatMsgs(msg);
                    }else if(to.equals(currentFriendEmail)){
                        System.out.println("messages equal to :" + msg);
                        chatUI.displayToMsg(msg);
                    }
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
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

    }

    public void addChat(String user, chat observer){
        this.chatUI = observer;
        this.currentChatFriendName = user;
    }

    public void addGrpChat(String grpid, groupChat observer){
        this.grpChatUI = observer;
        this.grpID = grpid;
    }


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
            String email = "abc@gmail.com";
            ChatMessage message = ChatMessage.newBuilder().setMessage(msg).setFrom(email).build();
            reqObserver.onNext(message);
            Thread.sleep(5000);
    }

//add a group message
    public void processGroupMsg(String friendemail, String groupId,String message){
        initConnection();
        if (updateStub == null) {
            this.updateStub = UpdateUserGrpc.newBlockingStub(channel);
            System.out.println("UpdateStub");
        }
        GroupMessage groupMessage = GroupMessage.newBuilder().setGroupDetails(MakeGroup.newBuilder().setFriendEmail(friendemail).setGroupId(groupId)).setMsg(message).build();
        reqObserverGrp.onNext(groupMessage);
        System.out.println(grpmsgArr.add(groupMessage.toString()));
    }

    //add a message - private chat
    public void processMsg(String touser,String msg) throws BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        initConnection();
        if (updateStub == null) {
            this.updateStub = UpdateUserGrpc.newBlockingStub(channel);
            System.out.println("UpdateStub");
        }
        String fromuser = ChatStore.getEmail();
        String messages = msg;

        //encrypt the message
        String msgs = givenPassword_whenEncrypt_thenSuccess(msg,"baeldung","12345678");
        System.out.println("msg encrpt");
        ChatMessage message = ChatMessage.newBuilder().setFrom(fromuser).setTo(touser).setMessage(msgs).build();
        reqObserver.onNext(message);
        System.out.println(msgArr.add(message.toString()));
    }

    public List<String> getMsgList() {
        return msgList;
    }
    public List<String> getGrpMsgList(){
        return grpMsgList;
    }

    public void setMsgList(List<String> msgList) {
        this.msgList = msgList;
    }

    //pass register details to server
    public  void register(String email,String password,String pblcKey,String username){
        initConn();
        if (authStub == null) {
            this.authStub = AuthenticateUserGrpc.newBlockingStub(channel);
        }
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
            this.authStub = AuthenticateUserGrpc.newBlockingStub(channel);
        System.out.println("login");

        LoginUser user = LoginUser.newBuilder().setEmail(email).setPassword(password).build();
        System.out.println("login1");
        Token token = authStub.login(user);

        //decrypt
        byte[] decodedBytes = Base64.getDecoder().decode(token.getPublicKey());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedBytes);
        KeyFactory kf = null;
        try {
            kf = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        PublicKey pb = null;
        try {
            pb = kf.generatePublic(spec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        System.out.println(pb);

        ChatStore.setPublicKey(pb);
        if(token.getToken().equals("")){
            System.out.println("User not available");
            return false;
        }else{
            System.out.println("login successful");
            //mage email address ekata adaala friendsla
            getFriendlist(email);
            getGroupList(email);

            return true;
        }
    }

    //get groups
    public void getGroupList(String myEmail){
        initConnection();
        if (updateStub == null) {
            this.updateStub = UpdateUserGrpc.newBlockingStub(channel);
            System.out.println("UpdateStub");
        }
        ViewGroup response = updateStub.getGroup(StringValue.of(myEmail));
        System.out.println("group list - get");
        System.out.println(response.getGrpDetailsList());
        //add groups to a arraylist
        ArrayList arrayList = new ArrayList();
        for(MakeGroup makeGroup : response.getGrpDetailsList()){
            arrayList.add(makeGroup.getGroupName());
            ChatStore.addGrpIdGrpNameToMap(makeGroup.getGroupId(), makeGroup.getGroupName());
        }
        ChatStore.setGroupList(arrayList);
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
        ArrayList arrayList = new ArrayList();
        for (RegisterUser registerUser : response.getFriendsInListList()) {
            System.out.println("*************************GetFriendlist************************");
            System.out.println("***************************************************************");

            arrayList.add(registerUser.getUsername());
            System.out.println("Encrypted key " + registerUser.getEncryptedKey());

            ChatStore.addFriendNameEmailToMap(registerUser.getUsername(),registerUser.getEmail());
            ChatStore.addEmailSymmetricKeyToMap(registerUser.getEmail(),registerUser.getEncryptedKey());

            try {
                //decrypt
                String myDecrypt = decryption(registerUser.getEncryptedKey(),ChatStore.getPrivateKey());
                System.out.println("myDecrypt"+myDecrypt);

                ChatStore.addFriendEmailDecryptedKeyMap(registerUser.getEmail(),myDecrypt);

            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
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

    //Create a group
    public String createGroup(String groupId, String groupName, String adminEmail,String friendEmail){
        initConnection();

        if(updateStub == null){
            this.updateStub = UpdateUserGrpc.newBlockingStub(channel);
            System.out.println("createGroup - chatstub - null");
        }
        System.out.println("createGroup - chatstub");
        System.out.println(ChatStore.getEmail());
        System.out.println(groupName);
        MakeGroup makeGroup = MakeGroup.newBuilder().setGroupId(groupId).setGroupName(groupName).setAdminEmail(adminEmail).setFriendEmail(friendEmail).build();
        MakeGroup response = updateStub.createGroup(makeGroup);
        System.out.println(response);
        grpArr.add(response.getGroupName());
        String grpname = response.getGroupName();
        ChatStore.setGroupList(grpArr);
        System.out.println(grpname);
        return grpname;
    }

    //add new friend
    public String updateFriendList(String emailf) throws NoSuchAlgorithmException {
        initConnection();
        if (updateStub == null) {
            this.updateStub = UpdateUserGrpc.newBlockingStub(channel);
            System.out.println("UpdateStub");
        }
        kpg = KeyPairGenerator.getInstance(CRYPTO_METHOD);
        kpg.initialize(CRYPTO_BITS);

        //symmetric key
        String randomString = "xyz";

        String myemail = ChatStore.getEmail();
        AddFriendReq friendrequest = AddFriendReq.newBuilder().setDetail(FriendList.newBuilder().setFriendsEmail(emailf).build()).setMyemail(myemail).build();
        AddFriendReq response = updateStub.addFriend(friendrequest);
        System.out.println("public key" + response.getDetail().getPublicKey());
            System.out.println("Friend is available - chatClient");
            System.out.println(response.getDetail().getUsername().getUsername());
            String frndName  = response.getDetail().getUsername().getUsername().toString();
            String publicKey = response.getDetail().getPublicKey();
            System.out.println("get public key");
            System.out.println(publicKey);

            try {
                //Creating a Cipher object
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

                //Initializing a Cipher object
                byte[] decodedBytes = Base64.getDecoder().decode(publicKey);
                X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedBytes);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                PublicKey pb = kf.generatePublic(spec);
                System.out.println(pb);

                //encryption
                String friendEncrypt= encrypt(randomString,pb);
                System.out.println("friendEncrypt "+ friendEncrypt);
                String myEncrypt = encrypt(randomString,ChatStore.getPublicKey());
                System.out.println("myEncrypt"+myEncrypt);

                AddFriendReq friendrequest1 = AddFriendReq.newBuilder().setDetail(FriendList.newBuilder().setFriendsEmail(emailf).build()).setMyemail(myemail).setAddedEmailf1(friendEncrypt).setAddbymyemail(myEncrypt).build();
                AddFriendReq response1 = updateStub.addFriend(friendrequest1);
                System.out.println("get response from friendrequest");
                System.out.println(response1.getDetail().getPublicKey());
                String myDecrypt = decryption(response1.getAddbymyemail(),ChatStore.getPrivateKey());
                System.out.println("getAddbymyemail() "+response1.getAddbymyemail());
                System.out.println("myDecrypt"+myDecrypt);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                System.out.println("invalid key exception when decrypt");
                e.printStackTrace();
            }

            return frndName;

    }

    //message decrypt
    String givenPassword_whenDecrpt_thenSuccess(String msg, String password, String salt)
            throws InvalidKeySpecException, NoSuchAlgorithmException,
            IllegalBlockSizeException, InvalidKeyException, BadPaddingException,
            InvalidAlgorithmParameterException, NoSuchPaddingException {

        String cipherText = msg;
        IvParameterSpec ivParameterSpec = AESUtil.generateIv();
        SecretKey key = AESUtil.getKeyFromPassword(password,salt);
        String decryptedCipherText = AESUtil.decryptPasswordBased(
                cipherText, key, ivParameterSpec);

        return decryptedCipherText;
    }

    //message encrypt
    String givenPassword_whenEncrypt_thenSuccess(String msg, String password, String salt)
            throws InvalidKeySpecException, NoSuchAlgorithmException,
            IllegalBlockSizeException, InvalidKeyException, BadPaddingException,
            InvalidAlgorithmParameterException, NoSuchPaddingException {

        String plainText = msg;
        IvParameterSpec ivParameterSpec = AESUtil.generateIv();
        SecretKey key = AESUtil.getKeyFromPassword(password,salt);
        String cipherText = AESUtil.encryptPasswordBased(plainText, key, ivParameterSpec);

        return cipherText;
    }

    private static final String ALGORITHM = "AES";

    public String encrypt(String msg, String symkey) {
        String encryptedVal = null;
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES/CTR/PKCS5PADDING");
            generator.init(128);
            SecretKey key = generator.generateKey();
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

        } catch(Exception ex) {
            System.out.println("The Exception is=" + ex);
        }
        return encryptedVal;
    }

    //encrypt
    public static String encrypt(String plainText, PublicKey publicKey)  {
        Cipher encryptCipher = null;
        try {
            encryptCipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        byte[] cipherText = new byte[0];
        try {
            cipherText = encryptCipher.doFinal(plainText.getBytes(UTF_8));
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(cipherText);
    }

    //decrypt
    public static String decryption(String cipherText, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] bytes = Base64.getDecoder().decode(cipherText);

        Cipher decriptCipher = Cipher.getInstance("RSA");
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decriptCipher.doFinal(bytes));

    }

}
