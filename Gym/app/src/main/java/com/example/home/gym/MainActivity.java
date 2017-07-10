package com.example.home.gym;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dbconnect db=new Dbconnect();
        db.execute("select * from cust_login;");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private class Dbconnect extends AsyncTask<String,Void,Void>
    {
         public String[] username=new String[25];


        @Override
        protected Void doInBackground(String... params) {
            Dbdetails db=new Dbdetails();


            Connection conn=null;
            try
            {
                Class.forName(db.getDriver());


                conn= DriverManager.getConnection(db.getUrl(), db.getUserName(), db.getPass());
                Statement stmt=null;
                stmt = conn.createStatement();
                String sql=params[0];
                ResultSet rs;

                rs=stmt.executeQuery(sql);


                if(rs.next()) {

                    int i = 0;
                    try {
                        rs.beforeFirst();
                        while (rs.next()) {

                            username[i]=new String();
                            username[i]=rs.getString("user_id");


                            i++;
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                }
                stmt.close();

            }catch(SQLException se)
            {
                se.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally{
                try{
                    if(conn!=null)
                        conn.close();
                }catch(SQLException se)
                {
                    se.printStackTrace();
                }
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(getApplicationContext(),username[0],Toast.LENGTH_SHORT).show();
        }
    }


}
