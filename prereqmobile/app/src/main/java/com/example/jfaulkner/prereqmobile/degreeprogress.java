package com.example.jfaulkner.prereqmobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DegreeProgress extends AppCompatActivity {

    public Connection con;
    public String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_degreeprogress);

        Intent i = getIntent();
        user = i.getStringExtra("user");

        loadClasses loadClasses = new loadClasses();
        loadClasses.execute("");
        freezeChanges();
    }

    public void updateProgress(View v) {
        Button update = findViewById(R.id.updateProgress);
        Button save = findViewById(R.id.saveProgress);
        update.setVisibility(View.GONE);
        save.setVisibility(View.VISIBLE);
        allowChanges();
    }

    public void saveProgress(View v) {
        Button update = findViewById(R.id.updateProgress);
        Button save = findViewById(R.id.saveProgress);
        update.setVisibility(View.VISIBLE);
        save.setVisibility(View.GONE);
        insertProgress insertProgress = new insertProgress();
        insertProgress.execute("");
        freezeChanges();
    }

    public void freezeChanges() {
        for (int i = 1; i < 13; i++) {
            CheckBox pass = findViewById(getResources().getIdentifier("pass" + i, "id", getPackageName()));
            CheckBox fail = findViewById(getResources().getIdentifier("fail" + i, "id", getPackageName()));
            CheckBox in = findViewById(getResources().getIdentifier("inProgress" + i, "id", getPackageName()));
            pass.setClickable(false);
            fail.setClickable(false);
            in.setClickable(false);
        }
    }

    public void allowChanges() {
        for (int i = 1; i < 13; i++) {
            CheckBox pass = findViewById(getResources().getIdentifier("pass" + i, "id", getPackageName()));
            CheckBox fail = findViewById(getResources().getIdentifier("fail" + i, "id", getPackageName()));
            CheckBox in = findViewById(getResources().getIdentifier("inProgress" + i, "id", getPackageName()));
            pass.setClickable(true);
            fail.setClickable(true);
            in.setClickable(true);

        }
    }

    public class loadClasses extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";
        String name2 = "";

        @Override
        protected void onPostExecute(String r) {
            //Toast.makeText(DegreeProgress.this, r, Toast.LENGTH_LONG).show();
            if (r.split(":").length == 1) {
                String course = r.split(":")[0];
                int courseNumber = course.split(";").length;
                TextView t;
                for (int i = 0; i < courseNumber; i++) {
                    t = findViewById(getResources().getIdentifier("c" + (i + 1), "id", getPackageName()));
                    t.setText(r.split(";")[i]);
                }
            } else {
                String course = r.split(":")[0];
                String status = r.split(":")[1];
                int courseNumber = course.split(";").length;
                int statusNumber = status.split(";").length;
                //Toast.makeText(DegreeProgress.this, status.split(";")[0], Toast.LENGTH_LONG).show();
                TextView t;
                CheckBox v;
                for (int i = 0; i < courseNumber; i++) {
                    t = findViewById(getResources().getIdentifier("c" + (i + 1), "id", getPackageName()));
                    t.setText(r.split(";")[i]);
                }
                for (int i = 0; i < statusNumber; i++) {
                    if (status.split(";")[i].matches("0")) {
                        v = findViewById(getResources().getIdentifier("pass" + (i + 1), "id", getPackageName()));
                        v.setChecked(true);
                    } else if (status.split(";")[i].matches("1")) {
                        v = findViewById(getResources().getIdentifier("fail" + (i + 1), "id", getPackageName()));
                        v.setChecked(true);
                    } else {
                        v = findViewById(getResources().getIdentifier("inProgress" + (i + 1), "id", getPackageName()));
                        v.setChecked(true);
                    }
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                DatabaseConnection connection = new DatabaseConnection();
                con = connection.connectionclass();        // Connect to database
                if (con == null) {
                    z = "Check Your Internet Access:";
                } else {

                    String query = "select * from courses;";
                    String course = "";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        name1 = rs.getString("courseName"); //Name is the string label of a column in database, read through the select query
                        name2 = rs.getString("courseNumber");
                        course += name1 + " " + name2 + ";";
                    }

                    isSuccess = true;

                    String getStatus = "select degreeStatus from degreeAF where dafUID = '" + user + "';";
                    String courseStatus = "";
                    Statement stmt2 = con.createStatement();
                    ResultSet rs2 = stmt2.executeQuery(getStatus);
                    while (rs2.next()) {
                        courseStatus += rs2.getString("degreeStatus") + ";";
                    }
                    z = course + ":" + courseStatus;

                    con.close();

                }
            } catch (
                    Exception ex)

            {
                isSuccess = false;
                z = ex.getMessage();

                Log.d("sql error", z);
            }

            return z;
        }
    }

    public class insertProgress extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(DegreeProgress.this, r, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                DatabaseConnection connection = new DatabaseConnection();
                con = connection.connectionclass();        // Connect to database
                if (con == null) {
                    z = "Check Your Internet Access!";
                } else {
                    String progress = "";
                    for (int i = 0; i < 12; i++) {
                        CheckBox passProgress = findViewById(getResources().getIdentifier("pass" + (i + 1), "id", getPackageName()));
                        CheckBox failProgress = findViewById(getResources().getIdentifier("fail" + (i + 1), "id", getPackageName()));
                        CheckBox inProgress = findViewById(getResources().getIdentifier("inProgress" + (i + 1), "id", getPackageName()));

                        if ((passProgress.isChecked() && failProgress.isChecked()) || (passProgress.isChecked() && inProgress.isChecked()) || (failProgress.isChecked() && inProgress.isChecked())) {
                            z = "Conflicting progress information. Please fix";
                            return z;
                        } else if (passProgress.isChecked() || failProgress.isChecked() || inProgress.isChecked()) {
                            if (passProgress.isChecked()) {
                                progress += "0;";
                            } else if (failProgress.isChecked()) {
                                progress += "1;";
                            } else {
                                progress += "null;";
                            }
                        }
                    }

                    String[] course = {"2103", "2203", "3223", "3243", "3333", "3363", "4003", "4033", "4123", "4363", "4523"};


                    String getIDNumber = "select Max(dafId) as id from degreeAF;";
                    Statement stmt1 = con.createStatement();
                    ResultSet idResult = stmt1.executeQuery(getIDNumber);
                    int newID = 0;
                    if (idResult.next()) {
                        String temp = idResult.getString("id");
                        newID = Integer.parseInt(temp) + 1;
                    }

                    String deleteRecords = "delete from degreeAF where dafUID = '" + user + "';";
                    Statement delete = con.createStatement();
                    delete.executeUpdate(deleteRecords);

                    String query = "";
                    for (int k = 0; k < progress.split(";").length; k++) {
                        if (progress.split(";")[k] != "") {
                            int idnum = newID + k;
                            query += "insert into degreeAF values (" + idnum + ",'" + course[k] + "','MSIS','" + user + "'," + progress.split(";")[k] + ");";
                        }
                    }
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);
                    z = "Progress has been updated";
                    isSuccess = true;
                    con.close();


                    //z = query;

                }
            } catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage();

                Log.d("sql error", z);
            }
            return z;
        }
    }


}
