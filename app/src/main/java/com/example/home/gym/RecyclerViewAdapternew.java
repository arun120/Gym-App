package com.example.home.gym;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alagesh on 01-02-2016.
 */
public class RecyclerViewAdapternew extends RecyclerView.Adapter<RecyclerViewAdapternew.ItemHolder> {

    Activity activity;
    static Context context;
    private List<String> itemsName;
    private List<String> vid_path;
    private OnItemClickListener onItemClickListener;
    private LayoutInflater layoutInflater;
    private List<String> itemsValue;
    int lastPosition=-1;
    static Button play;
    String text;

    public RecyclerViewAdapternew(Context context){
        layoutInflater = LayoutInflater.from(context);
        itemsName = new ArrayList<String>();
        itemsValue= new ArrayList<String>();
        vid_path= new ArrayList<String>();
        this.context=context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView itemcard=(CardView)layoutInflater.inflate(R.layout.card_resources,parent,false);
        return new ItemHolder(itemcard,this);
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        holder.setItemName(itemsName.get(position));
        holder.setItemValue(itemsValue.get(position));
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        transition(position,holder);
            }
        });
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

    public void add(int location, String iName, String iValue,String path,String text,Context context,Activity activity){
        itemsName.add(location, iName);
        itemsValue.add(location,iValue);
        this.context=context;
        this.activity=activity;
        vid_path.add(location,path);
        notifyItemInserted(location);
        this.text=text;
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
        CardView card;
        ImageView img;
        Button play;

        public ItemHolder(CardView itemView, RecyclerViewAdapternew parent) {
            super(itemView);
            itemView.setOnClickListener(this);
            card=itemView;
            this.parent = parent;
            textItemName = (TextView) card.findViewById(R.id.text_card_l);
            play= (Button) card.findViewById(R.id.card_play);
            textItemValue = (TextView) card.findViewById(R.id.text_card_m);
            img=(ImageView) card.findViewById(R.id.card_image);
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

        public void setImg(int thumbnail_id) {
            img.setImageResource(thumbnail_id);
        }
    }
    public void transition(int position,ItemHolder holder)
    {
        Intent intent = new Intent(context, Display.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Pair<View,String> img=Pair.create((View)holder.img,"thumbnail");
            Pair<View,String> maintext=Pair.create((View)holder.textItemName,"maintext");
            Pair<View,String> desc=Pair.create((View)holder.textItemValue,"desc");
            Pair<View,String> play=Pair.create((View)holder.play,"play");
            Activity activity = null;
            ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(activity,  holder.img, "thumbnail");
            context.startActivity(intent,optionsCompat.toBundle());
        }
        else {
            intent.putExtra("user", text);
            intent.putExtra("videoid", vid_path.get(position));
            intent.putExtra("id", 1);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
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
