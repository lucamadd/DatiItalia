package com.lucamadd.datiitalia;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.TypefaceSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    public static final String
            KEY_PREF_EXAMPLE_SWITCH = "regione";

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
                            "        <a target=\"_blank\" href=\"https://icons8.com/icons/set/marker\">Marker icon</a> icon by <a target=\"_blank\" href=\"https://icons8.com\">Icons8</a><br>"))
                            .setTitle("Crediti");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    ((TextView)dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                    return false;
                }
            });
        }
    }

}