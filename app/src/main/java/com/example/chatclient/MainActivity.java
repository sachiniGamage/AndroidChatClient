package com.example.chatclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chatclient.messageutil.ChatClient;

public class MainActivity extends AppCompatActivity {
    Button btn_signup, btn_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_signup = findViewById(R.id.btn_signup);
        btn_signin = findViewById(R.id.btn_signIn);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, register.class));
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, login.class));
            }
        });

//    TextView username;
//    Button send_comment;
//    EditText text_send;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        new Thread(ChatClient.getInstance()).start();
//
//        username = (TextView)findViewById(R.id.username);
//        //msg
//        text_send = (EditText)findViewById(R.id.text_send);
//        //send button
//        send_comment = (Button)findViewById(R.id.send_comment);
//
//        send_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ChatClient.getInstance().addMsgToQueue(text_send.getText().toString());
//            }
//        });


    }


}