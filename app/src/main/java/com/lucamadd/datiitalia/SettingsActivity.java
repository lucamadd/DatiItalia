package com.lucamadd.datiitalia;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        TextView tv = new TextView(this);
        tv.setText("Impostazioni");
        tv.setTextSize(28);
        tv.setTextColor(Color.BLACK);




        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setElevation(0);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(tv);
            //actionBar.setWindowTitle();
        }
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDarkThemeEnabled= prefs.getBoolean("dark_theme",false);
        if (isDarkThemeEnabled){
            tv.setTextColor(Color.parseColor("#a8a8a8"));
            tv.setBackgroundColor(Color.parseColor("#1d1d1d"));
            LinearLayout settings = findViewById(R.id.settings_layout);
            settings.setBackgroundColor(Color.parseColor("#1d1d1d"));
            setTheme(R.style.SettingsDarkAppTheme);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1d1d1d")));
            getWindow().setStatusBarColor(Color.parseColor("#1d1d1d"));
            int flags = getWindow().getDecorView().getSystemUiVisibility();
            flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            findPreference("regione").setOnPreferenceChangeListener(new Preference
                    .OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Toast.makeText(getContext(),"Le impostazioni saranno applicate al " +
                            "prossimo riavvio dell'applicazione",Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            findPreference("info_credits").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(Html.fromHtml("<br><a target=\"_blank\" href=\"https://icons8.com/icons/set/expand-arrow--v1\">Expand Arrow icon</a> icon by <a target=\"_blank\" href=\"https://icons8.com\">Icons8</a><br><br>\n" +
                            "        <a target=\"_blank\" href=\"https://icons8.com/icons/set/wifi-off\">Wi-Fi off icon</a> icon by <a target=\"_blank\" href=\"https://icons8.com\">Icons8</a><br><br>\n" +
                            "        <a target=\"_blank\" href=\"https://icons8.com/icons/set/checkmark\">Checkmark icon</a> icon by <a target=\"_blank\" href=\"https://icons8.com\">Icons8</a><br><br>\n" +
                            "        <a target=\"_blank\" href=\"https://icons8.com/icons/set/available-updates\">Available Updates icon</a> icon by <a target=\"_blank\" href=\"https://icons8.com\">Icons8</a><br><br>\n" +
                            "        <a target=\"_blank\" href=\"https://icons8.com/icons/set/settings\">Settings icon</a> icon by <a target=\"_blank\" href=\"https://icons8.com\">Icons8</a><br><br>\n" +
                            "        <a target=\"_blank\" href=\"https://icons8.com/icons/set/italy-map\">Italy Map icon</a> icon by <a target=\"_blank\" href=\"https://icons8.com\">Icons8</a><br><br>\n" +
                            "        <a target=\"_blank\" href=\"https://icons8.com/icons/set/map-marker\">Map Marker icon</a> icon by <a target=\"_blank\" href=\"https://icons8.com\">Icons8</a><br><br>\n" +
                            "        <a target=\"_blank\" href=\"https://icons8.com/icons/set/marker\">Marker icon</a> icon by <a target=\"_blank\" href=\"https://icons8.com\">Icons8</a><br><br>" +
                            "        <a target=\"_blank\" href=\"https://icons8.com/icons/set/up\">Up icon</a> icon by <a target=\"_blank\" href=\"https://icons8.com\">Icons8</a><br>"))
                            .setTitle("Crediti");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    ((TextView)dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                    return false;
                }
            });
            findPreference("dark_theme").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Toast.makeText(getContext(),"Le impostazioni saranno applicate al " +
                            "prossimo riavvio dell'applicazione",Toast.LENGTH_LONG).show();
                    return false;
                }
            });
            findPreference("check_updates").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://github.com/lucamadd/DatiItalia/releases/latest"));
                    startActivity(intent);
                    return true;
                }
            });

        }
    }


}