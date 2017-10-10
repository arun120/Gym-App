package com.example.home.gym;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Personal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    CollapsingToolbarLayout collapsingToolbarLayout;
    View view;
    String name=new String();
    String age=new String();
    String fname=new String();
    String oc=new String();
    String email=new String();
    String contact=new String();
    String gender=new String();
    String text=new String();
    TextView slide_name,slide_email,mgender,mname,mmail,moccupation,mcontact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        text=getIntent().getExtras().getString("user");
        DbForHealth dbForHealth=new DbForHealth();
        //Make the image view full blurred with black and white color and set translucent background
        dbForHealth.execute(text);
        ImageView imageview = (ImageView) findViewById(R.id.profile_back);
        view=findViewById(R.id.sample_bg);
        slide_name= (TextView) findViewById(R.id.slide_name);
        slide_email= (TextView) findViewById(R.id.slide_mail);
        mname= (TextView) findViewById(R.id.mname);
        mmail= (TextView) findViewById(R.id.mail);
        mgender= (TextView) findViewById(R.id.gender);
        moccupation= (TextView) findViewById(R.id.occupation);
        mcontact= (TextView) findViewById(R.id.contact);
        BitmapDrawable drawable = (BitmapDrawable) imageview.getDrawable();
        Bitmap bitmap =grayscale(drawable.getBitmap()) ;
        Bitmap blurred = blurRenderScript(bitmap, 25);//second parametre is radius
        imageview.setImageBitmap(blurred);
    }
    public Bitmap grayscale(Bitmap src)
    {
        float[] grayscalearray={0.213f, 0.715f, 0.072f, 0.0f, 0.0f,
                0.213f, 0.715f, 0.072f, 0.0f, 0.0f,
                0.213f, 0.715f, 0.072f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f, 0.0f,};
        ColorMatrix colorMatrix=new ColorMatrix(grayscalearray);
        int width=src.getWidth();
        int height=src.getHeight();
        Bitmap finalbitmap=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        ColorMatrixColorFilter matrixColorFilter=new ColorMatrixColorFilter(colorMatrix);
        Canvas canvas=new Canvas(finalbitmap);
        Paint paint=new Paint();
        paint.setColorFilter(matrixColorFilter);
        canvas.drawBitmap(src, 0, 0, paint);
        finalbitmap.compress(Bitmap.CompressFormat.JPEG,10,new ByteArrayOutputStream());
        return finalbitmap;

    }
    private int getColorWithAplha(int color, float ratio)
    {
        int transColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        transColor = Color.argb(alpha, r, g, b);
        return transColor ;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressLint("NewApi")
    private Bitmap blurRenderScript(Bitmap smallBitmap, int radius) {


        smallBitmap.compress(Bitmap.CompressFormat.PNG,10,new ByteArrayOutputStream());
        int width=Math.round(smallBitmap.getWidth()*0.9f);
        int height=Math.round(smallBitmap.getHeight()*0.9f);

        Bitmap bitmap = Bitmap.createBitmap(
                width, height,
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(getApplicationContext());

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(25.0f); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);
        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_up_page, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected class DbForHealth extends AsyncTask<String,Void,Void>
    {

        Connection  connection;
        Statement statement;
        ResultSet resultSet;
        Dbdetails dbdetails=new Dbdetails();
        @Override
        protected Void doInBackground(String... params) {
            try
            {
             Class.forName(dbdetails.getDriver());
                connection= DriverManager.getConnection(dbdetails.getUrl(), dbdetails.getUserName(), dbdetails.getPass());
                String sql="select * from personal where username like '"+params[0]+"';";
                statement=connection.createStatement();
                resultSet=statement.executeQuery(sql);
                resultSet.beforeFirst();
                if(resultSet.next())
                {
                        name=resultSet.getString("name");
                        age= resultSet.getString("age");
                        fname=resultSet.getString("f_s_name");
                        oc=resultSet.getString("oc_ds");
                        email=resultSet.getString("email_id");
                        contact=resultSet.getString("contact_no");
                        gender=resultSet.getString("gender");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mname.setText(name + ',' + age);
//            slide_name.setText(name);
  //          slide_email.setText(email);
            mmail.setText(email);
            moccupation.setText(oc);
            mgender.setText(gender);
            mcontact.setText(contact);

           /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getApplicationContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Personal.this, FragmentManagement.class);
                    intent.putExtra("user", text);
                    startActivity(intent);
                }
            });*/
        }
    }
}
