package com.example.home.gym;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;



/**
 * Created by Abishek on 11/16/2015.
 */
public class CustomArrayAdapter extends ArrayAdapter {

    Context context;
    ArrayList<String> main_text;
    ArrayList<String> sub_text;
    private ArrayList<String> path;

    public CustomArrayAdapter(Context context, ArrayList<String> main_txt,ArrayList<String> sub_txt){//,String [] sub_txt) {
        super(context, R.layout.list_view,main_txt);
        this.context=context;
        this.main_text=main_txt;
        this.sub_text=sub_txt;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void remove(Object object) {
        super.remove(object);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.list_view,parent,false);
        TextView textView1= (TextView) row.findViewById(R.id.video_name);
        TextView textView2= (TextView) row.findViewById(R.id.video_desc);
        TextView textView3= (TextView) row.findViewById(R.id.video_char);
        textView1.setText(main_text.get(position));
        textView2.setText(sub_text.get(position));
        char c=main_text.get(position).toUpperCase().charAt(0);

        textView3.setText(String.valueOf(c));
        return row;
    }



    public ArrayList<String> getPath() {
        return path;
    }
}