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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
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

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    private CircleImageView profile_image;
    private TextView changeProfileText;
    private EditText editName, editBio;
    private RadioGroup radioGroups;
    private RadioButton radioMale, radioFemale;
    private Button buttonSave;
    private final int GET_IMAGE_CODE=10;
    private Uri imageUri;
    private FirebaseAuth auth;
    private boolean isSelectImage =false;

    private String gender="";
    FirebaseStorage storage;
    StorageReference storageReference;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_profile);

        profile_image=findViewById(R.id.profile_image);
        changeProfileText=findViewById(R.id.changeProfileText);
        editName=findViewById(R.id.editName);
        editBio=findViewById(R.id.editBio);
        radioGroups=findViewById(R.id.radioGroups);
        radioMale=findViewById(R.id.radioMale);
        radioFemale=findViewById(R.id.radioFemale);
        buttonSave=findViewById(R.id.buttonSave);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        auth= FirebaseAuth.getInstance();
        DatabaseReference databaseReference;

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);


        changeProfileText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPer();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener()
        {


            @Override
            public void onClick(View view)
            {
                if(check())
                {
                    if(isSelectImage)
                    {

                        uploadImage();
                    }
                    else
                    {

                    }
                    progressDialog.setMessage("Uploading...");
                    progressDialog.show();
                }
            }
        });

    }

    private void checkPer()
    {
        Dexter
                .withContext(getApplicationContext())
                .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)  ///this line has error
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, GET_IMAGE_CODE);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        Toast.makeText(UserProfile.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GET_IMAGE_CODE && resultCode==RESULT_OK)
        {
            if(data!=null)
            {
                imageUri=data.getData();
                Picasso
                        .with(getApplicationContext())
                        .load(imageUri)
                .into(profile_image);

                isSelectImage=true;

            }
        }
    }

    private boolean check()
    {
        String name=editName.getText().toString().trim();
        String bio=editBio.getText().toString().trim();
        int radioCheck=radioGroups.getCheckedRadioButtonId();
        switch(radioCheck)
        {
            case R.id.radioMale:gender="Male";
            break;

            case R.id.radioFemale:gender="Female";
            break;

            default:
                gender="Not define";
                break;
        }

        if(name.isEmpty())
        {
            editName.setError("Enter name");
            return false;
        }
        else if (bio.isEmpty())
        {
            editBio.setError("Enter bio");
            return false;
        }
        else if(gender.isEmpty())
        {
            Toast.makeText(UserProfile.this, "Select Your Gender", Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;

    }

    private void uploadImage()
    {
        StorageReference storageReference=FirebaseStorage.getInstance().getReference("user-image")
                .child(FirebaseAuth.getInstance().getUid() + "IMAGE.jpg");
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                uploadUserData(String.valueOf(uri));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                uploadUserData("No");
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        uploadUserData("No");
                        Toast.makeText(UserProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


    }

    private void uploadUserData(String uploadedUri)
    {

        UserModel userModel=new UserModel();
        userModel.setName(editName.getText().toString().trim());
        userModel.setBio(editBio.getText().toString().trim());
        userModel.setGender(gender);
        userModel.setImageUri(uploadedUri);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(FirebaseAuth.getInstance().getUid())
                .setValue(userModel)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void unused)
                    {
                        progressDialog.dismiss();
                        Intent intent=new Intent();
                        intent.setClass(UserProfile.this,SigninActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }) .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();
                        Toast.makeText(UserProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


}