package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chat.adapter.UserAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements LifecycleObserver {

    private RecyclerView mainRecycleView;
    ProgressDialog progressDialog;
    private ArrayList<UserModel> userArrayList;
    private UserAdapter adapter;
    private CircleImageView profileImageView;
    FirebaseAuth auth;
    private ImageButton button1;
    private DatabaseReference reference;
    private ValueEventListener eventListener;
    private String currentUserUID;
    private ImageView ivCall;
    private ImageView ivEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        mainRecycleView=findViewById(R.id.main_recycle_view);
        ivCall=findViewById(R.id.iv_call);
        ivEmail=findViewById(R.id.iv_email);
        button1 = (ImageButton) findViewById(R.id.btn_circle);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        profileImageView=findViewById(R.id.profile_image);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        userArrayList=new ArrayList<>();
        currentUserUID=FirebaseAuth.getInstance().getUid();
        reference=FirebaseDatabase.getInstance().getReference("users");
        mainRecycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new UserAdapter(userArrayList,MainActivity.this);
        mainRecycleView.setAdapter(adapter);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });




        ivCall.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, CallActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ivEmail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, EmailActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //fetchAllUsers();
        //getUserProfileImage();

        profileImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        fetchAllUser();

    }


    /*private void fetchAllUsers()
    {
        Query query = FirebaseDatabase.getInstance().getReference("users");
        query.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    mainRecycleView.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        if(!snapshot.getKey().equals(FirebaseAuth.getInstance().getUid()))
                        {
                            UserModel user=snapshot.getValue(UserModel.class);

                            user.setId(snapshot.getKey());
                            userArrayList.add(user);
                        }
                    }
                    adapter.notifyDataSetChanged();

                }
                else
                {
                    Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.show();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserProfileImage()
    {
        Query query=FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("imageUri");
        query.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String imageUrl = String.valueOf(dataSnapshot.getValue());
                Picasso
                        .with(getApplicationContext())
                        .load(imageUrl)
                        .error(R.drawable.ic_baseline_account_circle_24)
                        .into(profileImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }*/

    private void fetchAllUser()
    {
        eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    mainRecycleView.setVisibility(View.VISIBLE);
                    userArrayList.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        UserModel userModel= dataSnapshot.getValue(UserModel.class);
                        userModel.setId(dataSnapshot.getKey());
                        if(!userModel.getId().equals(currentUserUID))
                        {
                            userArrayList.add(userModel);
                        }
                        else
                        {
                            setProfileImageView(String.valueOf(dataSnapshot.child("imageUri").getValue()));
                        }
                        adapter.notifyDataSetChanged();

                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Users not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        reference.addValueEventListener(eventListener);
    }

    private void setProfileImageView(String imageUrl)
    {
        Picasso
                .get()
                .load(imageUrl)
                .error(R.drawable.ic_baseline_account_circle_24)
                .into(profileImageView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        Cont.activeUser();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        Cont.deActiveUser();
    }


}