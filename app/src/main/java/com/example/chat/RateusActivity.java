package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;

public class RateusActivity extends AppCompatActivity {
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_rateus);

        ivBack=findViewById(R.id.iv_back);
        RateUsDialog rateUsDialog = new RateUsDialog(RateusActivity.this);
        rateUsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        rateUsDialog.setCancelable(false);
        rateUsDialog.show();

        ivBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });
    }
}
