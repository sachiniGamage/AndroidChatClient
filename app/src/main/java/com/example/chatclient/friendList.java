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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.chatclient.chatstore.ChatStore;
import com.example.chatclient.messageutil.ChatClient;
import com.example.chatclient.stub.FriendList;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class friendList extends AppCompatActivity {

    ImageView ProfileImage,add_btn,grpbtn;
    private String m_Text ;
    private ListView usersList;
    protected ArrayList<String> arrayStrings = new ArrayList<String>();
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        image();
        AddNewFriend();

    }

    private void AddNewFriend(){
        add_btn = (ImageView)findViewById(R.id.add);
        usersList = findViewById(R.id.usersList);


        arrayStrings = ChatStore.getFriendList();

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(friendList.this, android.R.layout.simple_list_item_1,arrayStrings);
        usersList = (ListView) findViewById(R.id.usersList);
        usersList.setAdapter(itemsAdapter);





        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(friendList.this);
                builder.setTitle("Friend's Email Address");

                // Set up the input
                EditText input = new EditText(friendList.this);
                // type of input
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String randomString = UUID.randomUUID().toString();
                        m_Text = input.getText().toString();
                        String name = null;
                        try {
                            name = ChatClient.getInstance().updateFriendList(m_Text);
                            ChatStore.addFriendNameEmailToMap(name,m_Text);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }

                        if(name.equals(null)){
                            System.out.println("Friend is not available in frindlistClass");
                            startActivity(new Intent(friendList.this,friendList.class));
                        }else{
                            System.out.println("frind is available in frendlist class");
//                            arrayStrings.add(m_Text);
//                            arrayStrings = ChatStore.getFriendList();

                            arrayStrings.add(name);
//
                            ArrayAdapter<String> itemsAdapter1 =
                                    new ArrayAdapter<String>(friendList.this, android.R.layout.simple_list_item_1,arrayStrings);
                            usersList = (ListView) findViewById(R.id.usersList);
                            usersList.setAdapter(itemsAdapter1);

                            usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                  String name=  usersList.getSelectedItem().toString();
                                    String name= usersList.getItemAtPosition(position).toString();

                                    startActivity(new Intent(friendList.this,chat.class).putExtra("Name",name).putExtra("email",ChatStore.getFriendEmailFromNameToMap(name)));
                                }
                            });

//                            usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                    Intent intent = new Intent(getApplicationContext(), chat.class);
//////                                Bundle bundle = new Bundle();
//////                                bundle.putString("email", m_Text);
//////                                intent.putExtra("email", bundle);
//                                    startActivity(intent);
////                                    startActivity(new Intent(friendList.this,chat.class).putExtra("Name",name));
//                                }
//                            });
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

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//               String name=  usersList.getSelectedItem().toString();
                String name= usersList.getItemAtPosition(position).toString();

                startActivity(new Intent(friendList.this,chat.class).putExtra("Name",name).putExtra("email", ChatStore.getFriendEmailFromNameToMap(name)));
            }
        });
    }


    private void image(){
        ProfileImage =(ImageView)findViewById(R.id.ProfileImage);
        grpbtn = (ImageView)findViewById(R.id.grpbtn);
        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), profile.class);
                startActivity(intent);
            }
        });

        grpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), groups.class);
                startActivity(intent);
            }
        });
    }
}