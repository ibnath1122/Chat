package com.example.chat;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.adapter.ChatAdapter;
import com.example.chat.adapter.UserAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private Intent getIntent;
    private CircleImageView profileImage;
    private TextView textViewUserName;
    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private ImageView ivBack, ivSendMessage;
    private String senderUID;
    private String receiverUID;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    private ArrayList<MessageModel> messageList;
    private ChatAdapter chatAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_chat);

        profileImage=findViewById(R.id.user_image_view);
        textViewUserName=findViewById(R.id.tv_user_name);
        chatRecyclerView=findViewById(R.id.chat_recyclerview);
        messageEditText=findViewById(R.id.edit_text_message);
        ivBack=findViewById(R.id.iv_back);
        ivSendMessage=findViewById(R.id.iv_send_message);


        getIntent=getIntent();
        userInfo();

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageList=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("messages");
        senderUID= FirebaseAuth.getInstance().getUid();

        chatAdapter=new ChatAdapter(messageList,this);
        chatRecyclerView.setAdapter(chatAdapter);

        ivBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                onBackPressed();
            }
        });

        ivSendMessage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String message=messageEditText.getText().toString().trim();
                if(!message.isEmpty())
                {
                    sendMessage(message);
                    messageEditText.setText("");
                }
                else
                {
                    Toast.makeText(ChatActivity.this, "Can't send empty message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fetchMessage();

    }

    private void userInfo()
    {
        Picasso
                .with(getApplicationContext())
                .load(getIntent.getStringExtra(UserAdapter.SENDER_IMAGE_URL_KEY))
                .error(R.drawable.ic_baseline_account_circle_24)
                .into(profileImage);

        textViewUserName.setText(getIntent.getStringExtra(UserAdapter.SENDER_NAME_KEY));
        receiverUID=getIntent.getStringExtra(UserAdapter.SENDER_UID_KEY);
    }

    private void sendMessage(String message)
    {
        String messageId=databaseReference.child(senderUID).child(receiverUID).push().getKey();
        MessageModel messageModel=new MessageModel();
        messageModel.setMessage(message);
        messageModel.setFrom(senderUID);
        messageModel.setMsTime(System.currentTimeMillis());
        databaseReference.child(senderUID).child(receiverUID).child(messageId)
                .setValue(messageModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        databaseReference.child(receiverUID).child(senderUID).child(messageId)
                                .setValue(messageModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ChatActivity.this,"message sent", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void fetchMessage()
    {
        eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    messageList.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        MessageModel message=dataSnapshot.getValue(MessageModel.class);
                        messageList.add(message);
                    }
                    chatAdapter.notifyDataSetChanged();
                    chatRecyclerView.scrollToPosition(messageList.size()-1);
                }
                chatRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ChatActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        databaseReference.child(senderUID).child(receiverUID).addValueEventListener(eventListener);
    }



}