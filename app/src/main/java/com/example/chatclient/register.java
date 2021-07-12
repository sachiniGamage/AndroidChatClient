package com.example.chatclient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chatclient.messageutil.ChatClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class register extends AppCompatActivity {

    private TextInputEditText name,email, password, confirmPassword;
    private TextView AlreadySignUp_hyperlink;
    Button signup;
    private String user,pass,ConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signup();
        hyperlink();

    }

    private void signup() {
        email = (TextInputEditText) findViewById(R.id.email);
        password = findViewById(R.id.Password);
        confirmPassword = findViewById(R.id.ConfirmPassword);
        signup = findViewById(R.id.signup);
        name = findViewById(R.id.name);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = email.getText().toString();
                pass = password.getText().toString();
                ConfirmPass = confirmPassword.getText().toString();

                if(user.equals("")) {
                    email.setError("Can't be a blank");
                }
                else if(pass.equals("")){
                    password.setError("Can't be a blank");
                }
                else if(ConfirmPass.equals("")){
                    confirmPassword.setError("Can't be a blank");
                }
                else if(pass.length()<8){
                    password.setError("At least 8 characters long");
                }
                else{

                    ChatClient.getInstance().register(email.getText().toString(), password.getText().toString(), name.getText().toString());
                    System.out.println("Register done");
                    startActivity(new Intent(register.this, login.class));

                }
            }

        });
    }

    private void hyperlink(){
        AlreadySignUp_hyperlink = findViewById(R.id.AlreadySignUp_hyperlink);

        AlreadySignUp_hyperlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
            }
        });
    }
}