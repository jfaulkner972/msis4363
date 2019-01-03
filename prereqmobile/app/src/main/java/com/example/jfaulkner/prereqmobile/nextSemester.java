package com.example.jfaulkner.prereqmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NextSemester extends AppCompatActivity {

    String userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_semester);
        userAccount = getIntent().getStringExtra("user");
    }
    public void buildScheduleGo(View v){
        Intent i = new Intent(this,BuildSchedule.class);
        i.putExtra("user",userAccount);
        startActivity(i);
    }

    public void displayScheduleGo1(View v){
        Intent i = new Intent(this,DisplaySchedule.class);
        i.putExtra("schNum","1");
        i.putExtra("user",userAccount);
        startActivity(i);
    }

    public void displayScheduleGo2(View v){
        Intent i = new Intent(this,DisplaySchedule.class);
        i.putExtra("schNum","2");
        i.putExtra("user",userAccount);
        startActivity(i);
    }

    public void displayScheduleGo3(View v){
        Intent i = new Intent(this,DisplaySchedule.class);
        i.putExtra("schNum","3");
        i.putExtra("user",userAccount);
        startActivity(i);
    }

    public void displayScheduleGo4(View v){
        Intent i = new Intent(this,DisplaySchedule.class);
        i.putExtra("schNum","4");
        i.putExtra("user",userAccount);
        startActivity(i);
    }
}
