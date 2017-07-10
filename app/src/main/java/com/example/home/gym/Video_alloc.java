package com.example.home.gym;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Video_alloc extends android.support.v4.app.Fragment implements RecyclerViewAdapternew.OnItemClickListener {

    String text = new String();
    Dbconnect dbconnect = new Dbconnect();
    String vid[] = new String[50];
    RecyclerViewAdapternew myadapter;
    RecyclerView myrecycler;

    public Video_alloc() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_video_alloc, container, false);



    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (getArguments() != null) {
            text = getArguments().getString("user");

        }
// set an exit transition

        DefaultItemAnimator itemAnimator=new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);

        myrecycler= (RecyclerView) getActivity().findViewById(R.id.myrecyclerview);
        myadapter=new RecyclerViewAdapternew(getActivity().getApplicationContext());
        myadapter.setOnItemClickListener(this);
        myrecycler.setAdapter(myadapter);
        myrecycler.setItemAnimator(itemAnimator);
        myrecycler.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),2,LinearLayoutManager.VERTICAL,false));

        //  Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
        dbconnect.execute(text);

    }

    @Override
    public void onItemClick(RecyclerViewAdapternew.ItemHolder item, int position) {
        Intent intent = new Intent(getActivity(), Display.class);
        intent.putExtra("user", text);
        intent.putExtra("videoid", vid[position]);
        startActivity(intent);
    }

    public class Dbconnect extends AsyncTask<String, Void, String[]> {
        String[] des = new String[50];
        String[] name = new String[50];
        String[] path = new String[50];
        Dbdetails dbdetails = new Dbdetails();
        Connection connection = null;
        Statement statement = null;
        String id = new String();
        String pri[] = new String[50];
        int i = 0, j = 0;
        Statement st=null;
        Statement st1=null;
        ResultSet rs2;
        ResultSet rs;
        ResultSet rs1;
        @Override
        protected String[] doInBackground(String... params) {
            try {
                Class.forName(dbdetails.getDriver());
                connection = DriverManager.getConnection(dbdetails.getUrl(), dbdetails.getUserName(), dbdetails.getPass());
                statement = connection.createStatement();
                String sql = "select * from video_alloc where cust_id like '" + params[0] + "'";
                rs = statement.executeQuery(sql);
                rs.beforeFirst();
                if (rs.next())
                    id = rs.getString("set_id");
                String sql1 = "select * from exercise_set where set_id like '" + id + "' order by priority";
                 st = connection.createStatement();
                rs1 = st.executeQuery(sql1);
                //rs1.beforeFirst();
                while (rs1.next()) {
                    vid[i] = new String();
                    pri[i] = new String();
                    vid[i] = rs1.getString("video_id");
                    pri[i] = rs1.getString("priority");
                    i++;

                }

                st1 = connection.createStatement();
                for (j = 0; j < i; j++) {
                    String sql2 = "select * from video where video_id like '" + vid[j] + "'";

                    rs2 = st1.executeQuery(sql2);
                    //rs2.beforeFirst();
                    if (rs2.next()) {
                        des[j] = new String();
                        path[j] = new String();
                        name[j] = new String();
                        des[j] = rs2.getString("des");
                        path[j] = rs2.getString("path");
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
            return path;
        }

        @Override
        protected void onPostExecute(String[] aVoid) {
            super.onPostExecute(aVoid);
            int j = 0;
            //for(j=0;j<5;j++)
            //  Toast.makeText(getApplicationContext(),path[j],Toast.LENGTH_SHORT).show();                                                                                                    }

          //  ListView listView = (ListView) getActivity().findViewById(R.id.listView);
            ArrayList<String> ls = new ArrayList<>();
            ArrayList<String> desc = new ArrayList<>();

             for(j=0;j<i;j++)
            {
            if(vid[j]!=null)
                myadapter.add(myadapter.getItemCount(),name[j],des[j],vid[j],text,getActivity().getApplicationContext(),getActivity());
                ls.add(name[j]);
                ls.add(des[j]);
            }
            CustomArrayAdapter arrayAdapter=new CustomArrayAdapter(getActivity().getApplicationContext(),ls,desc,text);
            //listView.setAdapter(arrayAdapter);
            /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getApplicationContext(), path[position], Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), Display.class);
                    intent.putExtra("user", text);
                    intent.putExtra("videoid", vid[position]);
                    startActivity(intent);
                }
            });*/
        }
    }
}