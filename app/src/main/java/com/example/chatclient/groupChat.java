package com.example.chatclient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chatclient.chatstore.ChatStore;
import com.example.chatclient.messageutil.ChatClient;

import java.util.ArrayList;
import java.util.UUID;

public class groupChat extends AppCompatActivity {

    ImageView addFriends;
    ImageView ProfileImage;
    String m_Text;
    TextView username;
    Button sendComment;
    EditText textSend;
    private ListView usersList;
    protected ArrayList<String> arrayStrings = new ArrayList<String>();

//    Intent intent = getIntent();

//    String name = intent.getStringExtra("Name");
//    String email = intent.getStringExtra("email");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        addFriendsToGroup();
        image();
        FriendName();
        grpSendMessage();
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

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
//                        String name = null;
//                        name = m_Text;

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

    //app logo
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
//        ChatClient.getInstance().processMsg(name);
    }

    public void grpSendMessage(){
        textSend = findViewById(R.id.textSend);
        sendComment = findViewById(R.id.sendComment);
        Intent intent = getIntent();

        String name = intent.getStringExtra("Name");
        String email = intent.getStringExtra("email");


        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatClient.getInstance().processGroupMsg(email,textSend.getText().toString(),name);
            }
        });
    }
}