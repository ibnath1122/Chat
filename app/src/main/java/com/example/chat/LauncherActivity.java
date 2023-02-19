package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class LauncherActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN=2500;

    ImageView imageView;
    TextView textView1, textView2;
    Animation top,buttom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_launcher);

        imageView=findViewById(R.id.imageView);
        textView1=findViewById(R.id.textView1);
        textView2=findViewById(R.id.textView2);

        top= AnimationUtils.loadAnimation(this, R.anim.top);
        buttom=AnimationUtils.loadAnimation(this, R.anim.buttom);

        imageView.setAnimation(top);
        textView1.setAnimation(buttom);
        textView2.setAnimation(buttom);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent=new Intent(LauncherActivity.this, FirstActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);


    }

    private void statusbarcolor()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_200,this.getTheme()));
        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setStatusBarColor(getResources().getColor(R.color.blue_200));
        }
    }

}
