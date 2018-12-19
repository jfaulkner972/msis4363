package com.example.jfaulkner.prereqmobile;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class login extends AppCompatActivity
{
    // Declaring layout button, edit texts
    public Button run;
    public TextView message;
    public ProgressBar progressBar;

    // End Declaring layout button, edit texts

    // Declaring connection variables
    public Connection con;
    String pass, userAccount;
    //End Declaring connection variables

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Getting values from button, texts and progress bar
        run = (Button) findViewById(R.id.login);

        run.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckLogin checkLogin = new CheckLogin();// this is the Asynctask, which is used to process in background to reduce load on app process
                checkLogin.execute("");
            }
        });
        //End Setting up the function when button login is clicked
    }

    public class CheckLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String name1 = "";

        @Override
        protected void onPostExecute(String r)
        {
            Toast.makeText(login.this, r, Toast.LENGTH_LONG).show();
            if(isSuccess)
            {
                Intent a = new Intent(login.this,menuActivity.class);
                a.putExtra("user",userAccount);
                login.this.startActivity(a);
            }
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
                    // Change below query according to your own database.
                    TextView user = findViewById(R.id.username);
                    String username = user.getText().toString();

                    TextView pass = findViewById(R.id.password);
                    String password = pass.getText().toString();

                    String query = "select loginID, pass from userSignIn where loginID = '" + username + "' and pass = '" + password + "';";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if(rs.next())
                    {
                        name1= rs.getString("loginID"); //Name is the string label of a column in database, read through the select query
                        z = "Welcome";
                        isSuccess=true;
                        con.close();

                    }
                    else
                    {
                        z = "Invalid Username or Password";
                        isSuccess = false;
                    }
                    userAccount = username;
                    user.setText("");
                    pass.setText("");
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

    public void signUp(View v){
        Intent i = new Intent(this,signup.class);
        startActivity(i);

    }

}
