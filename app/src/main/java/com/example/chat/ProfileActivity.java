package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private TextView editProfile, userName, userBio, userGender, logout, delete;
    private ImageView ivBack;
    private LinearLayout mainLayout;
    private Button button1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);

        profileImage=findViewById(R.id.profile_image);
        //button1 = findViewById(R.id.btn1);
        editProfile=findViewById(R.id.edit_textView);
        userName=findViewById(R.id.text_user_name);
        userBio=findViewById(R.id.text_user_bio);
        userGender=findViewById(R.id.text_user_gender);
        //logout=findViewById(R.id.tv_logout);
        ivBack=findViewById(R.id.iv_back);
        mainLayout=findViewById(R.id.main_layout);
        //delete= findViewById(R.id.txt_delete);




        /*delete = (TextView) findViewById(R.id.txt_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, DeleteProfileActivity.class);
                startActivity(intent);

            }
        });*/



        ivBack.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view)
           {

               //onBackPressed();
               Intent intent=new Intent(ProfileActivity.this, MainActivity.class);
               startActivity(intent);

           }
       });



       /*logout.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view)
           {
               Cont.deActiveUser();
               FirebaseAuth.getInstance().signOut();
               Intent intent=new Intent();
               intent.setClass(ProfileActivity.this,SigninActivity.class);
               //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(intent);
               finishAffinity();

           }
       });*/

       editProfile.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view)
           {

               Intent intent=new Intent(ProfileActivity.this, UpdateActivity.class);
               startActivity(intent);

           }
       });
       fetchUserProfile();
    }

    public void fetchUserProfile()
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid());
        reference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                mainLayout.setVisibility(View.VISIBLE);
                UserModel user = dataSnapshot.getValue(UserModel.class);
                setUserData(user);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ProfileActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUserData(UserModel model)
    {
        Picasso
                .get()
                .load(model.getImageUri())
                .error(R.drawable.ic_baseline_account_circle_24)
                .into(profileImage);

        userName.setText(model.getName());
        userBio.setText(model.getBio());
        userGender.setText(model.getGender());

    }



}