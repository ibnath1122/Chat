package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigninActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private TextView mRecoverPassTv;
    private Button signInButton;
    private FirebaseAuth auth;
    TextView textView;
    CheckBox showpassword;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signin);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        mRecoverPassTv = findViewById(R.id.recoverPassTV);
        signInButton = findViewById(R.id.signInButton);
        auth = FirebaseAuth.getInstance();
        showpassword = findViewById(R.id.showpassword);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        showpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if(b)
                {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                /*if(email.isEmpty())
                {
                    emailEditText.setError("Enter Email");
                }
                else if(password.isEmpty())
                {
                    passwordEditText.setError("Enter password");
                }

                else if(!email.isEmpty() && !password.isEmpty())
                {
                    signInWithEmailPassword(email, password);
                }*/

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    progressDialog.show();
                    Toast.makeText(SigninActivity.this, "Enter valid data", Toast.LENGTH_SHORT).show();

                } else if (!email.matches(emailPattern)) {
                    progressDialog.show();
                    Toast.makeText(SigninActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    progressDialog.show();
                    Toast.makeText(SigninActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                if(auth.getCurrentUser().isEmailVerified())
                                {
                                    Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(SigninActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                                }
                                //progressDialog.show();
                                //startActivity(new Intent(SigninActivity.this, MainActivity.class));
                            } else {
                                progressDialog.show();
                                Toast.makeText(SigninActivity.this, "Error in login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        textView = (TextView) findViewById(R.id.txt_signup);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });

        mRecoverPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRoverPasswordDialog();
            }
        });

    }


    private void signInWithEmailPassword(String email, String password) {
        //progressDialog.show();
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //progressDialog.show();
                        //Toast.makeText(SigninActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(SigninActivity.this, "Error in login", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void showRoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover password");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText login_email = new EditText(this);
        login_email.setHint("Email");
        login_email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        login_email.setMinEms(10);

        linearLayout.addView(login_email);
        linearLayout.setPadding(10, 10, 10, 10);

        builder.setView(linearLayout);

        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = login_email.getText().toString().trim();
                beginRecovery(email);
            }
        });


        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    private void beginRecovery(String email) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SigninActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SigninActivity.this, "Failed....", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SigninActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}