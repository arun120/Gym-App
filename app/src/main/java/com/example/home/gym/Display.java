package com.example.home.gym;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.BoolRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.rey.material.widget.Slider;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Display extends AppCompatActivity {
    String[] details = new String[30];
    String[] column_name = new String[30];
    Boolean allowed = true;
    String user = new String();
    String vid = new String();

    VideoView video;
    Handler handler = new Handler();
    ImageButton next, pause, previous;
    TextView current_dur, total_dur;
    RelativeLayout widgelayout;
    com.rey.material.widget.Slider slider;
    String inetpath;
    FloatingActionButton fab;
    int i = 0;
    int seek = 0;
    boolean completedbuffer = false;
    ProgressDialog mProgressDialog;
    TextView namedisp,descdisp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        user = getIntent().getExtras().getString("user");
        vid = getIntent().getExtras().getString("videoid");


        Toast.makeText(Display.this, vid, Toast.LENGTH_SHORT).show();
        new Videodet().execute(vid);

        Button skip = (Button) findViewById(R.id.skip);
        Button partial = (Button) findViewById(R.id.partial);
        Button completed = (Button) findViewById(R.id.completed);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Bufferring....");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        slider = (com.rey.material.widget.Slider) findViewById(R.id.seek);
        video = (VideoView) findViewById(R.id.half_video);

        pause = (ImageButton) findViewById(R.id.pause);
        next = (ImageButton) findViewById(R.id.next);
        previous = (ImageButton) findViewById(R.id.previous);
        widgelayout = (RelativeLayout) findViewById(R.id.widgetlayout);
        widgelayout.setVisibility(View.GONE);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        namedisp=(TextView) findViewById(R.id.name);
        descdisp=(TextView) findViewById(R.id.desc);



        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new History().execute("skip");
            }
        });


        partial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new History().execute("partial");
            }
        });


        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new History().execute("completed");
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            video_call();
            }
        });

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                int vid_dur = video.getDuration();
                Log.i("time", String.valueOf(vid_dur));
                Toast.makeText(getApplicationContext(), String.valueOf(vid_dur), Toast.LENGTH_LONG).show();
                total_dur = (TextView) findViewById(R.id.tot_dur);
                current_dur = (TextView) findViewById(R.id.cur_dur);
                String dur = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(vid_dur),
                        TimeUnit.MILLISECONDS.toMinutes(vid_dur) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(vid_dur)), // The change is in this line
                        TimeUnit.MILLISECONDS.toSeconds(vid_dur) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(vid_dur)));
                total_dur.setText(dur);


                slider.setValueRange(0, vid_dur / 100, true);
                update();
                completedbuffer = true;
                mProgressDialog.dismiss();


            }
        });


        slider.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {

                if (seek == 1)
                    seek = 0;
                else {
                    video.seekTo((int) TimeUnit.SECONDS.toMillis((int) newValue) / 10);
                    int vid_dur = video.getCurrentPosition();
                    float time = TimeUnit.MILLISECONDS.toSeconds(video.getCurrentPosition());
                    String dur = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(vid_dur),
                            TimeUnit.MILLISECONDS.toMinutes(vid_dur) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(vid_dur)), // The change is in this line
                            TimeUnit.MILLISECONDS.toSeconds(vid_dur) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(vid_dur)));
                    current_dur.setText(dur);
                    Log.i("time", String.valueOf(newValue));
                }
            }
        });
        slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video.seekTo((int) TimeUnit.SECONDS.toMillis(slider.getValue()));
            }
        });
        if(getIntent().getExtras()!=null){
            int pos=getIntent().getExtras().getInt("seek");
            int id=getIntent().getExtras().getInt("id");
            if(id==1)
                video_call();
            video.seekTo(pos);
        }
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video.isPlaying()) {
                    video.pause();
                    pause.setImageResource(R.drawable.play);
                } else {
                    video.start();
                    pause.setImageResource(R.drawable.pause);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long dur = TimeUnit.MILLISECONDS.toSeconds(video.getCurrentPosition());
                dur += 10;
                video.seekTo((int) TimeUnit.SECONDS.toMillis(dur));
                video.start();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long dur = TimeUnit.MILLISECONDS.toSeconds(video.getCurrentPosition());
                dur -= 10;
                video.seekTo((int) TimeUnit.SECONDS.toMillis(dur));
                video.start();
            }
        });
    }

    public void video_call()
    {
        if (completedbuffer == false) {
            mProgressDialog.show();
            return;
        }

        if (video.isPlaying()) {
            video.pause();
            fab.setImageResource(R.drawable.play);
            widget(1);
        } else {
            video.start();
            fab.setImageResource(R.drawable.stop);
            widget(0);
        }
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            update();
        }
    };

    public void update() {
        int vid_dur = video.getCurrentPosition();
        float time = TimeUnit.MILLISECONDS.toSeconds(video.getCurrentPosition());
        String dur = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(vid_dur),
                TimeUnit.MILLISECONDS.toMinutes(vid_dur) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(vid_dur)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(vid_dur) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(vid_dur)));
        current_dur.setText(dur);
        seek = 1;
        slider.setValue(time * 10, false);

        handler.postDelayed(run, 1000);

    }

    public void widget(int toggle) {
        if (toggle == 0) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {                // Do something for lollipop and above versions
                int cx, cy;
                cx = widgelayout.getWidth() / 2;
                cy = widgelayout.getHeight() / 2;
                widgelayout.setVisibility(View.VISIBLE);
                float finalRadius = (float) Math.hypot(cx, cy);

// create the animator for this view (the start radius is zero)

                Animator anim =
                        ViewAnimationUtils.createCircularReveal(widgelayout, cx, cy, 0, finalRadius);
                anim.start();


            } else {
                // do something for phones running an SDK before lollipop
                widgelayout.setVisibility(View.VISIBLE);
                final Animation content_in = AnimationUtils.loadAnimation(this, R.anim.content_in);
                widgelayout.startAnimation(content_in);

            }
        }
        if (toggle == 1) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                // Do something for lollipop and above versions
                int cx, cy;
                cx = widgelayout.getWidth() / 2;
                cy = widgelayout.getHeight() / 2;
                float finalRadius = (float) Math.hypot(cx, cy);
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(widgelayout, cx, cy, finalRadius, 0);
                anim.start();
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        widgelayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            } else {
                // do something for phones running an SDK before lollipop
                final Animation content_out = AnimationUtils.loadAnimation(this, R.anim.content_out);
                widgelayout.startAnimation(content_out);
                content_out.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        widgelayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }


    }











    private class History extends AsyncTask<String,Void,Void>{
        Dbdetails dbdetails= new Dbdetails();
        Connection connection;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),"Successfully Updated!!",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String... params) {
            String setid=null;
            try {
                Class.forName(dbdetails.getDriver());
                connection = DriverManager.getConnection(dbdetails.getUrl(), dbdetails.getUserName(), dbdetails.getPass());
                Statement stmt = null;
                stmt = connection.createStatement();
                String sql = "select * from video_alloc where cust_id like '" + user + "'";
                ResultSet rs;
                rs = stmt.executeQuery(sql);
                rs.beforeFirst();
                if (rs.next())
                    setid = rs.getString("set_id");
                sql="delete from history where cust_id like '"+user+"' and video_id like '"+vid+"' and set_id like '"+setid+"';";
                stmt.executeUpdate(sql);
                sql="insert into history values("+null+",'"+user+"','"+vid+"','"+setid+"','"+params[0]+"')";
                stmt.executeUpdate(sql);
        }catch (Exception e){
            e.printStackTrace();
            }
            return null;
        }
    }
    private  class Videodet extends AsyncTask<String, Void, String> {
        Dbdetails dbdetails= new Dbdetails();
        Connection connection;
        String path;
        String exc;
        String name;
        String desc;

        @Override
        protected String doInBackground(String... params) {

            String videopath=new String();
            try {
                Class.forName(dbdetails.getDriver());
                connection = DriverManager.getConnection(dbdetails.getUrl(), dbdetails.getUserName(), dbdetails.getPass());
                Statement stmt = null;
                stmt = connection.createStatement();
                String sql = "select * from video where  video_id like '" + params[0] + "';";
                ResultSet rs;
                rs = stmt.executeQuery(sql);
                if(rs.next())
                {
                 path=rs.getString("path");
                 exc=rs.getString("exceptions");
                 name=rs.getString("name");
                 desc=rs.getString("des");
                }

                    for(int i=0;i<30;i++)
                    {
                        details[i]=new String();
                        column_name[i]=new String();
                    }

                    rs=stmt.executeQuery("select * from health where username like '"+user+"';");
                    rs.beforeFirst();
                    if(rs.next())
                    {
                        details[0] = rs.getString("exercise");
                        details[1] = rs.getString("heart/stoke");
                        details[2] = rs.getString("high_bp");
                        details[3] = rs.getString("low_bp");
                        details[4] = rs.getString("breath");
                        details[5] = rs.getString("b_pain");
                        details[6] = rs.getString("j_pain");
                        details[7] = rs.getString("surgery");
                        details[8] = rs.getString("per_medicine");
                        details[9] = rs.getString("other_med");
                        details[10] = rs.getString("diabetes");
                        details[11] = rs.getString("heart_dis");
                        details[12] = rs.getString("cheast_pain");
                        details[13] = rs.getString("short_breath");
                        details[14] = rs.getString("bone_brk");
                        details[15] = rs.getString("childbirth");
                        details[16] = rs.getString("allergies");
                        details[17] = rs.getString("heart_murmur");
                        details[18] = rs.getString("pneumonia");
                        details[19] = rs.getString("epilepsy");
                        details[20] = rs.getString("tachycardia");
                        details[21] = rs.getString("oedema");
                        details[22] = rs.getString("heart_att");
                        details[23] = rs.getString("rec_surgery");
                        details[24] = rs.getString("palpitations");
                        details[25] = rs.getString("asthma");
                        details[26] = rs.getString("seizure");
                        details[27] = rs.getString("fainting");
                    }
                    column_name= new String[]{"exercise", "heart/stoke", "high_bp", "low_bp", "breath", "b_pain", "j_pain", "surgery", "per_medicine", "other_med", "diabetes", "heart_dis", "cheast_pain","short_breath", "bone_brk", "childbirth", "allergies", "heart_murmur", "pneumonia", "epilepsy", "tachycardia", "oedema", "heart_att", "rec_surgery", "palpitations", "asthma", "seizure", "fainting"};

                    for(int i=0;i<28;i++)
                    {
                        if(details[i].equals("y"))
                            if(exc.contains(column_name[i]))
                                allowed=false;
                    }


                InputStream input = null;
                Log.i("setup", "started");
                HttpURLConnection uconnection= null;
                try {
                    URL url = new URL(ServerPath.path+"AndroidVideo?userid="+user+"&videoid="+params[0]);

                    uconnection = (HttpURLConnection) url.openConnection();
                    uconnection.connect();
                    uconnection.getResponseCode();



                    if (uconnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        System.out.println("Server returned HTTP " + uconnection.getResponseCode()
                                + " " + uconnection.getResponseMessage());
                    }

                    input = uconnection.getInputStream();
                    videopath=ServerPath.path+"Android/"+user+"/"+name+path.substring(path.indexOf("."), path.lastIndexOf("."));
                    char c;
                    String s = new String();
                    while ((c = (char) input.read()) != (char) -1)
                        s += c;


                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(input!=null)
                        try {
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    if (uconnection != null)
                        uconnection.disconnect();
                }


            }catch(Exception e){
                e.printStackTrace();
            }





            return videopath;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            Log.i("path",s);

            namedisp.setText(name);
            descdisp.setText(desc);
            //path="http://192.168.1.120:8080/Gym/Android/Arun/video.mp4";
            inetpath=s;
            Uri uri=Uri.parse(inetpath);

            video.setVideoURI(uri);
            Toast.makeText(getApplicationContext(),allowed.toString(),Toast.LENGTH_SHORT).show();
        }
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
        // as you specify a parent activity in AndroidManifest.maps.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.full_screen){
            Intent intent=new Intent(Display.this,FullscreenVideoView.class);
            intent.putExtra("seek", video.getCurrentPosition());
            intent.putExtra("path",inetpath);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
