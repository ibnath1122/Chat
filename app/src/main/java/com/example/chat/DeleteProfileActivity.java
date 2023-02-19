package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeleteProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private EditText editTextUserPwd;
    private TextView textViewAuthenticated;
    private String userPwd;
    private ImageView ivBack;
    private Button buttonReAuthenticate, buttonDeleteUser;
    private static final String TAG="DeleteProfileActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_delete_profile);

        editTextUserPwd=findViewById(R.id.editText_delete_user_pwd);
        ivBack=findViewById(R.id.iv_back);
        textViewAuthenticated=findViewById(R.id.textView_delete_user_authenticated);
        buttonDeleteUser=findViewById(R.id.button_delete_user);
        buttonReAuthenticate=findViewById(R.id.button_delete_user_authenticated);

        buttonDeleteUser.setEnabled(false);

        auth=FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();


        ivBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(DeleteProfileActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        if(firebaseUser.equals(""))
        {
            Toast.makeText(DeleteProfileActivity.this, "Something went wrong" + "User Details are not available at the moment", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DeleteProfileActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();

        }
        else
        {
            reAuthenticateUser(firebaseUser);
        }
    }

    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        buttonReAuthenticate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String password = editTextUserPwd.getText().toString();

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(DeleteProfileActivity.this, "Password is needed", Toast.LENGTH_SHORT).show();
                    editTextUserPwd.setError("Please enter your current password to authenticate");
                    editTextUserPwd.requestFocus();
                } else {
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), password);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                editTextUserPwd.setEnabled(false);

                                buttonReAuthenticate.setEnabled(false);
                                buttonDeleteUser.setEnabled(true);
                                textViewAuthenticated.setText("You are authenticated/verified");
                                Toast.makeText(DeleteProfileActivity.this, "Password has been verified" + "You can delete your profile now", Toast.LENGTH_SHORT).show();
                                buttonDeleteUser.setBackgroundTintList(ContextCompat.getColorStateList(
                                        DeleteProfileActivity.this, R.color.blue_500));

                                buttonDeleteUser.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showAlertDialog();
                                    }
                                });

                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(DeleteProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void showAlertDialog() {

        AlertDialog.Builder builder= new AlertDialog.Builder(DeleteProfileActivity.this);
        builder.setTitle("Delete user & data");
        builder.setMessage("Do you want to delete your profile and related info?");
        
        
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUser(firebaseUser);
            }
        });
        
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent= new Intent(DeleteProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.blue_500));
            }
        });
        
        alertDialog.show();
    }

    private void deleteUser(FirebaseUser firebaseUser) {
        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    //deleteUserData();
                    auth.signOut();
                    Toast.makeText(DeleteProfileActivity.this, "User has been deleted!", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(DeleteProfileActivity.this, FrontActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    try{
                        throw task.getException();
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(DeleteProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}