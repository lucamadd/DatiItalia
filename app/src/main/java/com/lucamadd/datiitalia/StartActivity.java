package com.lucamadd.datiitalia;


import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lucamadd.datiitalia.Helper.CustomViewPager;
import com.lucamadd.datiitalia.ui.main.SectionsPagerAdapter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class StartActivity extends AppCompatActivity
{

    TabLayout tabs = null;
    CustomViewPager viewPager = null;


    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDarkThemeEnabled= prefs.getBoolean("dark_theme",false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Window window = getWindow();


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        if (isDarkThemeEnabled){
            window.setStatusBarColor(Color.parseColor("#1d1d1d"));
            tabs.setBackgroundColor(Color.parseColor("#292929"));
            tabs.setSelectedTabIndicatorColor(Color.parseColor("#292929"));
            findViewById(R.id.master_coordinator_layout).setBackgroundColor(Color.parseColor("#1d1d1d"));

        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        /*
        //checkUpdates();
        tabs.getTabAt(0).setIcon(R.drawable.nazionale).getIcon().setColorFilter(getResources()
                .getColor(R.color.ic_launcher_background), PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(1).setIcon(R.drawable.regioni).getIcon()
                .setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(2).setIcon(R.drawable.province).getIcon()
                .setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(3).setIcon(R.drawable.graph).getIcon()
                .setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);

         */

        createTabs(0);
        tabs.setTabTextColors(Color.parseColor("#a8a8a8"),getResources().getColor(R.color.ic_launcher_background));
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //tab.getIcon().setColorFilter(getResources().getColor(R.color.ic_launcher_background), PorterDuff.Mode.SRC_IN);
                int position = tab.getPosition();
                createTabs(position);
                viewPager.setPagingEnabled(position != 3);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void createTabs(int position){
        for (int i=0;i<4;i++){
            TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            switch (i){
                case 0:
                    tabOne.setText("Nazionale");
                    break;
                case 1:
                    tabOne.setText("Regioni");
                    break;
                case 2:
                    tabOne.setText("Province");
                    break;
                case 3:
                    tabOne.setText("Andamento");
                    break;
            }
            if (i == position)
                tabOne.setTextColor(getResources().getColor(R.color.ic_launcher_background));
            else
                tabOne.setTextColor(Color.parseColor("#a8a8a8"));
            Drawable img = null;
            switch (i){
                case 0:
                    img = ContextCompat.getDrawable(this, R.drawable.nazionale);
                    break;
                case 1:
                    img = ContextCompat.getDrawable(this, R.drawable.regioni);
                    break;
                case 2:
                    img = ContextCompat.getDrawable(this, R.drawable.province);
                    break;
                case 3:
                    img = ContextCompat.getDrawable(this, R.drawable.graph);
                    break;
            }
            img.setBounds(0, 0, 64, 64);
            if (i == position)
                img.setColorFilter(getResources().getColor(R.color.ic_launcher_background), PorterDuff.Mode.SRC_IN);
            else
                img.setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
            tabOne.setCompoundDrawables(null, img, null, null);
            tabs.getTabAt(i).setCustomView(null);
            tabs.getTabAt(i).setCustomView(tabOne);
        }
        /*
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Nazionale");
        tabOne.setTextColor(getResources().getColor(R.color.ic_launcher_background));
        Drawable img = ContextCompat.getDrawable(this, R.drawable.nazionale);
        img.setBounds(0, 0, 64, 64);
        img.setColorFilter(getResources().getColor(R.color.ic_launcher_background), PorterDuff.Mode.SRC_IN);
        tabOne.setCompoundDrawables(null, img, null, null);
        tabs.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Regioni");
        Drawable img2 = ContextCompat.getDrawable(this, R.drawable.regioni);
        img2.setBounds(0, 0, 64, 64);
        img2.setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        tabTwo.setCompoundDrawables(null, img2, null, null);
        tabs.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Province");
        Drawable img3 = ContextCompat.getDrawable(this, R.drawable.province);
        img3.setBounds(0, 0, 64, 64);
        img3.setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        tabThree.setCompoundDrawables(null, img3, null, null);
        tabs.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText("Andamento");
        Drawable img4 = ContextCompat.getDrawable(this, R.drawable.graph);
        img4.setBounds(0, 0, 64, 64);
        img4.setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        tabFour.setCompoundDrawables(null, img4, null, null);
        tabs.getTabAt(3).setCustomView(tabFour);

         */
    }


}