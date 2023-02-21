package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class FrontActivity extends AppCompatActivity {

    private Button button1;
    private Button button2;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();*/

        getSupportActionBar().hide();
        setContentView(R.layout.activity_front);

        button1 = findViewById(R.id.btn1);
        button2= findViewById(R.id.btn2);



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(FrontActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });

        textView = (TextView) findViewById(R.id.read);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FrontActivity.this, ReadActivity.class);
                startActivity(intent);

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(FrontActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });
    }

}