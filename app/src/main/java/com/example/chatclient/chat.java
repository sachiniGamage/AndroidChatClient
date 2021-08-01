package com.example.chatclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.chatclient.messageutil.ChatClient;
import com.example.chatclient.stub.ChatMessage;
import com.example.chatclient.stub.ChatMessageFromServer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class chat extends Activity {
    TextView username, displayChat;
    Button sendComment;
    EditText text_send;
    ImageView ProfileImage;
    ArrayList<String> msgArr = new ArrayList<String>();
//    private List<Observer> observers = new ArrayList<Observer>();

//    FrameLayout displayChat


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        new Thread(ChatClient.getInstance()).start();


        Intent intent = getIntent();
        sendMsg();
        image();
        FriendName();
        System.out.println("display chat2");
        ChatClient.getInstance().addChat(intent.getStringExtra("Name"),this);
    }

    //send the message and show it in the text view
    private void sendMsg(){
        text_send = (EditText) findViewById(R.id.textSend);
        sendComment = (Button) findViewById(R.id.sendComment);
        displayChat =  findViewById(R.id.displayChat);



//        displayChat.setText(ChatClient.getInstance().);
        System.out.println("display chat1");
        displayChat.setMovementMethod(new ScrollingMovementMethod());
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String name = intent.getStringExtra("email");
                ChatClient.getInstance().processMsg(name,text_send.getText().toString());
//                msgArr.add(text_send.toString());
//                ChatClient.getInstance().addMsgToQueue(text_send.getText().toString());
//                displayChat.setText(ChatClient.getInstance().getMsgFromQueue());
//                ChatClient.getInstance().watchMessages();
//                displayChat.setText();
                displayChat.setMovementMethod(new ScrollingMovementMethod());
                System.out.println("display chat1");
            }
        });

    }

    //go to friend list
    private void image(){
        ProfileImage =(ImageView)findViewById(R.id.ProfileImage);

        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), friendList.class);
                startActivity(intent);
            }
        });
    }

    private  void FriendName(){
        username = findViewById(R.id.userName);
        Intent intent = getIntent();

        String name = intent.getStringExtra("Name");
        String email = intent.getStringExtra("email");
        username.setText(name);
//        ChatClient.getInstance().processMsg(name);
    }

    public void DisplayChatMsgs(String msg){
        displayChat =  findViewById(R.id.displayChat);

        Intent intent = getIntent();
        System.out.println("display chat4");



        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // Stuff that updates the UI
                displayChat.setText(msg);
                System.out.println("display chat msg");

            }
        });
    }


}
