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
import com.example.chatclient.messageutil.E2EE;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class register extends AppCompatActivity {

    private TextInputEditText name,email, password, confirmPassword;
    private TextView AlreadySignUp_hyperlink;
    Button signup;
    private String user,pass,ConfirmPass;

    PublicKey publicKey;
    PrivateKey privateKey;
    KeyPairGenerator kpg;
    KeyPair kp;

    private final static String CRYPTO_METHOD = "RSA";
    private final static int CRYPTO_BITS = 2048;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signup();
        hyperlink();

    }

    public void generateKeyPair() {

        try{
        kpg = KeyPairGenerator.getInstance(CRYPTO_METHOD);
        kpg.initialize(CRYPTO_BITS);
        kp = kpg.genKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }

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

                if(publicKey == null){
                    generateKeyPair();
                }



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


                    ChatClient.getInstance().register(email.getText().toString(), password.getText().toString(), Base64.getEncoder().encodeToString(publicKey.getEncoded()), name.getText().toString());
                    System.out.println("Register done");
                    System.out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
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