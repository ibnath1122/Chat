package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
//import android.support.v7.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class MenuActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    private Button sendButton;

    ActionBarDrawerToggle mDrawerToggle;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav);
        sendButton = findViewById(R.id.sendBtn);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent=new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);*/
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        // mDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout.toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.messages:
                        /*drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(MainActivity.this,"Messages", Toast.LENGTH_SHORT).show();
                        fragmentR(new MessagesFragment());
                        Intent intent = new Intent(MenuActivity.this, SignUpActivity.class);
                        startActivity(intent);
                        break;*/
                        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                        startActivity(intent);

                    case R.id.call:
                        /*fragmentR(new ExploreFragment());
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(MainActivity.this,"Explore", Toast.LENGTH_SHORT).show();
                        break;*/
                        Intent intent1 = new Intent(MenuActivity.this, CallActivity.class);
                        startActivity(intent1);

                    case R.id.aboutus:
                        /*fragmentR(new CommentFragment());
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(MainActivity.this,"Comment", Toast.LENGTH_SHORT).show();
                        break;*/

                        Intent intent2 = new Intent(MenuActivity.this, AboutusActivity.class);
                        startActivity(intent2);
                }

                return true;
            }
        });




    }

}