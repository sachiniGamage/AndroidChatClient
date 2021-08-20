package com.example.chatclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chatclient.chatstore.ChatStore;
import com.example.chatclient.chatstore.GenPrivateKey;
import com.example.chatclient.messageutil.ChatClient;
import com.example.chatclient.stub.LoginUser;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class login extends AppCompatActivity {

    private TextInputEditText email,Password;
    TextView NewUser;
    TextView newUser_hyperlink;
    Button signin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signin();
        registerLink();

    }

    private void signin(){
        email = findViewById(R.id.email);
        Password = findViewById(R.id.Password);
        signin = findViewById(R.id.signin);

//        String[] arry = new String[2];
//        arry[0] = "a";
//        arry[1] = "b";

//        try {
//            GenPrivateKey.write(this,"sample.txt",arry);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            GenPrivateKey.read(this,"sample.txt");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            GenPrivateKey.genKeyPairIfNotExist1(this);
            System.out.println(ChatStore.getPublicKey());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.equals("")) {
                    email.setError("Can't be a blank");
                }
                else if(Password.equals("")){
                    Password.setError("Can't be a blank");
                }else {
                    ChatStore.setEmail(email.getText().toString());


                    boolean isAuthenticated = ChatClient.getInstance().login(email.getText().toString(), Password.getText().toString());
                    // TODO: return string/object from login function and proceed to next view only if login is successful.
                    System.out.println("login done");




                    if(isAuthenticated == true){
//                        startActivity(new Intent(login.this, chat.class).putExtra("LoginEmail", email.getText()));
                        startActivity(new Intent(login.this, friendList.class));
                    }
                    else{
                        System.out.println("Login failed");
                        startActivity(new Intent(login.this, login.class));
                    }
                }
            }
        });
    }

    private void registerLink(){
        newUser_hyperlink = findViewById(R.id.newUser_hyperlink);

        newUser_hyperlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), register.class);
                startActivity(intent);
            }
        });
    }

}