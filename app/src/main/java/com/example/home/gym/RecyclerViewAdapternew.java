package com.example.home.gym;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Alagesh on 01-02-2016.
 */
public class RecyclerViewAdapternew extends RecyclerView.Adapter<RecyclerViewAdapternew.ItemHolder> {

    static Context context;
    private List<String> itemsName;
    private OnItemClickListener onItemClickListener;
    private LayoutInflater layoutInflater;
    private List<String> itemsValue;
    private List<String> colora;
    int lastPosition=-1;

    String text;

    public RecyclerViewAdapternew(Context context){
        layoutInflater = LayoutInflater.from(context);
        itemsName = new ArrayList<String>();
        itemsValue= new ArrayList<String>();
        colora= new ArrayList<String>();
        this.context=context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView itemcard=(CardView)layoutInflater.inflate(R.layout.list_view,parent,false);
        return new ItemHolder(itemcard,this);
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        holder.setItemName(itemsName.get(position));
        holder.setItemValue(itemsValue.get(position));
        holder.setItemImage(Integer.valueOf(colora.get(position)));
        holder.setChars(String.valueOf(itemsName.get(position).toUpperCase().charAt(0)));
        setAnimation(holder.card,position);
    }

    @Override
    public int getItemCount() {
        return itemsName.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public OnItemClickListener getOnItemClickListener(){
        return onItemClickListener;
    }

    public interface OnItemClickListener{
        public void onItemClick(ItemHolder item, int position);
    }

    public void add(int location, String iName, String iValue,int i,Context context){
        itemsName.add(location, iName);
        itemsValue.add(location,iValue);
        colora.add(location,String.valueOf(i));
        this.context=context;
        notifyItemInserted(location);
    }

    public void remove(int location){
        if(location >= itemsName.size())
            return;

        itemsName.remove(location);
        notifyItemRemoved(location);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private RecyclerViewAdapternew parent;
        TextView textItemName;
        TextView textItemValue;
        ImageView rl;


        TextView chars;
        CardView card;

        public ItemHolder(CardView itemView, RecyclerViewAdapternew parent) {
            super(itemView);
            itemView.setOnClickListener(this);
            card=itemView;
            this.parent = parent;
            textItemName = (TextView) card.findViewById(R.id.video_name);
            textItemValue = (TextView) card.findViewById(R.id.video_desc);
            rl= (ImageView) card.findViewById(R.id.mybg);

            chars = (TextView) card.findViewById(R.id.video_char);
            /*img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                onClick(v);}
            });*/
        }

        public void setItemName(CharSequence name){
            textItemName.setText(name);
        }


        public void setItemValue(CharSequence description){

            textItemValue.setText(description);
        }
        public void setItemImage(int i){

            int[] colors={context.getResources().getColor(R.color.ada1),context.getResources().getColor(R.color.ada2),context.getResources().getColor(R.color.ada3),context.getResources().getColor(R.color.ada4),context.getResources().getColor(R.color.ada5),context.getResources().getColor(R.color.ada6),context.getResources().getColor(R.color.ada7),context.getResources().getColor(R.color.ada8)};
//            rl.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            ShapeDrawable sd=new ShapeDrawable(new OvalShape());
            sd.setIntrinsicHeight(50);
            sd.setIntrinsicWidth(50);
            sd.getPaint().setColor(colors[i]);
            rl.setImageDrawable(sd);

        }

        public void setChars(CharSequence chars) {
            this.chars.setText(chars);
        }
        public CharSequence getItemName(){


            return textItemName.getText();
        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = parent.getOnItemClickListener();
            if(listener != null){
                listener.onItemClick(this, getPosition());
            }
        }


    }
//    public void transition(int position,ItemHolder holder)
//    {
//        Intent intent = new Intent(context, Display.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Pair<View,String> img=Pair.create((View)holder.img,"thumbnail");
//            Pair<View,String> maintext=Pair.create((View)holder.textItemName,"maintext");
//            Pair<View,String> desc=Pair.create((View)holder.textItemValue,"desc");
//            Pair<View,String> play=Pair.create((View)holder.play,"play");
//            Activity activity = null;
//            ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(activity,  holder.img, "thumbnail");
//            context.startActivity(intent,optionsCompat.toBundle());
//        }
//        else {
//            intent.putExtra("user", text);
//            intent.putExtra("videoid", vid_path.get(position));
//            intent.putExtra("id", 1);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        }
//    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated

        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
