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

public class signup extends AppCompatActivity {

    public Button run;
    public Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        run = (Button) findViewById(R.id.login);

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckLogin checkLogin = new CheckLogin();// this is the Asynctask, which is used to process in background to reduce load on app process
                checkLogin.execute("");
            }
        });
    }

    public class CheckLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(signup.this, r, Toast.LENGTH_LONG).show();
            if (isSuccess) {
                Intent a = new Intent(signup.this, login.class);
                signup.this.startActivity(a);
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                con = connectionclass();        // Connect to database
                if (con == null) {
                    z = "Check Your Internet Access!";
                }
                else {
                    String getIDNumber = "select Max(id) as id from userSignIn;";
                    Statement stmt1 = con.createStatement();
                    ResultSet idResult = stmt1.executeQuery(getIDNumber);
                    if (idResult.next()) {
                        String temp = idResult.getString("id");
                        int newID = Integer.parseInt(temp) + 1;

                        TextView user = findViewById(R.id.username);
                        String username = user.getText().toString();

                        TextView pass = findViewById(R.id.password);
                        String password = pass.getText().toString();

                        String insert = "insert into userSignIn values (" + newID + ",'" + username + "','" + password + "');";
                        Statement stmt2 = con.createStatement();
                        try {
                            stmt2.executeQuery(insert);
                        } catch (Exception a) {
                            Log.d("issue", "issue happened");
                        }

                        String query = "select loginID, pass from userSignIn where loginID = '" + username + "';";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if (rs.next()) {
                            name1 = rs.getString("loginID"); //Name is the string label of a column in database, read through the select query
                            z = "Thank you for signing up please login";
                            isSuccess = true;
                            con.close();

                        } else {
                            z = "Invalid Username";
                            isSuccess = false;
                        }
                        user.setText("");
                        pass.setText("");
                    }
                    else{
                        z = "No ID";
                    }
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
