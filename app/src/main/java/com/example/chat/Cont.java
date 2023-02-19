package com.example.chat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Cont {
    public static void activeUser()
    {
        String currentUserUid= FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance().getReference("users").child(currentUserUid).child("status").setValue(true);

    }

    public static void deActiveUser()
    {
        String currentUserUid= FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance().getReference("users").child(currentUserUid).child("status").setValue(false);

    }

    public static String getDate(long milliSeconds){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getTime(long milliSeconds){
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm aa");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}
