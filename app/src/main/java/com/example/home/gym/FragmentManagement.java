package com.example.home.gym;

import android.content.Context;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.Window;

public class FragmentManagement extends AppCompatActivity  {

    String userid = null;
    Fragment fragmentnote;
    FragmentPagerAdapter adapterViewPager;
    Bundle args;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
        setContentView(R.layout.activity_fragment_management);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userid = getIntent().getExtras().getString("user");



        ViewPager mViewPager;
        mViewPager = (ViewPager) findViewById(R.id.pager);


        args = new Bundle();
        args.putString("user", userid);


        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), args, FragmentManagement.this);
        mViewPager.setAdapter(adapterViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }



    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private final int NUM_ITEMS = 3;
        Bundle args;
        private Context context;

        public MyPagerAdapter(FragmentManager fragmentManager, Bundle a, Context context) {

            super(fragmentManager);
            this.context = context;
            args = a;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            android.support.v4.app.Fragment fragment;

            switch (position) {

                case 0:
                    fragment=new UserProfile();
                    break;

                case 1:                 // Fragment # 0 - This will show FirstFragment

                    fragment = new Video_alloc();
                    break;
                case 2:
                    fragment = new Others();
                    break;

                default:
                    return null;
            }
            fragment.setArguments(args);

            return fragment;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {


                case 0:
                    return "Profile";
                case 1:
                    return "Daily Routine";

                case 2:
                    return "Others";



                default:
                    return null;
            }
        }


    }
}
