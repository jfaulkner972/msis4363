package com.example.jfaulkner.prereqmobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class BuildSchedule extends AppCompatActivity {

    public Connection con;
    public static String user= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_schedule);

        user = getIntent().getStringExtra("user");

        CheckLogin checkLogin = new CheckLogin();
        checkLogin.execute("");

    }
    public class CheckLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";
        String name2 = "";

        @Override
        protected void onPostExecute(String r)
        {

        }
        @Override
        protected String doInBackground(String... params)
        {

            try
            {
                DatabaseConnection connection = new DatabaseConnection();
                con = connection.connectionclass();        // Connect to database
                if (con == null)
                {
                    z = "Check Your Internet Access!";
                }
                else
                {

                    int counter = 1;
                    String query = "select * from courses;";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next())
                    {
                        name1= rs.getString("courseName"); //Name is the string label of a column in database, read through the select query
                        name2= rs.getString("courseNumber");
                        String course = name1 + " " + name2;
                        Button b = findViewById(getResources().getIdentifier("course"+counter,"id",getPackageName()));
                        b.setText(course);
                        z = "success";
                        isSuccess=true;
                        counter++;
                    }
                    con.close();
                }
            }
            catch (Exception ex)
            {
                isSuccess = false;
                z = ex.getMessage();

                Log.d ("sql error", z);
            }

            return z;
        }
    }


    public void displayScheduleGo(View v) {
        int counter = 0;
        String nextClass = "";
        for (int i = 1; i < 12; i++) {
            CheckBox checkBox = findViewById(getResources().getIdentifier("course" + i, "id", getPackageName()));
            if (checkBox.isChecked() == true) {
                counter++;
                nextClass += checkBox.getText().toString() + ";";
            }
        }
        if (counter > 6) {
            String message ="You can only put six classes on the schedule";
            Toast.makeText(BuildSchedule.this, message, Toast.LENGTH_LONG).show();
        }
        else {
            Intent i = new Intent(this, DisplaySchedule.class);
            i.putExtra("activateSave", "yes");
            i.putExtra("user",user);
            i.putExtra("nextClass",nextClass);
            startActivity(i);
        }
    }
}
