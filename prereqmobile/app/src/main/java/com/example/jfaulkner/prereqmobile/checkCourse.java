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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class checkCourse extends AppCompatActivity {

    public Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_course);
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
                con = connectionclass();        // Connect to database
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


    @SuppressLint("NewApi")
    public Connection connectionclass()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //your database connection string goes below
            ConnectionURL = "jdbc:jtds:sqlserver://prereqaf.database.windows.net:1433;DatabaseName=prereqaf;user=raven@prereqaf;password=yourHeartAndSoul!;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=300;";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }

    public void checkCoursePreReq(View v){
        Button b = (Button)v;
        String courseName = b.getText().toString();
        Intent i = new Intent(this,courseResults.class);
        i.putExtra("name",courseName);
        startActivity(i);

    }
}
