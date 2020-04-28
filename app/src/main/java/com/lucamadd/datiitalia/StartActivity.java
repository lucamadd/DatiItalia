package com.lucamadd.datiitalia;


import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.lucamadd.datiitalia.ui.main.SectionsPagerAdapter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class StartActivity extends AppCompatActivity
{

    TabLayout tabs = null;
    ViewPager viewPager = null;


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
        viewPager.setOffscreenPageLimit(2);
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

        //checkUpdates();
        tabs.getTabAt(0).setIcon(R.drawable.nazionale).getIcon().setColorFilter(getResources()
                .getColor(R.color.ic_launcher_background), PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(1).setIcon(R.drawable.regioni).getIcon()
                .setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(2).setIcon(R.drawable.province).getIcon()
                .setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);

        tabs.setTabTextColors(Color.parseColor("#a8a8a8"),getResources().getColor(R.color.ic_launcher_background));
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.ic_launcher_background), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }




}