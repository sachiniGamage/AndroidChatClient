package com.example.chatclient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chatclient.chatstore.ChatStore;
import com.example.chatclient.messageutil.ChatClient;
import com.example.chatclient.messageutil.E2EE;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
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

                //generate key pair
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


                    try {
                        //Creating a Cipher object
                        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

                        //Initializing a Cipher object
                        byte[] decodedBytes = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
                        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedBytes);
                        KeyFactory kf = KeyFactory.getInstance("RSA");
                        PublicKey pb = kf.generatePublic(spec);
                        System.out.println(pb);



//                String decodedString = new String(decodedBytes);
////                RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(new BigInteger(decodedString, 10), new BigInteger(publicExponentStr, 10));
//                KeyFactory factory = KeyFactory.getInstance("RSA");
////                PublicKey pupKey = factory.generatePublic(publicSpec);


//                cipher.init(Cipher.ENCRYPT_MODE, encodedaddedEmailf1);

                        //Adding data to the cipher
                        byte[] input = "Welcome to Tutorialspoint".getBytes();
                        cipher.update(input);

                        //encrypting the data
                        byte[] cipherText = cipher.doFinal();
                        System.out.println(new String(cipherText, "UTF8"));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    }


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