package com.lucamadd.datiitalia.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
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
import com.lucamadd.datiitalia.Helper.VolleyCallBack;
import com.lucamadd.datiitalia.R;
import com.lucamadd.datiitalia.SettingsActivity;
import com.lucamadd.datiitalia.StartActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    private TextView tamponi_piu = null;
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

        retryLayout = root.findViewById(R.id.retry_layout);

        Button retryButton = root.findViewById(R.id.retry_button);
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
        if (isOnline()){
            new Connection().execute();

        } else {
            italiaProgressBar.setVisibility(View.GONE);
            retryLayout.setVisibility(View.VISIBLE);
        }
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
}