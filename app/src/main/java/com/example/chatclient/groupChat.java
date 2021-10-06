package com.example.chatclient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chatclient.chatstore.ChatStore;
import com.example.chatclient.chatstore.GroupIDObject;
import com.example.chatclient.chatstore.GroupMsgObj;
import com.example.chatclient.messageutil.ChatClient;
import com.example.chatclient.stub.GroupMessage;

import java.util.ArrayList;
import java.util.UUID;

public class groupChat extends AppCompatActivity {

    ImageView addFriends;
    ImageView ProfileImage;
    String m_Text;
    TextView username,displayChat;
    Button sendComment;
    EditText textSend;
    private ListView usersList;
    protected ArrayList<String> arrayStrings = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        Intent intent = getIntent();
        addFriendsToGroup();
        image();
        FriendName();
        grpSendMessage();

        ArrayList<GroupMsgObj> grpMsgArr = ChatClient.getInstance().getGrpIdMsgsMap().get(intent.getStringExtra("uuid"));

        if(grpMsgArr != null) {
            for (int i = 0; i < grpMsgArr.size(); i++) {
                GroupMsgObj msgobj = grpMsgArr.get(i);
                System.out.println("email " +intent.getStringExtra("email"));
                if(msgobj.getFriendemail().equals(intent.getStringExtra("email"))){
                    this.displayToMsg(grpMsgArr.get(i).getMsg());
                }else{
                    this.displayGrpMsg(grpMsgArr.get(i).getMsg());
                }
            }
        }

        ChatClient.getInstance().addGrpChat(intent.getStringExtra("uuid"),this);
    }

    //button for add friends
    public void addFriendsToGroup(){
        addFriends = (ImageView)findViewById(R.id.addGrp);
        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        String email = intent.getStringExtra("email");
        String grpID = intent.getStringExtra("uuid");

        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(groupChat.this);
                builder.setTitle("Friend's Email Address");
                // Set up the input
                EditText input = new EditText(groupChat.this);
                // type of input
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(input);
                //add button
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        System.out.println(m_Text);

                        String groupName = ChatClient.getInstance().createGroup(grpID,name,email,m_Text);

                        if(name.equals(null)){
                            System.out.println("Friend is not available in frindlistClass");
                            startActivity(new Intent(groupChat.this,groupChat.class));
                        }else{
                            arrayStrings.add(name);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    //app logo - go to groups
    private void image(){
        ProfileImage =(ImageView)findViewById(R.id.ProfileImage);

        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), groups.class);
                startActivity(intent);
            }
        });
    }

    private  void FriendName(){
        username = findViewById(R.id.userName);
        Intent intent = getIntent();

        String name = intent.getStringExtra("Name");
        String email = intent.getStringExtra("email");
        username.setText(name);
    }

    //send messages - group chat
    public void grpSendMessage(){
        textSend = findViewById(R.id.textSend);
        sendComment = findViewById(R.id.sendComment);
        Intent intent = getIntent();

        String name = intent.getStringExtra("Name");
        String email = intent.getStringExtra("email");
        String uuid = intent.getStringExtra("uuid");
        //send button
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatClient.getInstance().processGroupMsg(ChatStore.getEmail(),uuid,textSend.getText().toString());
                displayToMsg(textSend.getText().toString());
            }
        });
    }


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
            }
        });
    }

    public void displayGrpMsg(String msg){
        displayChat = findViewById(R.id.displayChat);
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
            }
        });
    }
}