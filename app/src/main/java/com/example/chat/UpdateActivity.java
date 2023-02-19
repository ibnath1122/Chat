package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateActivity extends AppCompatActivity {

    private CircleImageView setting_image;
    private EditText setting_name, setting_bio;
    private Button save;
    private FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    Uri setImageURI;
    String gender;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_update);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        setting_image=findViewById(R.id.setting_image);
        ivBack=findViewById(R.id.iv_back);
        setting_name=findViewById(R.id.setting_name);
        setting_bio=findViewById(R.id.setting_bio);
        save=findViewById(R.id.save);

        DatabaseReference reference=database.getReference("users").child(auth.getUid());
        StorageReference storageReference=storage.getReference().child("user-image").child(auth.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                gender= snapshot.child("gender").getValue().toString();
                String name= snapshot.child("name").getValue().toString();
                String image= snapshot.child("imageUri").getValue().toString();
                String bio= snapshot.child("bio").getValue().toString();

                setting_name.setText(name);
                setting_bio.setText(bio);
                //Picasso.get().load(image).into(setting_image);

                Picasso
                        .with(getApplicationContext())
                        .load(image)
                        .error(R.drawable.ic_baseline_account_circle_24)
                        .into(setting_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                //onBackPressed();
                Intent intent=new Intent(UpdateActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        setting_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),10);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=setting_name.getText().toString();
                String bio=setting_bio.getText().toString();

                if(setImageURI!=null)
                {

                    storageReference.putFile(setImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String finalImageuri=uri.toString();
                                    UserModel users=new UserModel(auth.getUid(), name,bio, finalImageuri,gender);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(UpdateActivity.this, "Data Successfully Updated", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(UpdateActivity.this, ProfileActivity.class));
                                            }

                                            else
                                            {
                                                Toast.makeText(UpdateActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else
                {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String finalImageuri=uri.toString();
                            UserModel users=new UserModel(auth.getUid(), name,bio, finalImageuri,gender);
                            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(UpdateActivity.this, "Data Successfully Updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(UpdateActivity.this, ProfileActivity.class));
                                    }

                                    else
                                    {
                                        Toast.makeText(UpdateActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10)
        {
            if(data!=null)
            {
                setImageURI=data.getData();
                setting_image.setImageURI(setImageURI);

            }
        }
    }



}