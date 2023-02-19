package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity{

    //public CardView card1, card2, card3;
    GridLayout mainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dashboard);

        /*card1=(CardView) findViewById(R.id.c1);
        card3=(CardView) findViewById(R.id.c2);
        card3=(CardView) findViewById(R.id.c3);

        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);*/

        mainGrid=(GridLayout) findViewById(R.id.mainGrid);

        setSingleEvent(mainGrid);


    }

    private void setSingleEvent(GridLayout mainGrid) {

        for(int i=0; i<mainGrid.getChildCount(); i++)
        {
            CardView cardView=(CardView) mainGrid.getChildAt(i);
            final int finalI=i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(finalI==0)
                    {
                        Intent intent= new Intent(DashboardActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    else if(finalI==1)
                    {
                        Intent intent= new Intent(DashboardActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }

                    else if(finalI==2)
                    {
                        Cont.deActiveUser();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent=new Intent();
                        intent.setClass(DashboardActivity.this,SigninActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finishAffinity();
                    }

                    else if(finalI==3)
                    {
                        Intent intent= new Intent(DashboardActivity.this, DeleteProfileActivity.class);
                        startActivity(intent);
                    }

                    else if(finalI==4)
                    {
                        Intent intent= new Intent(DashboardActivity.this, RateusActivity.class);
                        startActivity(intent);
                    }

                    else if(finalI==5)
                    {
                        Intent intent= new Intent(DashboardActivity.this, AboutusActivity.class);
                        startActivity(intent);
                    }

                    else if(finalI==6)
                    {
                        Intent intent= new Intent(DashboardActivity.this, CallActivity.class);
                        startActivity(intent);
                    }

                    else if(finalI==7)
                    {
                        Intent intent= new Intent(DashboardActivity.this, FrontActivity.class);
                        startActivity(intent);
                    }

                    else
                    {
                        Toast.makeText(DashboardActivity.this, "No item", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    /*@Override
    public void onClick(View v) {

        Intent i;

        switch(v.getId()){
            case R.id.c1:
                i=new Intent(this, MainActivity.class);
                startActivity(i);
                break;

        }



    }*/
}