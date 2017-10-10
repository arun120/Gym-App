package com.example.home.gym;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.skyfishjy.library.RippleBackground;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {
    String user;
    String pass;
    TextView v1,v2;
    String abc = "hello";
    RelativeLayout rl;
    Typeface typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText userName, password;
        Button button;

        userName = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.phone);
        button = (Button) findViewById(R.id.register);

        String type="fontb.otf";
        typeface= Typeface.createFromAsset(getAssets(),type);
        YoYo.with(Techniques.Landing).duration(1500).playOn(findViewById(R.id.logo));
        final RippleBackground rippleBackground= (RippleBackground) findViewById(R.id.content);
        rippleBackground.startRippleAnimation();
        final Boolean bol=rippleBackground.isRippleAnimationRunning();
        if(bol==true){
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rippleBackground.stopRippleAnimation();
                }
            },1500);
        }

        v1= (TextView) findViewById(R.id.textView5);
        v2= (TextView) findViewById(R.id.usrs);
        rl= (RelativeLayout) findViewById(R.id.rel);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rippleBackground.startRippleAnimation();
                Boolean bols=rippleBackground.isRippleAnimationRunning();
                if(bols==true){
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rippleBackground.stopRippleAnimation();
                        }
                    },1500);
                }
//                EditText gname=(EditText) findViewById(R.id.name);
//                EditText gphone=(EditText) findViewById(R.id.phone);
//                EditText gaddress=(EditText) findViewById(R.id.address);


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

                YoYo.with(Techniques.SlideOutUp).duration(1000).playOn(findViewById(R.id.rel));
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rl.setVisibility(View.INVISIBLE);
                        v1.setTypeface(typeface);
                        v2.setTypeface(typeface);
                        v1.setVisibility(View.VISIBLE);
                        v2.setVisibility(View.VISIBLE);
                        v2.setText(user);
                        YoYo.with(Techniques.BounceIn).duration(1500).playOn(findViewById(R.id.textView5));
                        YoYo.with(Techniques.BounceIn).duration(1500).playOn(findViewById(R.id.usrs));
                    }
                },900);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoginActivity.this, Homepage.class);
                        intent.putExtra("user", user);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), abc, Toast.LENGTH_SHORT).show();
                    }
                },4000);

            } else
                Toast.makeText(getApplicationContext(), abc + "notfound", Toast.LENGTH_SHORT).show();

        }


    }
}