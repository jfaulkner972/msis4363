package com.example.jfaulkner.prereqmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    String userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent l = getIntent();
        userAccount = l.getStringExtra("user");

    }

    public void degreeOptionGo(View v){

        Intent i = new Intent(this,DegreeProgress.class);
        i.putExtra("user",userAccount);
        startActivity(i);

    }

    public void checkCourseGo(View v){
        Intent i = new Intent(this,CheckCourse.class);
        startActivity(i);

    }

    public void nextSemesterGo(View v){
        Intent i = new Intent(this,NextSemester.class);
        i.putExtra("user",userAccount);
        startActivity(i);

    }
}
