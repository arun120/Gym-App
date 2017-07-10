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
    int[] images;
    Activity activity;
    ArrayList<String> main_text;
    ArrayList<String> sub_text;
    String rn;
    ListView listView;
    private ArrayList<String> path;
    AdapterView.OnItemClickListener onItemClickListener;
//    String [] sub_text;

    public CustomArrayAdapter(Context context, ArrayList<String> main_txt,ArrayList<String> sub_txt,String rn){//,String [] sub_txt) {
        super(context, R.layout.card_resources,R.id.text_card_l,main_txt);
        this.context=context;
        //this.activity=activity;
        this.rn=rn;
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
        View row=inflater.inflate(R.layout.card_resources,parent,false);
        ImageView imageView= (ImageView) row.findViewById(R.id.card_image);
        Button play= (Button) row.findViewById(R.id.card_play);
        final TextView textView1= (TextView) row.findViewById(R.id.text_card_l);
        TextView textView2= (TextView) row.findViewById(R.id.text_card_m);
        //Images[0]=R.drawable.pdf_list;
//        int i= images.get(position);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
  /*              Intent load = new Intent(Intent.ACTION_VIEW);
                String path = new String();

                path = main_text.get(position);
                //Toast.makeText(getActivity(),path,Toast.LENGTH_SHORT).show();
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/49311.pdf");
                load.setDataAndType(Uri.fromFile(file), "text/plain");
                load.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Toast.makeText(context,"Working daww",Toast.LENGTH_LONG).show();
                context.startActivity(load);
*/
            }
        });

        //imageView.setImageResource(Images[images[position]]);
        textView1.setText(main_text.get(position));
//        textView2.setText(sub_text.get(position));
        return row;
    }



    public ArrayList<String> getPath() {
        return path;
    }
}