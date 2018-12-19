package com.example.jfaulkner.prereqmobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class courseResults extends AppCompatActivity {

    public Connection con;
    private String courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_results);

        Intent result = getIntent();
        String course = result.getStringExtra("name");
        courseName = course;
        TextView courseTxtView = findViewById(R.id.courseResult);
        courseTxtView.setText(course);

        CheckLogin checkLogin = new CheckLogin();
        checkLogin.execute("");

    }

    public class CheckLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        @Override
        protected void onPostExecute(String r) {
            int number = r.split(";").length;
            int setInvisble = 0;
            for (int i = 0; i < number; i++) {
                TextView t = findViewById(getResources().getIdentifier("prereqTxt" + (i + 1), "id", getPackageName()));
                t.setText(r.split(";")[i]);
                setInvisble = i;
            }
            setInvisble += 2;
            while (setInvisble < 4) {
                TextView t = findViewById(getResources().getIdentifier("prereqTxt" + (setInvisble), "id", getPackageName()));
                t.setVisibility(View.INVISIBLE);

                TextView u = findViewById(getResources().getIdentifier("prereq" + (setInvisble), "id", getPackageName()));
                u.setVisibility(View.INVISIBLE);
                setInvisble++;
            }
            TextView bF = findViewById(R.id.prereqTxt1);
            String temp = bF.getText().toString();
            if(temp == ""){
                findViewById(R.id.prereq1).setVisibility(View.INVISIBLE);
            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                con = connectionclass();        // Connect to database
                if (con == null) {
                    z = "Check Your Internet Access!";
                } else {
                    int courseNumber = Integer.parseInt(courseName.split(" ")[1]);
                    String query = "select p.preqOne from courses c join prereq p on c.courseNumber = p.courseNumber where c.courseNumber = " + courseNumber;
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        name1 = rs.getString("preqOne"); //Name is the string label of a column in database, read through the select query
                        z += name1 + ";";
                        isSuccess = true;
                    }
                    con.close();
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage();

                Log.d("sql error", z);
            }

            return z;
        }
    }


    @SuppressLint("NewApi")
    public Connection connectionclass() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //your database connection string goes below
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {
            Log.e("error here 1 : ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("error here 2 : ", e.getMessage());
        } catch (Exception e) {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }


}
