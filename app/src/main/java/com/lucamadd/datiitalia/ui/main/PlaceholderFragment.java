package com.lucamadd.datiitalia.ui.main;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


import com.lucamadd.datiitalia.Helper.AndamentoNazionale;
import com.lucamadd.datiitalia.Helper.DataHelper;
import com.lucamadd.datiitalia.Helper.Measurement;
import com.lucamadd.datiitalia.R;
import com.lucamadd.datiitalia.SettingsActivity;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import static android.content.Context.MODE_PRIVATE;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView casi_totali_italia = null;
    private TextView ricoverati_con_sintomi = null;
    private TextView terapia_intensiva = null;
    private TextView totale_ospedalizzati = null;
    private TextView isolamento_domiciliare = null;
    private TextView totale_attualmente_positivi = null;
    private TextView variazione_totale_positivi = null;
    private TextView dimessi_guariti = null;
    private TextView deceduti = null;
    private TextView tamponi = null;

    private TextView casi_totali_italia_piu = null;
    private TextView ricoverati_con_sintomi_piu = null;
    private TextView terapia_intensiva_piu = null;
    private TextView totale_ospedalizzati_piu = null;
    private TextView isolamento_domiciliare_piu = null;
    private TextView totale_attualmente_positivi_piu = null;
    private TextView dimessi_guariti_piu = null;
    private TextView deceduti_piu = null;
    private TextView tamponi_piu = null;

    private TextView data_italia = null;
    private PageViewModel pageViewModel;

    private LinearLayout firstLayout = null;
    private LinearLayout masterLayout = null;
    private LinearLayout retryLayout = null;
    private ProgressBar italiaProgressBar = null;

    private DecimalFormat decim = new DecimalFormat("#,###");

    private Button settingsButton = null;
    private Button retryButton = null;
    private Button notesButton = null;

    private AndamentoNazionale datiNazionali = null;
    private AndamentoNazionale variazioneDatiNazionali = null;

    private DataHelper data;

    private boolean isDarkThemeEnabled;



    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index",index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getContext());
        isDarkThemeEnabled = prefs.getBoolean("dark_theme",false);
        super.onCreate(savedInstanceState);

        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);

        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_start, container, false);

        notesButton = root.findViewById(R.id.notes_button);
        settingsButton = root.findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SettingsActivity.class);
                startActivity(i);
            }
        });


        masterLayout = root.findViewById(R.id.master_layout);
        italiaProgressBar = root.findViewById(R.id.italia_progress_bar);
        firstLayout = root.findViewById(R.id.first_layout);

        if (isDarkThemeEnabled)
            setDarkTheme(root);
        else{
            settingsButton.getBackground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
            notesButton.setBackground(getResources().getDrawable(R.drawable.notes));
        }


        retryLayout = root.findViewById(R.id.retry_layout);

        Button retryButton = root.findViewById(R.id.retry_button);
        Button networkIcon = root.findViewById(R.id.network_icon);

        if (isDarkThemeEnabled){
            retryButton.setBackgroundColor(Color.parseColor("#292929"));
            retryButton.setTextColor(Color.parseColor("#a8a8a8"));
            networkIcon.getBackground().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
            TextView retry_TV = root.findViewById(R.id.italia_tv_retry);
            retry_TV.setTextColor(Color.parseColor("#a8a8a8"));
        } else {
            networkIcon.getBackground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
        }
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retryLayout.setVisibility(View.GONE);
                italiaProgressBar.setVisibility(View.VISIBLE);
                if (isOnline()){
                    new Connection().execute();
                } else {
                    italiaProgressBar.setVisibility(View.GONE);
                    retryLayout.setVisibility(View.VISIBLE);
                }
            }
        });



        masterLayout.setVisibility(View.GONE);

        casi_totali_italia  = root.findViewById(R.id.casitotaliitalia);
        ricoverati_con_sintomi = root.findViewById(R.id.ricoveraticonsintomiitalia);
        terapia_intensiva = root.findViewById(R.id.terapiaintensivaitalia);
        totale_ospedalizzati = root.findViewById(R.id.totaleospedalizzatiitalia);
        isolamento_domiciliare = root.findViewById(R.id.isolamentodomiciliareitalia);
        totale_attualmente_positivi = root.findViewById(R.id.totaleattualmentepositiviitalia);
        variazione_totale_positivi = root.findViewById(R.id.totaleattualmentepositiviitaliapiu);
        dimessi_guariti = root.findViewById(R.id.dimessiguaritiitalia);
        deceduti = root.findViewById(R.id.decedutiitalia);
        tamponi = root.findViewById(R.id.tamponiitalia);

        casi_totali_italia_piu = root.findViewById(R.id.casitotaliitaliapiu);
        ricoverati_con_sintomi_piu = root.findViewById(R.id.ricoveraticonsintomiitaliapiu);
        terapia_intensiva_piu = root.findViewById(R.id.terapiaintensivaitaliapiu);
        totale_ospedalizzati_piu = root.findViewById(R.id.totaleospedalizzatiitaliapiu);
        isolamento_domiciliare_piu = root.findViewById(R.id.isolamentodomiciliareitaliapiu);
        totale_attualmente_positivi_piu = root.findViewById(R.id.totaleattualmentepositiviitaliapiu);
        dimessi_guariti_piu = root.findViewById(R.id.dimessiguaritiitaliapiu);
        deceduti_piu = root.findViewById(R.id.decedutiitaliapiu);
        tamponi_piu = root.findViewById(R.id.tamponiitaliapiu);
        /*
        final TextView textView = root.findViewById(R.id.section_label);
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
         */

        data = new DataHelper();
        data_italia = root.findViewById(R.id.data_italia);
        checkFirstRun();
        checkUpdates();
        return root;
    }


    private class Connection extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            data.getData();
            data.getMoreData();
            data.getNoteData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("onPostExecute()","iniziato");
            datiNazionali = data.getDatiNazionali();
            setData(datiNazionali);
            variazioneDatiNazionali = data.getVariazioneDatiNazionali();
            setMoreData(variazioneDatiNazionali);
            data_italia.setText(data.getCurrentDayText());
            if (data.checkNote(data.getNoteGiornaliere())){
                if (isDarkThemeEnabled)
                    notesButton.setBackground(getResources().getDrawable(R.drawable.notes_alert_dark));
                else
                    notesButton.setBackground(getResources().getDrawable(R.drawable.notes_alert));
                notesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder;
                        if (isDarkThemeEnabled)
                            builder = new AlertDialog.Builder(getActivity(),R.style.DarkAlertDialogStyle);
                        else
                            builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(Html.fromHtml("<p>" + data.getNoteGiornaliere().getNote() + "<p>"))
                                .setTitle("Note");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            } else
                notesButton.setEnabled(false);
            Log.i("onPostExecute()","terminato");

        }
    }


    private void setData(AndamentoNazionale dati){
        if (dati != null){
            casi_totali_italia.setText(decim.format(dati.getTotale_casi()) + " casi");
            ricoverati_con_sintomi.setText(decim.format(dati.getRicoverati_con_sintomi()) + "");
            terapia_intensiva.setText(decim.format(dati.getTerapia_intensiva()) + "");
            totale_ospedalizzati.setText(decim.format(dati.getTotale_ospedalizzati()) + "");
            isolamento_domiciliare.setText(decim.format(dati.getIsolamento_domiciliare()) + "");
            totale_attualmente_positivi.setText(decim.format(dati.getTotale_positivi()) + "");
            variazione_totale_positivi.setText("+" + decim.format(dati.getVariazione_totale_positivi()));
            dimessi_guariti.setText(decim.format(dati.getDimessi_guariti()) + "");
            deceduti.setText(decim.format(dati.getDeceduti()) + "");
            //tamponi.setText(decim.format(dati.getTamponi()) + "");
            tamponi.setText(new Measurement(dati.getTamponi()).toString());
            casi_totali_italia_piu.setText("+" + decim.format(dati.getNuovi_positivi()));
        } else {
            italiaProgressBar.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "C'è stato un errore nella ricezione dei dati. " +
                    "Contatta lo sviluppatore per maggiori informazioni.", Toast.LENGTH_LONG);
        }
    }

    private void setMoreData(AndamentoNazionale nuovo){
        if (nuovo != null){
            int ricoverati_con_sintomi_piu_ = nuovo.getRicoverati_con_sintomi();
            int terapia_intensiva_piu_ = nuovo.getTerapia_intensiva();
            int totale_ospedalizzati_piu_ = nuovo.getTotale_ospedalizzati();
            int isolamento_domiciliare_piu_ = nuovo.getIsolamento_domiciliare();
            int totale_attualmente_positivi_piu_ = nuovo.getTotale_positivi();
            int dimessi_guariti_piu_ = nuovo.getDimessi_guariti();
            int deceduti_piu_ = nuovo.getDeceduti();
            int tamponi_piu_ = nuovo.getTamponi();

            if (ricoverati_con_sintomi_piu_ > 0){
                ricoverati_con_sintomi_piu.setText("+" + decim.format(ricoverati_con_sintomi_piu_));
                ricoverati_con_sintomi_piu.setTextColor(Color.RED);
            } else {
                ricoverati_con_sintomi_piu.setText(decim.format(ricoverati_con_sintomi_piu_));
                ricoverati_con_sintomi_piu.setTextColor(Color.rgb(0,153,51));
            }
            if (terapia_intensiva_piu_ > 0){
                terapia_intensiva_piu.setText("+" + decim.format(terapia_intensiva_piu_));
                terapia_intensiva_piu.setTextColor(Color.RED);
            } else {
                terapia_intensiva_piu.setText("" + decim.format(terapia_intensiva_piu_));
                terapia_intensiva_piu.setTextColor(Color.rgb(0,153,51));
            }
            if (totale_ospedalizzati_piu_ > 0){
                totale_ospedalizzati_piu.setText("+" + decim.format(totale_ospedalizzati_piu_));
                totale_ospedalizzati_piu.setTextColor(Color.RED);
            } else {
                totale_ospedalizzati_piu.setText("" + decim.format(totale_ospedalizzati_piu_));
                totale_ospedalizzati_piu.setTextColor(Color.rgb(0,153,51));
            }
            if (isolamento_domiciliare_piu_ > 0){
                isolamento_domiciliare_piu.setText("+" + decim.format(isolamento_domiciliare_piu_));
                isolamento_domiciliare_piu.setTextColor(Color.RED);
            } else {
                isolamento_domiciliare_piu.setText("" + decim.format(isolamento_domiciliare_piu_));
                isolamento_domiciliare_piu.setTextColor(Color.rgb(0,153,51));
            }
            if (totale_attualmente_positivi_piu_ > 0){
                totale_attualmente_positivi_piu.setText("+"  + decim.format(totale_attualmente_positivi_piu_));
                totale_attualmente_positivi_piu.setTextColor(Color.RED);
            } else {
                totale_attualmente_positivi_piu.setText("" + decim.format(totale_attualmente_positivi_piu_));
                totale_attualmente_positivi_piu.setTextColor(Color.rgb(0,153,51));
            }
            if (dimessi_guariti_piu_ > 0){
                dimessi_guariti_piu.setText("+" + decim.format(dimessi_guariti_piu_));
                dimessi_guariti_piu.setTextColor(Color.rgb(0,153,51));
            } else {
                dimessi_guariti_piu.setText("" + decim.format(dimessi_guariti_piu_));
                dimessi_guariti_piu.setTextColor(Color.RED);
            }
            if (deceduti_piu_ > 0){
                deceduti_piu.setText("+" + decim.format(deceduti_piu_));
                deceduti_piu.setTextColor(Color.RED);
            } else {
                deceduti_piu.setText("" + decim.format(deceduti_piu_));
                deceduti_piu.setTextColor(Color.rgb(0,153,51));
            }
            if (tamponi_piu_ == 0){
                tamponi_piu.setText("0");
            } else {
                tamponi_piu.setText("+" + decim.format(tamponi_piu_).substring(1));
            }

            italiaProgressBar.setVisibility(View.GONE);
            firstLayout.removeView(italiaProgressBar);
            masterLayout.setVisibility(View.VISIBLE);
            Log.i("MASTERLAYOUT MADE","VISIBLE");

        } else {
            italiaProgressBar.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "C'è stato un errore nella ricezione dei dati. " +
                    "Contatta lo sviluppatore per maggiori informazioni.", Toast.LENGTH_LONG);
        }
    }

    protected boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isOnline()){
            new Connection().execute();

        } else {
            italiaProgressBar.setVisibility(View.GONE);
            retryLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setDarkTheme(View root){
        firstLayout.setBackgroundColor(Color.parseColor("#1d1d1d"));
        Button settingsButton = root.findViewById(R.id.settings_button);
        settingsButton.getBackground().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        Button notesButton = root.findViewById(R.id.notes_button);
        notesButton.setBackground(getResources().getDrawable(R.drawable.notes_alert_dark));
        TextView italiaTVTitle = root.findViewById(R.id.italia_tv_title);
        italiaTVTitle.setTextColor(Color.parseColor("#a8a8a8"));
        CardView italia_rl1 = root.findViewById(R.id.italia_rl1);
        italia_rl1.setCardBackgroundColor(Color.parseColor("#292929"));
        italia_rl1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));
        CardView italia_rl2 = root.findViewById(R.id.italia_rl2);
        italia_rl2.setCardBackgroundColor(Color.parseColor("#292929"));
        italia_rl2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));
        CardView italia_rl3 = root.findViewById(R.id.italia_rl3);
        italia_rl3.setCardBackgroundColor(Color.parseColor("#292929"));
        italia_rl3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));

        CardView italia_rl4 = root.findViewById(R.id.italia_rl4);
        italia_rl4.setCardBackgroundColor(Color.parseColor("#292929"));
        italia_rl4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));

        CardView italia_rl5 = root.findViewById(R.id.italia_rl5);
        italia_rl5.setCardBackgroundColor(Color.parseColor("#292929"));
        italia_rl5.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));

        CardView italia_rl6 = root.findViewById(R.id.italia_rl6);
        italia_rl6.setCardBackgroundColor(Color.parseColor("#292929"));
        italia_rl6.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));

        CardView italia_rl7 = root.findViewById(R.id.italia_rl7);
        italia_rl7.setCardBackgroundColor(Color.parseColor("#292929"));
        italia_rl7.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));

        CardView italia_rl8 = root.findViewById(R.id.italia_rl8);
        italia_rl8.setCardBackgroundColor(Color.parseColor("#292929"));
        italia_rl8.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));

        TextView ricoveratiConSintomi1 = root.findViewById(R.id.ricoveraticonsintomiitalia_text);
        ricoveratiConSintomi1.setTextColor(Color.parseColor("#a8a8a8"));

        TextView ricoveratiConSintomi2 = root.findViewById(R.id.ricoveraticonsintomiitalia);
        ricoveratiConSintomi2.setTextColor(Color.parseColor("#a8a8a8"));

        TextView terapiaIntensiva1 = root.findViewById(R.id.terapiaintensivaitalia_text);
        terapiaIntensiva1.setTextColor(Color.parseColor("#a8a8a8"));

        TextView terapiaIntensiva2 = root.findViewById(R.id.terapiaintensivaitalia);
        terapiaIntensiva2.setTextColor(Color.parseColor("#a8a8a8"));

        TextView totaleOspedalizzati1 = root.findViewById(R.id.totaleospedalizzatiitalia_text);
        totaleOspedalizzati1.setTextColor(Color.parseColor("#a8a8a8"));

        TextView totaleOspedalizzati2 = root.findViewById(R.id.totaleospedalizzatiitalia);
        totaleOspedalizzati2.setTextColor(Color.parseColor("#a8a8a8"));

        TextView isolamentoDomiciliare1 = root.findViewById(R.id.isolamentodomiciliareitalia_text);
        isolamentoDomiciliare1.setTextColor(Color.parseColor("#a8a8a8"));

        TextView isolamentoDomiciliare2 = root.findViewById(R.id.isolamentodomiciliareitalia);
        isolamentoDomiciliare2.setTextColor(Color.parseColor("#a8a8a8"));

        TextView dimessiGuariti1 = root.findViewById(R.id.dimessiguaritiitalia_text);
        dimessiGuariti1.setTextColor(Color.parseColor("#a8a8a8"));

        TextView dimessiGuariti2 = root.findViewById(R.id.dimessiguaritiitalia);
        dimessiGuariti2.setTextColor(Color.parseColor("#a8a8a8"));

        TextView deceduti1 = root.findViewById(R.id.decedutiitalia_text);
        deceduti1.setTextColor(Color.parseColor("#a8a8a8"));

        TextView deceduti2 = root.findViewById(R.id.decedutiitalia);
        deceduti2.setTextColor(Color.parseColor("#a8a8a8"));

        TextView totaleAttualmentePositivi1 = root.findViewById(R.id.totaleattualmentepositiviitalia_text);
        totaleAttualmentePositivi1.setTextColor(Color.parseColor("#a8a8a8"));

        TextView totaleAttualmentePositivi2 = root.findViewById(R.id.totaleattualmentepositiviitalia);
        totaleAttualmentePositivi2.setTextColor(Color.parseColor("#a8a8a8"));

        TextView tamponi1 = root.findViewById(R.id.tamponiitalia_text);
        tamponi1.setTextColor(Color.parseColor("#a8a8a8"));

        TextView tamponi2 = root.findViewById(R.id.tamponiitalia);
        tamponi2.setTextColor(Color.parseColor("#a8a8a8"));

        /*
        TextView italiaTVDataItalia = root.findViewById(R.id.data_italia);
        italiaTVDataItalia.setTextColor(Color.parseColor("#a8a8a8"));
        TextView italiaTVCasiTotaliItalia = root.findViewById(R.id.casitotaliitalia);
        italiaTVCasiTotaliItalia.setTextColor(Color.parseColor("#a8a8a8"));
        TextView italiaTVCasiTotaliItaliaPiu = root.findViewById(R.id.casitotaliitaliapiu);
        italiaTVCasiTotaliItaliaPiu.setTextColor(Color.parseColor("#a8a8a8"));

         */
    }

    public void checkUpdates(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String curr_version = "v0.11b";
        String server_version = null;
        try {
            server_version = SettingsActivity.getFinalURL("https://github.com/lucamadd/DatiItalia/releases/latest");
        } catch (IOException e) {
            e.printStackTrace();
        }
        server_version = server_version.substring(52);
        Log.i("serverversion", server_version);
        if (!curr_version.equals(server_version)){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final String finalServer_version = server_version;
            builder.setMessage(Html.fromHtml("<br>È disponibile un aggiornamento!"))
                    .setTitle("Controllo aggiornamenti").setNeutralButton("Scarica", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startDownload(finalServer_version);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            ((TextView)dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public void startDownload(String server_version) {
        Toast.makeText(getContext(), "Download iniziato" ,Toast.LENGTH_SHORT).show();

        String url = "https://github.com/lucamadd/DatiItalia/releases/latest/download/Dati.Italia." + server_version + ".apk";
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

    public void checkFirstRun() {
        boolean isFirstRun = getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun){
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
                    .setTitle("Novità!");
            AlertDialog dialog = builder.create();
            dialog.show();

            getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }


}