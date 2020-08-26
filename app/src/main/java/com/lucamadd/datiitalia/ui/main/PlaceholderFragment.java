package com.lucamadd.datiitalia.ui.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.JsonReader;
import android.util.Log;
import android.util.MalformedJsonException;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.lucamadd.datiitalia.Helper.AndamentoNazionale;
import com.lucamadd.datiitalia.Helper.DataHelper;
import com.lucamadd.datiitalia.R;
import com.lucamadd.datiitalia.SettingsActivity;
import com.lucamadd.datiitalia.StartActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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

    private TextView data_italia = null;
    private PageViewModel pageViewModel;

    private LinearLayout firstLayout = null;
    private LinearLayout masterLayout = null;
    private LinearLayout retryLayout = null;
    private ProgressBar italiaProgressBar = null;

    private DecimalFormat decim = new DecimalFormat("#,###");

    private Button settingsButton = null;
    private Button retryButton = null;

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
        else
            settingsButton.getBackground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);

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
        checkUpdates();

        return root;
    }


    private class Connection extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            data.getData();
            data.getMoreData();
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
            tamponi.setText(decim.format(dati.getTamponi()) + "");
            casi_totali_italia_piu.setText("+" + decim.format(dati.getNuovi_positivi()));
        } else {
            italiaProgressBar.setVisibility(View.VISIBLE);
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

            italiaProgressBar.setVisibility(View.GONE);
            firstLayout.removeView(italiaProgressBar);
            masterLayout.setVisibility(View.VISIBLE);
            Log.i("MASTERLAYOUT MADE","VISIBLE");

        } else {
            italiaProgressBar.setVisibility(View.VISIBLE);

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
        String curr_version = "v0.8.4b";
        String server_version = null;
        try {
            server_version = SettingsActivity.getFinalURL("https://github.com/lucamadd/DatiItalia/releases/latest");
        } catch (IOException e) {
            e.printStackTrace();
        }
        server_version = server_version.substring(52);
        if (!curr_version.equals(server_version)){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(Html.fromHtml("<br>È disponibile un aggiornamento! Clicca <a target=\"_blank\" href=\"https://github.com/lucamadd/DatiItalia/releases/latest/download/Dati.Italia." + server_version + ".apk\">qui</a> per aggiornare.<br>"))
                    .setTitle("Controllo aggiornamenti");
            AlertDialog dialog = builder.create();
            dialog.show();
            ((TextView)dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}