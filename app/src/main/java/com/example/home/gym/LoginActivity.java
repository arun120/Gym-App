package com.example.home.gym;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {
    String user;
    String pass;

    String abc = "hello";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText userName, password;
        Button button;

        userName = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        button = (Button) findViewById(R.id.login);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Button clicked!",Toast.LENGTH_SHORT).show();

                user = userName.getText().toString();
                pass = password.getText().toString();
                //  Toast.makeText(getApplicationContext(),user,Toast.LENGTH_LONG).show();

                DbConnect dbConnect = new DbConnect();
                //               Toast.makeText(getApplicationContext(),user,Toast.LENGTH_LONG).show();
                dbConnect.execute(user, pass);


            }
        });
    }

    private class DbConnect extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Dbdetails db = new Dbdetails();

            ResultSet rs;
            Connection conn = null;
            try {
                Class.forName(db.getDriver());


                conn = DriverManager.getConnection(db.getUrl(), db.getUserName(), db.getPass());
                Statement stmt;
                stmt = conn.createStatement();
                String sql = "select * from cust_login where user_id like '" + params[0] + "' and  pass like '" + params[1] + "';";

                rs = stmt.executeQuery(sql);


                if (rs.next())
                    abc = "found";
                else
                    abc = "not found";

                stmt.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            return abc;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (abc.equals("found")) {
                Intent intent = new Intent(LoginActivity.this, FragmentManagement.class);
                intent.putExtra("user", user);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), abc, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getApplicationContext(), abc + "notfound", Toast.LENGTH_SHORT).show();

        }


    }
}