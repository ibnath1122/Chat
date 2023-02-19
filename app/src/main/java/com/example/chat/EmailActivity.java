package com.example.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EmailActivity extends Activity {

    private ImageView ivBack;
    private EditText Email, Subject, Message;
    private Button Send;
    Button attachment;
    TextView tvAttachment;
    String email;
    String subject;
    String message;
    Uri URI = null;
    private static final int PICK_FROM_GALLERY = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_email);

        Email=findViewById(R.id.emailAddress);
        ivBack=findViewById(R.id.iv_back);
        Subject=findViewById(R.id.subject);
        Message=findViewById(R.id.message);
        attachment = findViewById(R.id.btAttachment);
        tvAttachment = findViewById(R.id.tvAttachment);
        Send=findViewById(R.id.sendBtn);

        ivBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(EmailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
        //attachment button listener
        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFolder();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            URI = data.getData();
            tvAttachment.setText(URI.getLastPathSegment());
            tvAttachment.setVisibility(View.VISIBLE);
        }
    }
    public void sendEmail() {
        try {
            email = Email.getText().toString();
            subject = Subject.getText().toString();
            message = Message.getText().toString();
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            if (URI != null) {
                emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
            }
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
            this.startActivity(Intent.createChooser(emailIntent, "Sending email..."));
        } catch (Throwable t) {
            Toast.makeText(this, "Request failed try again: "+ t.toString(), Toast.LENGTH_LONG).show();
        }
    }
    public void openFolder() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY);
    }
}