package com.example.home.gym;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link UserProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfile extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;
    ImageView imageview;
    String name=new String();
    String age=new String();
    String fname=new String();
    String oc=new String();
    String email=new String();
    String contact=new String();
    String gender=new String();
    String text=new String();
    //TextView slide_name,slide_email,mgender,mname,mmail,moccupation,mcontact;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfile newInstance(String param1, String param2) {
        UserProfile fragment = new UserProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UserProfile() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (getArguments() != null) {
            text = getArguments().getString("user");

        }

        DbForHealth dbForHealth=new DbForHealth();
        //Make the image view full blurred with black and white color and set translucent background
        dbForHealth.execute(text);
        imageview = (ImageView) getActivity().findViewById(R.id.profile_back);
        CircleImageView dp = (CircleImageView) getActivity().findViewById(R.id.dp1);
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//Toast.makeText(getApplication(),"hi",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
//second parametre is radius
//        imageview.setImageBitmap(blurred);
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
        finalbitmap.compress(Bitmap.CompressFormat.JPEG, 10, new ByteArrayOutputStream());
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

    @SuppressLint("NewApi")
    private Bitmap blurRenderScript(Bitmap smallBitmap, int radius) {


        smallBitmap.compress(Bitmap.CompressFormat.PNG,10,new ByteArrayOutputStream());
        int width=Math.round(smallBitmap.getWidth()*0.9f);
        int height=Math.round(smallBitmap.getHeight()*0.9f);

        Bitmap bitmap = Bitmap.createBitmap(
                width, height,
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(getActivity().getApplicationContext());

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                ByteArrayOutputStream boas = new ByteArrayOutputStream();


                Bitmap btmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                btmap.compress(Bitmap.CompressFormat.JPEG, 70, boas); //bm is the bitmap object
                byte[] byteArrayImage = boas.toByteArray();


                BitmapFactory.Options opt;

                opt = new BitmapFactory.Options();
                opt.inTempStorage = new byte[16 * 1024];
                opt.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArrayImage, 0, byteArrayImage.length, opt);

                ImageView imageView = (ImageView) getActivity().findViewById(R.id.profile_back);
                CircleImageView dp = (CircleImageView) getActivity().findViewById(R.id.dp1);
                dp.setImageBitmap(bitmap);
                Bitmap bitmap1=grayscale(bitmap);
                Bitmap blurred = blurRenderScript(bitmap1, 25);
                imageView.setImageBitmap(blurred);
            } catch (OutOfMemoryError a) {
                Toast.makeText(getActivity().getApplicationContext(), "Image size high", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    protected class DbForHealth extends AsyncTask<String,Void,Void>
    {

        Connection connection;
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

        }
    }
}
