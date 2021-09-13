package com.example.chatclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView NewUser,fgtPsw;
    TextView newUser_hyperlink;
    Button signin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signin();
        registerLink();
        forgotPassword();

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
//                    Toast.makeText(login.this,"Sign In Successful !!!", Toast.LENGTH_SHORT).show();




                    if(isAuthenticated == true){
//                        startActivity(new Intent(login.this, chat.class).putExtra("LoginEmail", email.getText()));
                        startActivity(new Intent(login.this, friendList.class));
                    }
                    else{
                        System.out.println("Login failed");
                        Toast.makeText(login.this,"Sign In Failed. Wrong Email or Password !!!", Toast.LENGTH_SHORT).show();
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

    private void forgotPassword(){
        fgtPsw = findViewById(R.id.fgtPsw);

        fgtPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);


                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract the email and send reset link

                        String mail = resetMail.getText().toString();
//                        fauth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(login.this, "Reset Link Sent To Your Email.",Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(login.this, "Error! Reset Link Not Sent." +e.getMessage(),Toast.LENGTH_SHORT).show();
//                            }
//                        });

//                    }
//                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialog
                    }
                });

                passwordResetDialog.create().show();
            }
        });
            }
        });
    }

}