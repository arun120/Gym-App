package com.example.home.gym;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static com.google.android.gms.internal.zzs.TAG;


public class Others extends android.support.v4.app.Fragment {

    String userid=null;
    RecyclerViewAdapternew myadapter;
    RecyclerView myrecycler;
    public Others() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userid = getArguments().getString("user");

        }


        Toast.makeText(getActivity(),userid,Toast.LENGTH_SHORT).show();
    }

    ImageMap front,back;
    TextView front_t,back_t;
    RelativeLayout frontl,backl,frontm,backm;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        front = (ImageMap)getActivity().findViewById(R.id.map);
        back=(ImageMap)getActivity().findViewById(R.id.map1);
        front_t= (TextView) getActivity().findViewById(R.id.fronttext);
        back_t= (TextView) getActivity().findViewById(R.id.backtext);
        frontm= (RelativeLayout) getActivity().findViewById(R.id.fronton);
        backm= (RelativeLayout) getActivity().findViewById(R.id.backon);
        frontl= (RelativeLayout) getActivity().findViewById(R.id.front);
        backl= (RelativeLayout) getActivity().findViewById(R.id.back);
        frontl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                front_t.setTextColor(getResources().getColor(R.color.white));
                back_t.setTextColor(getResources().getColor(R.color.grey));
                frontm.setVisibility(View.VISIBLE);
                backm.setVisibility(View.GONE);
                front.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
            }
        });
        backl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                front_t.setTextColor(getResources().getColor(R.color.grey));
                back_t.setTextColor(getResources().getColor(R.color.white));
                frontm.setVisibility(View.GONE);
                backm.setVisibility(View.VISIBLE);
                front.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
            }
        });
        front.addOnImageMapClickedHandler(new ImageMap.OnImageMapClickedHandler()
        {
            @Override
            public void onImageMapClicked(int id, ImageMap imageMap)
            {
                // when the area is tapped, show the name in a
                // text bubble
                if(front.getLocationName(id)!=null)
                    Toast.makeText(getContext(),"lol "+String.valueOf(front.getLocationName(id)),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBubbleClicked(int id)
            {
                // react to info bubble for area being tapped
            }
        });




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_others, container, false);
    }




}
