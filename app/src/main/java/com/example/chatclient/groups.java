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

import java.util.ArrayList;
import java.util.UUID;

public class groups extends AppCompatActivity {

    ImageView addFriends;
    ImageView ProfileImage,btnAdd;
    private String m_Text ;
    private ListView usersList;
    protected ArrayList<String> arrayStrings = new ArrayList<String>();
    protected ArrayList<String> grpArrayStrings = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        addNameToGroup();
        image();
    }


    //button for add friends
    public void addNameToGroup(){
        addFriends = (ImageView)findViewById(R.id.addGrp);
        btnAdd = (ImageView)findViewById(R.id.btnAdd);
        usersList = findViewById(R.id.usersList);
        String randomString = UUID.randomUUID().toString();
        ChatStore.getGroupList();
        grpArrayStrings = ChatStore.getGroupList();

        ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(groups.this, android.R.layout.simple_list_item_1, grpArrayStrings);
            usersList = (ListView) findViewById(R.id.usersList);
            usersList.setAdapter(itemsAdapter);
        


//        Intent intent = getIntent();
//        String name = intent.getStringExtra("Name");
//        ChatClient.getInstance().getGroupList(ChatStore.getEmail());

//        if(arrayStrings != null) {
//
//        arrayStrings.add("a");
//            System.out.println("arr" + arrayStrings);
//
//            arrayStrings = ChatStore.getGroupList();
//
//


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(groups.this);
                builder.setTitle("Group Name");

                // Set up the input
                EditText input = new EditText(groups.this);
                // type of input
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        m_Text = input.getText().toString();
                        System.out.println(m_Text);
                        String groupName = m_Text;
//                        String groupName = ChatClient.getInstance().createGroup(m_Text,ChatStore.getEmail());
                        System.out.println("groupName: " + groupName);
//                        groupName = m_Text;

                        ChatStore.addGrpIdGrpNameToMap(randomString,groupName);
                        arrayStrings.add(groupName);

                        if(groupName.equals("")){
                            startActivity(new Intent(groups.this,groups.class));
                        }else{


//                            ChatStore.setGroupList(arrayStrings);
                            ArrayAdapter<String> itemsAdapter1 =
                                    new ArrayAdapter<String>(groups.this, android.R.layout.simple_list_item_1,arrayStrings);
                            usersList = (ListView) findViewById(R.id.usersList);
                            usersList.setAdapter(itemsAdapter1);

                            usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                  String name=  usersList.getSelectedItem().toString();
                                    String name= usersList.getItemAtPosition(position).toString();

                                    startActivity(new Intent(groups.this,groupChat.class).putExtra("Name",name).putExtra("email",ChatStore.getEmail()).putExtra("uuid",randomString));
                                }
                            });
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

                startActivity(new Intent(groups.this,groupChat.class).putExtra("Name",name).putExtra("email", ChatStore.getEmail()).putExtra("uuid",randomString));
            }
        });
    }
    //app logo
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

}
