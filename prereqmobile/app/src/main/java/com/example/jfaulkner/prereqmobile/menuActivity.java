package com.example.jfaulkner.prereqmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class menuActivity extends AppCompatActivity {

    String userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent l = getIntent();
        userAccount = l.getStringExtra("user");

    }

    public void degreeOptionGo(View v){

        Intent i = new Intent(this,degreeprogress.class);
        i.putExtra("user",userAccount);
        startActivity(i);

    }

    public void checkCourseGo(View v){
        Intent i = new Intent(this,checkCourse.class);
        startActivity(i);

    }

    public void nextSemesterGo(View v){
        Intent i = new Intent(this,nextSemester.class);
        i.putExtra("user",userAccount);
        startActivity(i);

    }
}
