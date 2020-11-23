package com.lucamadd.datiitalia;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

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
        public void onCreatePreferences(final Bundle savedInstanceState, String rootKey) {
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
                            "        <a target=\"_blank\" href=\"https://icons8.com/icons/set/up\">Up icon</a> icon by <a target=\"_blank\" href=\"https://icons8.com\">Icons8</a><br><br>" +
                            "        <a target=\"_blank\" href=\"https://icons8.com/icons/set/visible\">Eye icon</a> icon by <a target=\"_blank\" href=\"https://icons8.com\">Icons8</a><br><br>" +
                            "        <a target=\"_blank\" href=\"https://icons8.com/icon/80152/notepad\">Notepad icon by Icons8</a> icon by <a target=\"_blank\" href=\"https://icons8.com\">Icons8</a><br><br>" +
                            "        <a target=\"_blank\" href=\"https://icons8.com/icons/set/closed-eye\">Closed Eye icon</a> icon by <a target=\"_blank\" href=\"https://icons8.com\">Icons8</a><br><br>" +
                            "        <a target=\"_blank\" href=\"https://icons8.it/icons/set/combo-chart\">Grafico combinato icon</a> icon by <a target=\"_blank\" href=\"https://icons8.it\">Icons8</a><br>"))
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
            findPreference("info").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(Html.fromHtml("<ul>" +
                            "<li>Aggiunta la possibilità di vedere il numero di tamponi giornalieri</li>" +
                            "<li>Scheda andamento: aggiunta la possibilità di visualizzare il riepilogo regionale</li>" +
                            "<li>Ora è possibile visualizzare le note giornaliere (se presenti)</li>" +
                            "<li>Risolto un bug che impediva di lasciare un feedback</li>" +
                            "<li>Aggiunta notifica giornaliera</li>" +
                            "<li>N.B. Il servizio di notifica è gestito direttamente da Google. Nel caso di applicazioni " +
                            "presenti sul Play Store, la notifica viene inviata anche se l'app non è in esecuzione. Dati Italia " +
                            "non è presente sul Play Store, per cui si potrebbero verificare ritardi nella ricezione delle " +
                            "notifiche, o in alcuni casi potrebbero non essere ricevute.</li>" +
                            "</ul>"))
                            .setTitle("Changelog");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    ((TextView)dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                    return false;
                }
            });
            findPreference("feedback").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(Intent.ACTION_SENDTO);
                    i.setType("message/rfc822");
                    i.setData(Uri.parse("mailto:"));
                    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"lucamadd.developer@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, "[FEEDBACK] Dati Italia");
                    i.putExtra(Intent.EXTRA_TEXT   , "");
                    try {
                        startActivity(Intent.createChooser(i, "Invia mail con"));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getContext(), "Non sono installate applicazioni compatibili con l'invio di mail.", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
            findPreference("check_updates").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String curr_version = "v0.9b";
                    String server_version = null;
                    try {
                        server_version = getFinalURL("https://github.com/lucamadd/DatiItalia/releases/latest");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    server_version = server_version.substring(52);
                    if (!curr_version.equals(server_version)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        final String finalServer_version = server_version;
                        builder.setMessage(Html.fromHtml("<br>È disponibile un aggiornamento!<br>"))
                                .setTitle("Controllo aggiornamenti").setNeutralButton("Scarica", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getContext(), "Download iniziato" ,Toast.LENGTH_SHORT).show();

                                String url = "https://github.com/lucamadd/DatiItalia/releases/latest/download/Dati.Italia." + finalServer_version + ".apk";
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                                final String nameOfFile = URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url)); //fetching name of file and type from server


                                request.setTitle(nameOfFile);  //set title for notification in status_bar
                                //request.setDescription("Apri la cartella Download per installare la nuova versione");
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);  //flag for if you want to show notification in status or not

                                //String nameOfFile = "YourFileName.pdf";    //if you want to give file_name manually

                                //String nameOfFile = URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url)); //fetching name of file and type from server

                                File f = new File(Environment.getExternalStorageDirectory() + "/Download");       // location, where to download file in external directory
                                if (!f.exists()) {
                                    f.mkdirs();
                                }
                                request.setDestinationInExternalPublicDir("Download", nameOfFile);
                                DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                downloadManager.enqueue(request);
                                BroadcastReceiver onComplete=new BroadcastReceiver() {
                                    public void onReceive(Context ctxt, Intent intent) {
                                        Toast.makeText(ctxt, "Download completato. Installa la nuova versione dalla cartella Download" ,Toast.LENGTH_LONG).show();

                                    }
                                };
                                getContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                            }
                        });
                        ;
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        ((TextView)dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(Html.fromHtml("<br>Nessun aggiornamento disponibile.<br>"))
                                .setTitle("Controllo aggiornamenti");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        ((TextView)dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                    }
                    return true;
                }
            });

        }
    }

    public static String getFinalURL(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setInstanceFollowRedirects(false);
        con.connect();
        con.getInputStream();

        if (con.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
            String redirectUrl = con.getHeaderField("Location");
            return getFinalURL(redirectUrl);
        }
        return url;
    }





    }