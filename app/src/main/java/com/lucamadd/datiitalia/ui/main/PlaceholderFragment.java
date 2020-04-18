package com.lucamadd.datiitalia.ui.main;

import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lucamadd.datiitalia.Helper.AndamentoNazionale;
import com.lucamadd.datiitalia.R;
import com.lucamadd.datiitalia.StartActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
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
        return root;
    }

    @SuppressWarnings("deprecation")
    public void getData(){
        //final TextView textView = getView().findViewById(R.id.casitotaliitalia);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url =getResources().getString(R.string.andamento_nazionale_latest);

        // Request a string response from the provided URL.
        StringRequest jsonRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null){
                            Log.e("tag",response);
                            JsonParser parser = new JsonParser();
                            JsonArray jArray = (JsonArray) parser.parse(response);
                            Log.e("first element is",jArray.get(0).toString());
                            Gson gson = new Gson();
                            AndamentoNazionale dati = gson.fromJson(jArray.get(0).toString(),
                                    AndamentoNazionale.class);
                            if (dati != null) {
                                setData(dati);
                            }
                        }
                        else {
                            Toast.makeText(getContext(), "Impossibile connettersi al servizio " +
                                    "al momento.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                casi_totali_italia.setText("");
                Toast.makeText(getContext(), "Unable to fetch data: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonRequest);

        RequestQueue queue2 = Volley.newRequestQueue(getContext());
        String url2 =getResources().getString(R.string.andamento_nazionale);

        // Request a string response from the provided URL.
        StringRequest jsonRequest2 = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null){
                            JsonParser parser = new JsonParser();
                            JsonArray jArray = (JsonArray) parser.parse(response);
                            Log.i("log",response);
                            Gson gson = new Gson();
                            /*
                             public AndamentoNazionale(String data, String stato,int ricoverati_con_sintomi, int terapia_intensiva,
                              int totale_ospedalizzati, int isolamento_domiciliare,
                              int totale_positivi, int variazione_totale_positivi,
                              int nuovi_positivi, int dimessi_guariti, int deceduti,
                              int totale_casi, int tamponi, String note_it, String note_en){
                             */
                            AndamentoNazionale datiNuovi = gson.fromJson(jArray.get(jArray.size()-1)
                                    .toString(), AndamentoNazionale.class);
                            AndamentoNazionale datiVecchi = gson.fromJson(jArray.get(jArray.size()-2)
                                    .toString(), AndamentoNazionale.class);
                            setMoreData(new AndamentoNazionale(null,null,
                                    datiNuovi.getRicoverati_con_sintomi() - datiVecchi.getRicoverati_con_sintomi(),
                                    datiNuovi.getTerapia_intensiva() - datiVecchi.getTerapia_intensiva(), datiNuovi.getTotale_ospedalizzati() - datiVecchi.getTotale_ospedalizzati(),
                                    datiNuovi.getIsolamento_domiciliare() - datiVecchi.getIsolamento_domiciliare(), datiNuovi.getTotale_positivi() - datiVecchi.getTotale_positivi(),
                                    0, 0, datiNuovi.getDimessi_guariti() - datiVecchi.getDimessi_guariti(),
                                    datiNuovi.getDeceduti() - datiVecchi.getDeceduti(),0, datiVecchi.getTamponi() - datiNuovi.getTamponi(), null, null));

                        }
                        else {
                            Toast.makeText(getContext(), "Impossibile connettersi al servizio al" +
                                    " momento.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                casi_totali_italia.setText("");
                Toast.makeText(getContext(), "Unable to fetch data: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();

            }
        });
        // Add the request to the RequestQueue.
        queue2.add(jsonRequest2);
    }

    private void setData(AndamentoNazionale dati){
        casi_totali_italia.setText(dati.getTotale_casi() + " casi");
        ricoverati_con_sintomi.setText(dati.getRicoverati_con_sintomi() + "");
        terapia_intensiva.setText(dati.getTerapia_intensiva() + "");
        totale_ospedalizzati.setText(dati.getTotale_ospedalizzati() + "");
        isolamento_domiciliare.setText(dati.getIsolamento_domiciliare() + "");
        totale_attualmente_positivi.setText(dati.getTotale_positivi() + "");
        variazione_totale_positivi.setText("+" + dati.getVariazione_totale_positivi());
        dimessi_guariti.setText(dati.getDimessi_guariti() + "");
        deceduti.setText(dati.getDeceduti() + "");
        tamponi.setText(dati.getTamponi() + "");
        casi_totali_italia_piu.setText("+" + dati.getNuovi_positivi());
    }

    private void setMoreData(AndamentoNazionale nuovo){

        int ricoverati_con_sintomi_piu_ = nuovo.getRicoverati_con_sintomi();
        int terapia_intensiva_piu_ = nuovo.getTerapia_intensiva();
        int totale_ospedalizzati_piu_ = nuovo.getTotale_ospedalizzati();
        int isolamento_domiciliare_piu_ = nuovo.getIsolamento_domiciliare();
        int totale_attualmente_positivi_piu_ = nuovo.getTotale_positivi();
        int dimessi_guariti_piu_ = nuovo.getDimessi_guariti();
        int deceduti_piu_ = nuovo.getDeceduti();
        int tamponi_piu_ = nuovo.getTamponi();

        if (ricoverati_con_sintomi_piu_ > 0){
            ricoverati_con_sintomi_piu.setText("+" + ricoverati_con_sintomi_piu_);
        } else {
            ricoverati_con_sintomi_piu.setText("" + ricoverati_con_sintomi_piu_);
        }
        if (terapia_intensiva_piu_ > 0){
            terapia_intensiva_piu.setText("+" + terapia_intensiva_piu_);
        } else {
            terapia_intensiva_piu.setText("" + terapia_intensiva_piu_);
        }
        if (totale_ospedalizzati_piu_ > 0){
            totale_ospedalizzati_piu.setText("+" + totale_ospedalizzati_piu_);
        } else {
            totale_ospedalizzati_piu.setText("" + totale_ospedalizzati_piu_);
        }
        if (isolamento_domiciliare_piu_ > 0){
            isolamento_domiciliare_piu.setText("+" + isolamento_domiciliare_piu_);
        } else {
            isolamento_domiciliare_piu.setText("" + isolamento_domiciliare_piu_);
        }
        if (totale_attualmente_positivi_piu_ > 0){
            totale_attualmente_positivi_piu.setText("+"  + totale_attualmente_positivi_piu_);
        } else {
            totale_attualmente_positivi_piu.setText("" + totale_attualmente_positivi_piu_);
        }
        if (dimessi_guariti_piu_ > 0){
            dimessi_guariti_piu.setText("+" + dimessi_guariti_piu_);
        } else {
            dimessi_guariti_piu.setText("" + dimessi_guariti_piu_);
        }
        if (deceduti_piu_ > 0){
            deceduti_piu.setText("+" + deceduti_piu_);
        } else {
            deceduti_piu.setText("" + deceduti_piu_);
        }
        if (tamponi_piu_ > 0){
            tamponi_piu.setText("+" + tamponi_piu_);
        } else {
            tamponi_piu.setText("" + tamponi_piu_);
        }
    }

}