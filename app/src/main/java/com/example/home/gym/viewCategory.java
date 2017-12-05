package com.example.home.gym;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class viewCategory extends AppCompatActivity implements RecyclerViewAdapternew.OnItemClickListener {

    RecyclerViewAdapternew myadapter;
    RecyclerView myrecycler;
    Dbconnect db = new Dbconnect();
    String vid[] = new String[500];
    String[] name = new String[500];
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);
        DefaultItemAnimator itemAnimator=new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);


        text = getIntent().getExtras().getString("user");
        String category = getIntent().getExtras().getString("category");



        myrecycler= (RecyclerView) findViewById(R.id.listView1);
        myadapter=new RecyclerViewAdapternew(getApplicationContext());
        myadapter.setOnItemClickListener(this);
        myrecycler.setItemAnimator(itemAnimator);
        myrecycler.setLayoutManager(new LinearLayoutManager(this));

        db.execute(category);
    }




    @Override
    public void onItemClick(RecyclerViewAdapternew.ItemHolder item, int position) {
        Intent intent = new Intent(viewCategory.this, Display.class);
        intent.putExtra("user", text);
        intent.putExtra("videoid", vid[position]);
        startActivity(intent);

    }


    public class Dbconnect extends AsyncTask<String, Void, Void> {

        Dbdetails dbdetails = new Dbdetails();
        Connection connection = null;
        Statement statement = null;
        int i = 0, j = 0;
        Statement st=null;
        Statement st1=null;
        ResultSet rs2;
        @Override
        protected Void doInBackground(String... params) {
            try {
                Class.forName(dbdetails.getDriver());
                connection = DriverManager.getConnection(dbdetails.getUrl(), dbdetails.getUserName(), dbdetails.getPass());
                statement = connection.createStatement();


                st1 = connection.createStatement();
                for (j = 0; j < i; j++) {
                    String sql2 = "select * from video where path like '%" + params[0] + "%'";

                    rs2 = st1.executeQuery(sql2);
                    //rs2.beforeFirst();
                    if (rs2.next()) {
                        vid[j]=rs2.getString("video_id");
                        name[j] = rs2.getString("name");
                    }
                }
                st1.close();
                st.close();
                statement.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null)
                        connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
           return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            int j = 0;
            //for(j=0;j<5;j++)
            //  Toast.makeText(getApplicationContext(),path[j],Toast.LENGTH_SHORT).show();                                                                                                    }

            //  ListView listView = (ListView) getActivity().findViewById(R.id.listView);


            Log.i("Video","Desc");
            for(j=0;j<i;j++)
            {
                if(vid[j]!=null)
                    //            myadapter.add(myadapter.getItemCount(),name[j],des[j],vid[j],text,getActivity().getApplicationContext(),getActivity());


                myadapter.add(myadapter.getItemCount(),name[j],vid[j],j,getApplicationContext());
            }


            myrecycler.setAdapter(myadapter);


        }
    }
}
