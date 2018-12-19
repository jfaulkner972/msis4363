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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class displaySchedule extends AppCompatActivity {

    public Connection con;
    private static String user = "";
    private static String schNum = "";
    private String nextClass = "";
    private String preq = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_schedule);
        user = getIntent().getStringExtra("user");

        if (getIntent().getStringExtra("activateSave") != null) {
            buildSchedule buildSchedule = new buildSchedule();
            buildSchedule.execute("");
        } else {
            schNum = getIntent().getStringExtra("schNum");
            loadSchedule loadSchedule = new loadSchedule();
            loadSchedule.execute("");
        }
    }

    public class buildSchedule extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        @Override
        protected void onPostExecute(String r) {

            int setInvisble = 0;
            for (int i = 0; i < nextClass.split(";").length; i++) {
                TextView t = findViewById(getResources().getIdentifier("course" + (i + 1), "id", getPackageName()));
                t.setText(nextClass.split(";")[i]);
                setInvisble = i;
            }
            setInvisble += 2;
            while (setInvisble < 7) {
                TextView u = findViewById(getResources().getIdentifier("course" + (setInvisble), "id", getPackageName()));
                u.setVisibility(View.INVISIBLE);
                setInvisble++;
            }

            TextView t = findViewById(R.id.status);
            t.setText("You need to have passed the following classes to take these classes. " +
                    "Check your progress to see if you have passed them: " + r);

            //Toast.makeText(displaySchedule.this, r, Toast.LENGTH_LONG).show();
            preq = r;
            Button b = findViewById(R.id.saveSchedule);
            b.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String course = "";
            String query = "";
            nextClass = getIntent().getStringExtra("nextClass");

            try {
                con = connectionclass();        // Connect to database
                if (con == null) {
                    z = "Check Your Internet Access!";
                } else {
                    for (int i = 0; i < nextClass.split(";").length; i++) {
                        query = "select preqOne from prereq where courseNumber = '" + nextClass.split(";")[i].split(" ")[1] + "';";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        while (rs.next()) {
                            name1 = rs.getString("preqOne"); //Name is the string label of a column in database, read through the select query
                            course += name1 + ",";
                        }
                    }
                    con.close();
                    z = course;
                    //z= query1;

                }
            } catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage();

                Log.d("sql error", z);
            }

            return z;
        }
    }

    public class loadSchedule extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        @Override
        protected void onPostExecute(String r) {
            //Toast.makeText(displaySchedule.this, r, Toast.LENGTH_LONG).show();
            int setInvisble = 0;
            TextView success = findViewById(R.id.status);
            if (r.split("z").length == 2) {
                success.setText("You need to have passed the following classes to take these classes. " +
                        "Check your progress to see if you have passed them: " + r.split("z")[1]);
                for (int i = 0; i < r.split("z")[0].split(";").length; i++) {
                    TextView t = findViewById(getResources().getIdentifier("course" + (i + 1), "id", getPackageName()));
                    t.setText(r.split(";")[i]);
                    setInvisble = i;
                }
                setInvisble += 2;
                while (setInvisble < 7) {
                    TextView u = findViewById(getResources().getIdentifier("course" + (setInvisble), "id", getPackageName()));
                    u.setVisibility(View.INVISIBLE);
                    setInvisble++;
                }
            } else {
                success.setText("Please Make a Schedule");
                setInvisble = 1;
                while (setInvisble < 7) {
                    TextView u = findViewById(getResources().getIdentifier("course" + (setInvisble), "id", getPackageName()));
                    u.setVisibility(View.INVISIBLE);
                    setInvisble++;
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String course = "";
            try {
                con = connectionclass();        // Connect to database
                if (con == null) {
                    z = "Check Your Internet Access!";
                } else {
                    String temp = "";
                    String query = "select schCourse from schedule where schUser = '" + user + "' and schNum =" + Integer.parseInt(schNum);
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        name1 = rs.getString("schCourse"); //Name is the string label of a column in database, read through the select query
                        course += name1 + ";";
                    }

                    String query1 = "select schStatus from schedule where schUser = '" + user + "' and schNum = " + Integer.parseInt(schNum);
                    Statement stmt1 = con.createStatement();
                    ResultSet rs1 = stmt1.executeQuery(query1);
                    while (rs1.next()) {
                        temp = rs1.getString("schStatus"); //Name is the string label of a column in database, read through the select query

                    }

                    z = course + "z" + temp;

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

    public void saveSchedule(View v) {
        insertSchedule insertSchedule = new insertSchedule();
        insertSchedule.execute("");
    }

    public class insertSchedule extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(displaySchedule.this, r, Toast.LENGTH_LONG).show();
            Intent i = new Intent(displaySchedule.this, nextSemester.class);
            i.putExtra("user", user);
            displaySchedule.this.startActivity(i);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                con = connectionclass();        // Connect to database
                if (con == null) {
                    z = "Check Your Internet Access!";
                } else {
                    String getIDNumber = "select Max(schID) as id from schedule;";
                    Statement stmt1 = con.createStatement();
                    ResultSet idResult = stmt1.executeQuery(getIDNumber);
                    int newID = 0;
                    if (idResult.next()) {
                        String temp = idResult.getString("id");
                        newID = Integer.parseInt(temp) + 1;
                    }

                    String getscheduleNumber = "select schNum as number from schedule where schID = (select max(schID) from schedule) and schUser = '" + user + "';";
                    Statement stmt2 = con.createStatement();
                    ResultSet scheduleIDResult = stmt2.executeQuery(getscheduleNumber);
                    int newscheduleID = 1;
                    if (scheduleIDResult.next()) {
                        String temp = scheduleIDResult.getString("number");
                        if ((Integer.parseInt(temp) + 1) >= 5) {
                            newscheduleID = 1;
                        } else {
                            newscheduleID = Integer.parseInt(temp) + 1;
                        }
                    }

                    String deleteRecords = "delete from schedule where schNum = " + newscheduleID + ";";
                    Statement delete = con.createStatement();
                    delete.executeUpdate(deleteRecords);

                    String query = "";
                    for (int k = 0; k < nextClass.split(";").length; k++) {
                        int idnum = newID + k;
                        query += "insert into schedule values (" + idnum + "," + newscheduleID + ",'" + user + "','" + nextClass.split(";")[k] + "','" + preq + "');";
                    }
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);
                    z = "Schedule has been saved as " + newscheduleID;
                    isSuccess = true;
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

}
