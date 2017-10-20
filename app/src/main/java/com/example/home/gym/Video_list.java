package com.example.home.gym;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static com.google.android.gms.internal.zzs.TAG;

public class Video_list extends AppCompatActivity implements RecyclerViewAdapternew.OnItemClickListener{

    RecyclerViewAdapternew myadapter;
    RecyclerView myrecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String type=getIntent().getStringExtra("type");
        setTitle(type);
        setSupportActionBar(toolbar);
        DefaultItemAnimator itemAnimator=new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);

        myrecycler= (RecyclerView) findViewById(R.id.listView);
        myadapter=new RecyclerViewAdapternew(getApplicationContext());
        myadapter.setOnItemClickListener(this);
        String[] name=new String[]{"Chest","Abs","Shoulder","Arms","Sixpack","Legs","Back arms","Gym"};
        ArrayList<String> list=new ArrayList<>();
        for(int i=0;i<name.length;i++){
            Random r=new Random();
            int ii=r.nextInt(7)+0;
            Log.d(TAG, "onActivityCreated: Adadei "+String.valueOf(ii)+" added");
            myadapter.add(myadapter.getItemCount(),name[i],name[i],ii,getApplicationContext());
        }

        myrecycler.setAdapter(myadapter);
        myrecycler.setItemAnimator(itemAnimator);
        myrecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



    }

    @Override
    public void onItemClick(RecyclerViewAdapternew.ItemHolder item, int position) {
        Toast.makeText(getApplicationContext(),"lol"+String.valueOf(position),Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(getActivity(), Display.class);
//        intent.putExtra("user", text);
//        intent.putExtra("videoid", vid[position]);
//        startActivity(intent);
    }
}
