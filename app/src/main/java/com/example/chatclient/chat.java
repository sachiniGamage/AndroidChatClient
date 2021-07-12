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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class chat extends Activity {
    TextView username, displayChat;
    Button sendComment;
    EditText text_send;
    ImageView ProfileImage;
//    FrameLayout displayChat


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        new Thread(ChatClient.getInstance()).start();

        sendMsg();
        imageAndName();

    }

    //send the message and show it in the text view
    private void sendMsg(){
//        username = (TextView) findViewById(R.id.userName);
        text_send = (EditText) findViewById(R.id.textSend);
        sendComment = (Button) findViewById(R.id.sendComment);
        displayChat =  findViewById(R.id.displayChat);

        displayChat.setText(ChatClient.getInstance().getMsgFromQueue());
        System.out.println("display chat1");
        displayChat.setMovementMethod(new ScrollingMovementMethod());
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatClient.getInstance().addMsgToQueue(text_send.getText().toString());
                displayChat.setText(ChatClient.getInstance().getMsgFromQueue());
                displayChat.setMovementMethod(new ScrollingMovementMethod());
                System.out.println("display chat1");
            }
        });

    }

    //go to friend list
    private void imageAndName(){
        ProfileImage =(ImageView)findViewById(R.id.ProfileImage);
        username = findViewById(R.id.userName);

//        Bundle b = getIntent().getExtras();
//        String email = (String) b.get("email") ;
//        username.setText(email);
        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), friendList.class);
                startActivity(intent);
            }
        });
    }
}
