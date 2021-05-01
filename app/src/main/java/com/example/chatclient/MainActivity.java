package com.example.chatclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chatclient.messageutil.ChatClient;

public class MainActivity extends AppCompatActivity {

    TextView username;
    Button send_comment;
    EditText text_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(ChatClient.getInstance()).start();

        username = findViewById(R.id.username);
        send_comment = findViewById(R.id.send_comment);
        text_send = findViewById(R.id.text_send);

        ChatClient.getInstance().addMsgToQueue(text_send.toString());
    }


}