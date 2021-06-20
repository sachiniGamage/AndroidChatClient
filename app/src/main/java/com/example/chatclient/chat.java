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
//    private ScrollView chatView;
//    ExecutorService myExecutor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        new Thread(ChatClient.getInstance()).start();

        username = (TextView) findViewById(R.id.userName);
        //msg
        text_send = (EditText) findViewById(R.id.textSend);
        //send button
        sendComment = (Button) findViewById(R.id.sendComment);
        ProfileImage =(ImageView)findViewById(R.id.ProfileImage);
//        chatView = (ScrollView) findViewById(R.id.chatVIew);
        displayChat = (TextView) findViewById(R.id.displayChat);

        displayChat.setText(ChatClient.getInstance().getMsgFromQueue());
        System.out.println("display chat1");
        displayChat.setMovementMethod(new ScrollingMovementMethod());

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatClient.getInstance().addMsgToQueue(text_send.getText().toString());
//                String msg =  text_send.getText().toString();
                System.out.println("display chat");
//                displayChat.append(msg);
                displayChat.setText(ChatClient.getInstance().getMsgFromQueue());
                System.out.println("display chat1");
                displayChat.setMovementMethod(new ScrollingMovementMethod());
//                try
//                {
//                    myExecutor = Executors.newCachedThreadPool();
//                    myExecutor.execute(ChatClient.getInstance());
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }

            }
        });





        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), friendList.class);
                startActivity(intent);
            }
        });
    }

    public void onClick(View view) {
    }

//    public void displayMessages(){
//        String msg =  (String)sendComment.getText();
////        ChatClient.getInstance().watchMessages();
////        ChatClient.getInstance().getMsgFromQueue();
//        displayChat.setText(ChatClient.getInstance().getMsgFromQueue());
//
//        chatView.fullScroll(ScrollView.FOCUS_DOWN);
//    }

}
