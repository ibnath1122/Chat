package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CallActivity extends AppCompatActivity {

    private EditText phoneNo;
    FloatingActionButton callbtn;
    static int PERMISSION_CODE=100;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_call);

        phoneNo=findViewById(R.id.phnEditText);
        ivBack=findViewById(R.id.iv_back);
        callbtn=findViewById(R.id.callbtn);

         ivBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(CallActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        if(ContextCompat.checkSelfPermission(CallActivity.this, android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED);
        {
            ActivityCompat.requestPermissions(CallActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
        }

        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneno=phoneNo.getText().toString();
                Intent i=new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:"+phoneno));
                startActivity(i);
            }
        });


    }
}