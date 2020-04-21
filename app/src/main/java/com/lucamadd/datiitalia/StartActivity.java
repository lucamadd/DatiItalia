package com.lucamadd.datiitalia;

import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.lucamadd.datiitalia.Helper.AndamentoNazionale;
import com.lucamadd.datiitalia.Helper.DataHelper;
import com.lucamadd.datiitalia.ui.main.RegioniFragment;
import com.lucamadd.datiitalia.ui.main.SectionsPagerAdapter;

import java.net.InetAddress;

public class StartActivity extends AppCompatActivity
{

    TabLayout tabs = null;
    ViewPager viewPager = null;

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        /*
        IMPOSTARE LINK PER LE ICONE PREE DA UN SITO ESTERNO
        <a target="_blank" href="https://icons8.com/icons/set/expand-arrow--v1">Expand Arrow icon</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
        <a target="_blank" href="https://icons8.com/icons/set/wifi-off">Wi-Fi off icon</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
        <a target="_blank" href="https://icons8.com/icons/set/checkmark">Checkmark icon</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
        <a target="_blank" href="https://icons8.com/icons/set/available-updates">Available Updates icon</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
        <a target="_blank" href="https://icons8.com/icons/set/settings">Settings icon</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
        <a target="_blank" href="https://icons8.com/icons/set/italy-map">Italy Map icon</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
        <a target="_blank" href="https://icons8.com/icons/set/map-marker">Map Marker icon</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
        <a target="_blank" href="https://icons8.com/icons/set/marker">Marker icon</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
         */
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