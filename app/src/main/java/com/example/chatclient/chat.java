package com.example.chatclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.chatclient.messageutil.ChatClient;
import com.example.chatclient.stub.ChatMessage;
import com.example.chatclient.stub.ChatMessageFromServer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class chat extends Activity {
    TextView username, displayChat;
    Button sendComment;
    EditText text_send;
    ImageView ProfileImage;
    ArrayList<String> msgArr = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        sendMsg();
        image();
        FriendName();
        System.out.println("display chat2");

        ArrayList<String> MsgArr =  ChatClient.getInstance().getChatFrndsMap().get(intent.getStringExtra("Name"));

        if(MsgArr != null) {
            for (int i = 0; i < MsgArr.size(); i++) {

                this.DisplayChatMsgs(MsgArr.get(i));
            }
        }
        ChatClient.getInstance().addChat(intent.getStringExtra("Name"),this);
    }

    //send the message and show it in the text view
    private void sendMsg(){
        text_send = (EditText) findViewById(R.id.textSend);
        sendComment = (Button) findViewById(R.id.sendComment);
        displayChat =  findViewById(R.id.displayChat);

        Context currentContext = this;

        System.out.println("display chat1");
        displayChat.setMovementMethod(new ScrollingMovementMethod());

        //send button - on click
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String name = intent.getStringExtra("email");
                try {
                    ChatClient.getInstance().processMsg(name,text_send.getText().toString());
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }
                displayChat.setMovementMethod(new ScrollingMovementMethod());
                System.out.println("display chat1");

                displayToMsg(text_send.getText().toString());
            }
        });

    }

    //view sent messages
    public void displayToMsg(String msg){
        Context currentContext = this;
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_example);

                TextView textView1 = new TextView(currentContext);
                AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT,
                        AbsListView.LayoutParams.WRAP_CONTENT);
                textView1.setGravity( Gravity.RIGHT);

                textView1.setText(msg);
                textView1.setBackgroundColor(0xff66ff6); // hex color 0xAARRGGBB
                textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
                linearLayout.addView(textView1);

                System.out.println("display chat msg to");
                displayChat.setMovementMethod(new ScrollingMovementMethod());

            }
        });
    }

    //go to friend list
    private void image(){
        ProfileImage =(ImageView)findViewById(R.id.ProfileImage);

        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), friendList.class);
                startActivity(intent);
            }
        });
    }

    //view friend name in the top of the chat
    private  void FriendName(){
        username = findViewById(R.id.userName);
        Intent intent = getIntent();

        String name = intent.getStringExtra("Name");
        String email = intent.getStringExtra("email");
        username.setText(name);
    }

    //display messages
    public void DisplayChatMsgs(String msg){
        displayChat =  findViewById(R.id.displayChat);

        Intent intent = getIntent();
        System.out.println("display chat4");
        Context currentContext = this;
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_example);

                TextView textView1 = new TextView(currentContext);
                textView1.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT,
                        AbsListView.LayoutParams.WRAP_CONTENT));
                textView1.setText(msg);
                textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
                textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
                linearLayout.addView(textView1);

                System.out.println("display chat msg");
                displayChat.setMovementMethod(new ScrollingMovementMethod());

            }
        });
    }


}
