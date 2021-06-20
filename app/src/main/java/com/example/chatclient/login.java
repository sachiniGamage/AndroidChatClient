package com.example.chatclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

public class login extends AppCompatActivity {

    private TextInputEditText email,Password;
    TextView NewUser;
    TextView newUser_hyperlink;
    Button signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        Password = findViewById(R.id.Password);
        newUser_hyperlink = findViewById(R.id.newUser_hyperlink);
//        NewUser = findViewById(R.id.signup);
        signin = findViewById(R.id.signin);

        newUser_hyperlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), register.class);
//                startActivity(intent);
//                startActivity(new Intent(login.this, register.class));
                Intent intent = new Intent(getApplicationContext(), register.class);
                startActivity(intent);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, chat.class));
            }
        });
    }

    public void onClick(View view) {
        startActivity(new Intent(login.this, register.class));
    }
}