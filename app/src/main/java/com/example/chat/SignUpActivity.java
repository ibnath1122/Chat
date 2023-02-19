package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, passwordReEditText;
    private Button signUpButton;
    private FirebaseAuth auth;
    ProgressDialog progressDialog;
    TextView textView;
    String emailPattern= "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        emailEditText=findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        passwordReEditText=findViewById(R.id.passwordReEditText);
        signUpButton=findViewById(R.id.signUpButton);
        auth= FirebaseAuth.getInstance();


        textView=(TextView) findViewById(R.id.txt_signin);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUpActivity.this, SigninActivity.class);
                startActivity(intent);

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                progressDialog.show();
                String email=emailEditText.getText().toString().trim();
                String password=passwordEditText.getText().toString().trim();
                String repassword=passwordReEditText.getText().toString().trim();

                /*if(email.isEmpty())
                {
                    emailEditText.setError("Enter Email");
                }
                else if(password.isEmpty())
                {
                    passwordEditText.setError("Enter password");
                }

                else if(repassword.isEmpty())
                {
                    passwordReEditText.setError("Confirm password");
                }

                else if(!email.isEmpty() && !password.isEmpty() && !repassword.isEmpty())
                {
                    if(password.equals(repassword))
                    {
                        signUpWithEmailPassword(email, password);

                    }
                    else
                    {
                        passwordReEditText.setError("Confirm password");
                    }
                }*/


                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repassword)) {
                    Toast.makeText(SignUpActivity.this, "Please Enter Valid data", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (!email.matches(emailPattern)) {
                    Toast.makeText(SignUpActivity.this, "Please Enter valid Email", Toast.LENGTH_SHORT);
                    progressDialog.dismiss();
                } else if (!password.equals(repassword)) {
                    Toast.makeText(SignUpActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (password.length() < 6) {
                    Toast.makeText(SignUpActivity.this, "Enter 6 character password", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

                else if(!email.isEmpty() && !password.isEmpty() && !repassword.isEmpty())
                {
                    if(password.equals(repassword))
                    {
                        signUpWithEmailPassword(email, password);

                    }
                    else
                    {
                        passwordReEditText.setError("Confirm password");
                    }
                }
            }
        });
    }

    private void signUpWithEmailPassword(String email, String password)
    {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //progressDialog.show();
                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    startActivity(new Intent(SignUpActivity.this, UserProfile.class));
                                    finish();
                                    Toast.makeText(SignUpActivity.this, "User register successfully, please verify your email", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        //startActivity(new Intent(SignUpActivity.this, UserProfile.class));
                        //finish();
                        //Toast.makeText(SignUpActivity.this, "Done create user account", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //progressDialog.show();
                        //Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(SignUpActivity.this, "Fail to create user account", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}