package com.lucamadd.datiitalia.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.lucamadd.datiitalia.Helper.AndamentoRegionale;
import com.lucamadd.datiitalia.Helper.DataHelper;
import com.lucamadd.datiitalia.R;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegioniFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView casi_totali_regioni = null;
    private TextView ricoverati_con_sintomi = null;
    private TextView terapia_intensiva = null;
    private TextView totale_ospedalizzati = null;
    private TextView isolamento_domiciliare = null;
    private TextView totale_attualmente_positivi = null;
    private TextView variazione_totale_positivi = null;
    private TextView dimessi_guariti = null;
    private TextView deceduti = null;
    private TextView tamponi = null;

    private TextView casi_totali_regioni_piu = null;
    private TextView ricoverati_con_sintomi_piu = null;
    private TextView terapia_intensiva_piu = null;
    private TextView totale_ospedalizzati_piu = null;
    private TextView isolamento_domiciliare_piu = null;
    private TextView totale_attualmente_positivi_piu = null;
    private TextView dimessi_guariti_piu = null;
    private TextView deceduti_piu = null;

    private TextView data_regioni = null;
    private PageViewModel pageViewModel;

    private LinearLayout firstLayout = null;
    private LinearLayout masterLayout = null;
    private LinearLayout retryLayout = null;
    private ProgressBar regioniProgressBar = null;

    private DecimalFormat decim = new DecimalFormat("#,###");

    private Button regioniEditButton = null;
    private Button retryButton = null;

    private ArrayList<AndamentoRegionale> datiRegionali = null;
    private ArrayList<AndamentoRegionale> variazioneDatiRegionali = null;

    private final String[] selectedRegion = {""};
    private TextView regioneTextView = null;
    private Spinner spinner = null;
    private TextView spinnerTextView = null;

    private DataHelper data;
    private boolean isDarkThemeEnabled;


    public static RegioniFragment newInstance(int index) {
        RegioniFragment fragment = new RegioniFragment();
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

        View root = inflater.inflate(R.layout.fragment_regioni, container, false);



        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getContext());
        String favRegion= prefs.getString("regione","");
        selectedRegion[0] = favRegion;

        spinner = root.findViewById(R.id.spinner_regioni);

        ArrayAdapter<String> adapter;
        if (isDarkThemeEnabled){
             adapter = new ArrayAdapter<>(
                    getContext(),
                    R.layout.custom_spinner_dark,
                    getResources().getStringArray(R.array.regioni_entries));
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_dark);

        } else {
            adapter = new ArrayAdapter<>(
                    getContext(),
                    R.layout.custom_spinner,
                    getResources().getStringArray(R.array.regioni_entries));
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);

        }
        spinner.setAdapter(adapter);
        spinner.setScrollBarSize(0);
        spinner.setEnabled(false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = spinner.getItemAtPosition(position).toString();
                if (regioneTextView != null && position > 0){
                    regioneTextView.setText(selectedItem);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        regioniProgressBar = root.findViewById(R.id.regioni_progress_bar);

        regioniProgressBar.setVisibility(View.VISIBLE);

        masterLayout = root.findViewById(R.id.regioni_master_layout);
        firstLayout = root.findViewById(R.id.regioni_first_layout);
        retryLayout = root.findViewById(R.id.regioni_retry_layout);


        data_regioni = root.findViewById(R.id.data_regioni);

        final Vibrator vibe = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        final RelativeLayout hintLayout = root.findViewById(R.id.hint_layout_regioni);

        regioneTextView = root.findViewById(R.id.regione_textview);
        regioniEditButton = root.findViewById(R.id.regioni_edit_button);
        regioniEditButton.setEnabled(false);
        regioniEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(20);
                //se è loading
                if (regioniEditButton.getBackground().getConstantState() == getResources().getDrawable(R.drawable.edit).getConstantState()){
                    regioniEditButton.setBackground(getResources().getDrawable(R.drawable.ok));
                    if (isDarkThemeEnabled)
                        regioniEditButton.getBackground().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
                    spinner.setEnabled(true);
                    spinner.performClick();
                    masterLayout.setVisibility(View.GONE);
                    hintLayout.setVisibility(View.VISIBLE);
                } else {
                    //se è ok
                    if (!regioneTextView.getText().toString().equals("Scegli una regione")){
                        hintLayout.setVisibility(View.GONE);
                        regioniProgressBar.setVisibility(View.VISIBLE);
                        regioniEditButton.setEnabled(false);
                        masterLayout.setVisibility(View.GONE);

                        regioneTextView.setVisibility(View.GONE);
                        spinner.setVisibility(View.VISIBLE);
                        spinner.setEnabled(false);
                        regioniEditButton.setBackground(getResources().getDrawable(R.drawable.edit));
                        selectedRegion[0] = spinner.getSelectedItem().toString();
                        tryConnection();
                    } else {
                        regioniEditButton.setBackground(getResources().getDrawable(R.drawable.edit));
                    }
                }
            }
        });

        if (isDarkThemeEnabled)
            setDarkTheme(root);
        else
            regioniEditButton.getBackground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);


        retryButton = root.findViewById(R.id.regioni_retry_button);
        if (isDarkThemeEnabled){
            retryButton.setBackgroundColor(Color.parseColor("#292929"));
            retryButton.setTextColor(Color.parseColor("#a8a8a8"));
        }
        Button retryButton = root.findViewById(R.id.regioni_retry_button);
        Button networkIcon = root.findViewById(R.id.regioni_network_icon);

        if (isDarkThemeEnabled){
            retryButton.setBackgroundColor(Color.parseColor("#292929"));
            retryButton.setTextColor(Color.parseColor("#a8a8a8"));
            networkIcon.getBackground().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
            TextView retry_TV = root.findViewById(R.id.regioni_tv_retry);
            retry_TV.setTextColor(Color.parseColor("#a8a8a8"));
        } else {
            networkIcon.getBackground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
        }
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retryLayout.setVisibility(View.GONE);
                regioniProgressBar.setVisibility(View.VISIBLE);
                regioniEditButton.setEnabled(false);
                masterLayout.setVisibility(View.GONE);

                if (isOnline()){
                    new Connection().execute();
                } else {
                    regioniProgressBar.setVisibility(View.GONE);
                    regioniEditButton.setEnabled(true);

                    retryLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        masterLayout.setVisibility(View.GONE);

        casi_totali_regioni  = root.findViewById(R.id.casitotaliregioni);
        ricoverati_con_sintomi = root.findViewById(R.id.ricoveraticonsintomiregioni);
        terapia_intensiva = root.findViewById(R.id.terapiaintensivaregioni);
        totale_ospedalizzati = root.findViewById(R.id.totaleospedalizzatiregioni);
        isolamento_domiciliare = root.findViewById(R.id.isolamentodomiciliareregioni);
        totale_attualmente_positivi = root.findViewById(R.id.totaleattualmentepositiviregioni);
        variazione_totale_positivi = root.findViewById(R.id.totaleattualmentepositiviregionipiu);
        dimessi_guariti = root.findViewById(R.id.dimessiguaritiregioni);
        deceduti = root.findViewById(R.id.decedutiregioni);
        tamponi = root.findViewById(R.id.tamponiregioni);

        casi_totali_regioni_piu = root.findViewById(R.id.casitotaliregionipiu);
        ricoverati_con_sintomi_piu = root.findViewById(R.id.ricoveraticonsintomiregionipiu);
        terapia_intensiva_piu = root.findViewById(R.id.terapiaintensivaregionipiu);
        totale_ospedalizzati_piu = root.findViewById(R.id.totaleospedalizzatiregionipiu);
        isolamento_domiciliare_piu = root.findViewById(R.id.isolamentodomiciliareregionipiu);
        totale_attualmente_positivi_piu = root.findViewById(R.id.totaleattualmentepositiviregionipiu);
        dimessi_guariti_piu = root.findViewById(R.id.dimessiguaritiregionipiu);
        deceduti_piu = root.findViewById(R.id.decedutiregionipiu);
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

    private class Connection extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            data.getRegioniDataLatest();
            data.getMoreRegioniData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            datiRegionali = data.getDatiRegionali();
            variazioneDatiRegionali = data.getVariazioneDatiRegionali();
            setData(datiRegionali,selectedRegion[0]);
            setMoreData(variazioneDatiRegionali,selectedRegion[0]);
            data_regioni.setText(data.getCurrentDayText());
        }
    }

    private void tryConnection(){
        retryLayout.setVisibility(View.GONE);
        //Log.i("SELECTED REGION IS ", selectedRegion[0] + " DDD");
        if (!selectedRegion[0].equals("")){
            if (isOnline()){
                new Connection().execute();


            } else {
                regioniProgressBar.setVisibility(View.GONE);
                regioniEditButton.setEnabled(true);

                masterLayout.setVisibility(View.GONE);
                retryLayout.setVisibility(View.VISIBLE);

            }
        } else {
            regioniProgressBar.setVisibility(View.GONE);
            regioniEditButton.setEnabled(true);

            spinner.setVisibility(View.INVISIBLE);
            regioneTextView.setVisibility(View.VISIBLE);
        }


    }


    private void setData(ArrayList<AndamentoRegionale> dati, String regione){
/*
        Log.i("DEBUG", "\ncodice_regione: " + selectedRegion.getCodice_regione()+
                "\ndenominazione_regione: "+selectedRegion.getDenominazione_regione()+
                "\nricoverati con sintomi: "+selectedRegion.getRicoverati_con_sintomi()+
                "\nterapia intensiva: "+selectedRegion.getTerapia_intensiva()+
                "\ntotale_ospedalizzati: "+selectedRegion.getTotale_ospedalizzati()+
                "\nisolamento_domiciliare: "+selectedRegion.getIsolamento_domiciliare()+
                "\ntotale_positivi: "+selectedRegion.getTotale_positivi()+
                "\nvariazione_totale_positivi: "+selectedRegion.getVariazione_totale_positivi()+
                "\nnuovi_positivi: "+selectedRegion.getNuovi_positivi()+
                "\ndimessi_guariti: "+selectedRegion.getDimessi_guariti()+
                "\ndeceduti: "+selectedRegion.getDeceduti()+
                "\ntotale_casi: "+selectedRegion.getTotale_casi()+
                "\ntamponi: "+selectedRegion.getTamponi());

 */
        if (dati != null){
            AndamentoRegionale selectedRegion = null;
            for (AndamentoRegionale ar: dati){
                if (ar.getDenominazione_regione().equals(regione)){
                    selectedRegion = ar;
                }
            }
            casi_totali_regioni.setText(decim.format(selectedRegion.getTotale_casi()) + " casi");
            ricoverati_con_sintomi.setText(decim.format(selectedRegion.getRicoverati_con_sintomi()) + "");
            terapia_intensiva.setText(decim.format(selectedRegion.getTerapia_intensiva()) + "");
            totale_ospedalizzati.setText(decim.format(selectedRegion.getTotale_ospedalizzati()) + "");
            isolamento_domiciliare.setText(decim.format(selectedRegion.getIsolamento_domiciliare()) + "");
            totale_attualmente_positivi.setText(decim.format(selectedRegion.getTotale_positivi()) + "");
            variazione_totale_positivi.setText("+" + decim.format(selectedRegion.getVariazione_totale_positivi()));
            dimessi_guariti.setText(decim.format(selectedRegion.getDimessi_guariti()) + "");
            deceduti.setText(decim.format(selectedRegion.getDeceduti()) + "");
            tamponi.setText(decim.format(selectedRegion.getTamponi()) + "");
            casi_totali_regioni_piu.setText("+" + decim.format(selectedRegion.getNuovi_positivi()));
            regioniProgressBar.setVisibility(View.GONE);
            regioniEditButton.setEnabled(true);

            masterLayout.setVisibility(View.VISIBLE);
        } else {
            regioniProgressBar.setVisibility(View.VISIBLE);
            regioniEditButton.setEnabled(false);
            masterLayout.setVisibility(View.GONE);

        }
    }

    private void setMoreData(ArrayList<AndamentoRegionale> dati, String regione){
        if (dati != null){
            AndamentoRegionale selectedRegion = null;
            for (AndamentoRegionale ar: dati){
                if (ar.getDenominazione_regione().equals(regione)){
                    selectedRegion = ar;
                }
            }
            int ricoverati_con_sintomi_piu_ = selectedRegion.getRicoverati_con_sintomi();
            int terapia_intensiva_piu_ = selectedRegion.getTerapia_intensiva();
            int totale_ospedalizzati_piu_ = selectedRegion.getTotale_ospedalizzati();
            int isolamento_domiciliare_piu_ = selectedRegion.getIsolamento_domiciliare();
            int totale_attualmente_positivi_piu_ = selectedRegion.getTotale_positivi();
            int dimessi_guariti_piu_ = selectedRegion.getDimessi_guariti();
            int deceduti_piu_ = selectedRegion.getDeceduti();

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

            regioniProgressBar.setVisibility(View.GONE);
            regioniEditButton.setEnabled(true);

            masterLayout.setVisibility(View.VISIBLE);

        } else {
            regioniProgressBar.setVisibility(View.VISIBLE);
            regioniEditButton.setEnabled(false);
            masterLayout.setVisibility(View.GONE);


        }
    }

    protected boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean isFragmentVisible(Fragment fragment) {
        Activity activity = fragment.getActivity();
        View focusedView = fragment.getView().findFocus();
        return activity != null
                && focusedView != null
                && focusedView == activity.getWindow().getDecorView().findFocus();
    }

    @Override
    public void onStart() {
        super.onStart();
        data = new DataHelper();

        if (selectedRegion[0].equals("")){
            regioniProgressBar.setVisibility(View.GONE);
            regioniEditButton.setEnabled(true);

            spinner.setVisibility(View.INVISIBLE);
            regioneTextView.setVisibility(View.VISIBLE);
        } else {
            regioneTextView.setText(selectedRegion[0]);
            spinner.setVisibility(View.INVISIBLE);
            regioneTextView.setVisibility(View.VISIBLE);
            tryConnection();
        }

        tryConnection();
    }
    private void setDarkTheme(View root){
        firstLayout.setBackgroundColor(Color.parseColor("#1d1d1d"));
        regioniEditButton.getBackground().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        //TextView italiaTVTitle = root.findViewById(R.id.italia_tv_title);
        //italiaTVTitle.setTextColor(Color.parseColor("#a8a8a8"));
        CardView regioni_rl1 = root.findViewById(R.id.regioni_rl1);
        regioni_rl1.setCardBackgroundColor(Color.parseColor("#292929"));
        regioni_rl1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));
        CardView regioni_rl2 = root.findViewById(R.id.regioni_rl2);
        regioni_rl2.setCardBackgroundColor(Color.parseColor("#292929"));
        regioni_rl2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));
        CardView regioni_rl3 = root.findViewById(R.id.regioni_rl3);
        regioni_rl3.setCardBackgroundColor(Color.parseColor("#292929"));
        regioni_rl3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));

        CardView regioni_rl4 = root.findViewById(R.id.regioni_rl4);
        regioni_rl4.setCardBackgroundColor(Color.parseColor("#292929"));
        regioni_rl4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));

        CardView regioni_rl5 = root.findViewById(R.id.regioni_rl5);
        regioni_rl5.setCardBackgroundColor(Color.parseColor("#292929"));
        regioni_rl5.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));

        CardView regioni_rl6 = root.findViewById(R.id.regioni_rl6);
        regioni_rl6.setCardBackgroundColor(Color.parseColor("#292929"));
        regioni_rl6.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));

        CardView regioni_rl7 = root.findViewById(R.id.regioni_rl7);
        regioni_rl7.setCardBackgroundColor(Color.parseColor("#292929"));
        regioni_rl7.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));

        CardView regioni_rl8 = root.findViewById(R.id.regioni_rl8);
        regioni_rl8.setCardBackgroundColor(Color.parseColor("#292929"));
        regioni_rl8.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));

        TextView ricoveratiConSintomi1 = root.findViewById(R.id.ricoveraticonsintomiregioni_text);
        ricoveratiConSintomi1.setTextColor(Color.parseColor("#a8a8a8"));

        TextView ricoveratiConSintomi2 = root.findViewById(R.id.ricoveraticonsintomiregioni);
        ricoveratiConSintomi2.setTextColor(Color.parseColor("#a8a8a8"));

        TextView terapiaIntensiva1 = root.findViewById(R.id.terapiaintensivaregioni_text);
        terapiaIntensiva1.setTextColor(Color.parseColor("#a8a8a8"));

        TextView terapiaIntensiva2 = root.findViewById(R.id.terapiaintensivaregioni);
        terapiaIntensiva2.setTextColor(Color.parseColor("#a8a8a8"));

        TextView totaleOspedalizzati1 = root.findViewById(R.id.totaleospedalizzatiregioni_text);
        totaleOspedalizzati1.setTextColor(Color.parseColor("#a8a8a8"));

        TextView totaleOspedalizzati2 = root.findViewById(R.id.totaleospedalizzatiregioni);
        totaleOspedalizzati2.setTextColor(Color.parseColor("#a8a8a8"));

        TextView isolamentoDomiciliare1 = root.findViewById(R.id.isolamentodomiciliareregioni_text);
        isolamentoDomiciliare1.setTextColor(Color.parseColor("#a8a8a8"));

        TextView isolamentoDomiciliare2 = root.findViewById(R.id.isolamentodomiciliareregioni);
        isolamentoDomiciliare2.setTextColor(Color.parseColor("#a8a8a8"));

        TextView dimessiGuariti1 = root.findViewById(R.id.dimessiguaritiregioni_text);
        dimessiGuariti1.setTextColor(Color.parseColor("#a8a8a8"));

        TextView dimessiGuariti2 = root.findViewById(R.id.dimessiguaritiregioni);
        dimessiGuariti2.setTextColor(Color.parseColor("#a8a8a8"));

        TextView deceduti1 = root.findViewById(R.id.decedutiregioni_text);
        deceduti1.setTextColor(Color.parseColor("#a8a8a8"));

        TextView deceduti2 = root.findViewById(R.id.decedutiregioni);
        deceduti2.setTextColor(Color.parseColor("#a8a8a8"));

        TextView totaleAttualmentePositivi1 = root.findViewById(R.id.totaleattualmentepositiviregioni_text);
        totaleAttualmentePositivi1.setTextColor(Color.parseColor("#a8a8a8"));

        TextView totaleAttualmentePositivi2 = root.findViewById(R.id.totaleattualmentepositiviregioni);
        totaleAttualmentePositivi2.setTextColor(Color.parseColor("#a8a8a8"));

        TextView tamponi1 = root.findViewById(R.id.tamponiregioni_text);
        tamponi1.setTextColor(Color.parseColor("#a8a8a8"));

        TextView tamponi2 = root.findViewById(R.id.tamponiregioni);
        tamponi2.setTextColor(Color.parseColor("#a8a8a8"));

        spinner.setBackgroundColor(Color.parseColor("#1d1d1d"));
        // inflate the layout
        View myLayout = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_dropdown,null);
        View myLayout2 = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner,null);

        // load the text view
        spinnerTextView =  myLayout.findViewById(R.id.text1);
        spinnerTextView.setBackgroundColor(Color.parseColor("#1d1d1d"));
        spinnerTextView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1d1d1d")));
        spinnerTextView.setTextColor(Color.parseColor("#a8a8a8"));
        // load the text view
        TextView spinnerTextView2 =  myLayout2.findViewById(R.id.custom_spinner);
        spinnerTextView2.setBackgroundColor(Color.parseColor("#1d1d1d"));
        spinnerTextView2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1d1d1d")));
        spinnerTextView2.setTextColor(Color.parseColor("#a8a8a8"));

        regioneTextView.setTextColor(Color.parseColor("#a8a8a8"));

        /*
        TextView italiaTVDataItalia = root.findViewById(R.id.data_italia);
        italiaTVDataItalia.setTextColor(Color.parseColor("#a8a8a8"));
        TextView italiaTVCasiTotaliItalia = root.findViewById(R.id.casitotaliitalia);
        italiaTVCasiTotaliItalia.setTextColor(Color.parseColor("#a8a8a8"));
        TextView italiaTVCasiTotaliItaliaPiu = root.findViewById(R.id.casitotaliitaliapiu);
        italiaTVCasiTotaliItaliaPiu.setTextColor(Color.parseColor("#a8a8a8"));

         */
    }
}