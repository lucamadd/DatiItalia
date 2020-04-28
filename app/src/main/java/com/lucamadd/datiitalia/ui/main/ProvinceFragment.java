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
import android.util.Log;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.lucamadd.datiitalia.Helper.AndamentoProvinciale;
import com.lucamadd.datiitalia.Helper.AndamentoRegionale;
import com.lucamadd.datiitalia.Helper.DataHelper;
import com.lucamadd.datiitalia.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProvinceFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private TextView casi_totali_province = null;
    private TextView casi_totali_province_piu = null;

    private TextView data_province = null;

    private PageViewModel pageViewModel;

    private LinearLayout firstLayout = null;
    private LinearLayout masterLayout = null;
    private LinearLayout retryLayout = null;
    private ProgressBar provinceProgressBar = null;

    private DecimalFormat decim = new DecimalFormat("#,###");

    private Button provinceEditButton = null;
    private Button retryButton = null;

    private ArrayList<AndamentoProvinciale> datiProvinciali = null;
    private ArrayList<AndamentoProvinciale> variazioneDatiProvinciali = null;

    private final String[] selectedRegion = {""};
    private TextView regioneTextView = null;
    private Spinner spinner = null;

    private DataHelper data;

    private boolean isDarkThemeEnabled;

    public static ProvinceFragment newInstance(int index) {
        ProvinceFragment fragment = new ProvinceFragment();
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
        View root = inflater.inflate(R.layout.fragment_province, container, false);



        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getContext());
        String favRegion= prefs.getString("regione","");
        selectedRegion[0] = favRegion;

        spinner = root.findViewById(R.id.spinner_province);

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

        provinceProgressBar = root.findViewById(R.id.province_progress_bar);

        provinceProgressBar.setVisibility(View.VISIBLE);
        masterLayout = root.findViewById(R.id.province_master_layout);
        firstLayout = root.findViewById(R.id.province_first_layout);
        retryLayout = root.findViewById(R.id.province_retry_layout);

        data_province = root.findViewById(R.id.data_province);


        final Vibrator vibe = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        final RelativeLayout hintLayout = root.findViewById(R.id.hint_layout_province);


        regioneTextView = root.findViewById(R.id.province_textview);
        provinceEditButton = root.findViewById(R.id.province_edit_button);
        provinceEditButton.setEnabled(false);
        provinceEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(20);
                if (provinceEditButton.getBackground().getConstantState() == getResources().getDrawable(R.drawable.edit).getConstantState()){
                    provinceEditButton.setBackground(getResources().getDrawable(R.drawable.ok));
                    if (isDarkThemeEnabled)
                        provinceEditButton.getBackground().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);

                    spinner.setEnabled(true);
                    spinner.performClick();
                    masterLayout.setVisibility(View.GONE);
                    hintLayout.setVisibility(View.VISIBLE);

                } else {
                    if (!regioneTextView.getText().toString().equals("Scegli una regione")){
                        hintLayout.setVisibility(View.GONE);
                        provinceProgressBar.setVisibility(View.VISIBLE);
                        regioneTextView.setVisibility(View.GONE);
                        spinner.setVisibility(View.VISIBLE);
                        provinceEditButton.setEnabled(false);
                        masterLayout.setVisibility(View.GONE);

                        spinner.setEnabled(false);
                        provinceEditButton.setBackground(getResources().getDrawable(R.drawable.edit));
                        selectedRegion[0] = spinner.getSelectedItem().toString();
                        tryConnection();
                    } else {
                        provinceEditButton.setBackground(getResources().getDrawable(R.drawable.edit));
                    }
                }
            }
        });

        if (isDarkThemeEnabled)
            setDarkTheme(root);
        else
            provinceEditButton.getBackground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);


        retryButton = root.findViewById(R.id.province_retry_button);
        if (isDarkThemeEnabled){
            retryButton.setBackgroundColor(Color.parseColor("#292929"));
            retryButton.setTextColor(Color.parseColor("#a8a8a8"));
        }
        Button retryButton = root.findViewById(R.id.province_retry_button);
        Button networkIcon = root.findViewById(R.id.province_network_icon);

        if (isDarkThemeEnabled){
            retryButton.setBackgroundColor(Color.parseColor("#292929"));
            retryButton.setTextColor(Color.parseColor("#a8a8a8"));
            networkIcon.getBackground().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
            TextView retry_TV = root.findViewById(R.id.province_tv_retry);
            retry_TV.setTextColor(Color.parseColor("#a8a8a8"));
        } else {
            networkIcon.getBackground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
        }
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retryLayout.setVisibility(View.GONE);
                provinceProgressBar.setVisibility(View.VISIBLE);
                provinceEditButton.setEnabled(false);
                masterLayout.setVisibility(View.GONE);

                if (isOnline()){
                    new Connection().execute();
                } else {
                    provinceProgressBar.setVisibility(View.GONE);
                    provinceEditButton.setEnabled(true);
                    retryLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        masterLayout.setVisibility(View.GONE);


        casi_totali_province  = root.findViewById(R.id.casitotaliprovince);

        casi_totali_province_piu = root.findViewById(R.id.casitotaliprovincepiu);

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

    private void tryConnection(){
        provinceEditButton.setEnabled(false);

        retryLayout.setVisibility(View.GONE);
        Log.i("SELECTED REGION IS ", selectedRegion[0] + " DDD");
        if (!selectedRegion[0].equals("")){
            if (isOnline()){
                new Connection().execute();

            } else {
                provinceProgressBar.setVisibility(View.GONE);
                provinceEditButton.setEnabled(true);

                masterLayout.setVisibility(View.GONE);
                retryLayout.setVisibility(View.VISIBLE);
            }
        } else {
            provinceProgressBar.setVisibility(View.GONE);
            provinceEditButton.setEnabled(true);

            spinner.setVisibility(View.INVISIBLE);
            regioneTextView.setVisibility(View.VISIBLE);
        }



    }

    private class Connection extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            data.getProvinceDataLatest();
            data.getMoreProvinceData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            datiProvinciali = data.getDatiProvinciali();
            variazioneDatiProvinciali = data.getVariazioneDatiProvinciali();
            setData(datiProvinciali, variazioneDatiProvinciali, selectedRegion[0]);
            data_province.setText(data.getCurrentDayText());
        }
    }


    private void setData(ArrayList<AndamentoProvinciale> dati, ArrayList<AndamentoProvinciale> datiNuovi, String regione){
        ArrayList<AndamentoProvinciale> selectedProvince = new ArrayList<>();
        ArrayList<AndamentoProvinciale> selectedProvinceNuovi = new ArrayList<>();

        if (dati != null){
            for (AndamentoProvinciale ar: dati){
                if (ar.getDenominazione_regione().equals(regione)) {
                    selectedProvince.add(ar);
                    Log.i("DEBUG", "\ncodice_regione: " + ar.getCodice_regione() +
                            "\ndenominazione_regione: " + ar.getDenominazione_regione() +
                            "\ntotale_casi: " + ar.getTotale_casi() +
                            "\ndenominazione_provincia: " + ar.getDenominazione_provincia());
                }

            }
            for (AndamentoProvinciale ar: datiNuovi){
                if (ar.getDenominazione_regione().equals(regione)) {
                    selectedProvinceNuovi.add(ar);
                    Log.i("DEBUG", "\ncodice_regione: " + ar.getCodice_regione() +
                            "\ndenominazione_regione: " + ar.getDenominazione_regione() +
                            "\ntotale_casi: " + ar.getTotale_casi() +
                            "\ndenominazione_provincia: " + ar.getDenominazione_provincia());
                }

            }
            Log.i("hjhj","pp "+masterLayout.getChildCount());
            masterLayout.removeViews(1,masterLayout.getChildCount()-1);

            int cases = 0;
            int newCases = 0;

            for (int i=0;i<selectedProvince.size();i++){

                if (getContext() != null){
                    cases += selectedProvince.get(i).getTotale_casi();
                    newCases += selectedProvinceNuovi.get(i).getTotale_casi();

                    casi_totali_province.setText(decim.format(cases) + " casi");
                    casi_totali_province_piu.setText("+" + decim.format(newCases));

                    //relativelayout
                    RelativeLayout whiteLayout = new RelativeLayout(getContext());
                    RelativeLayout.LayoutParams whiteParams = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    if (i != selectedProvince.size() - 1)
                        whiteParams.setMargins(0,20,0,0);
                    else
                        whiteParams.setMargins(0,20,0,50);
                    whiteLayout.setLayoutParams(whiteParams);

                    //cardview
                    CardView whiteCard = new CardView(getContext());
                    RelativeLayout.LayoutParams whiteCardParams = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, 160);
                    whiteCardParams.setMargins(0,20,0,0);
                    whiteCard.setLayoutParams(whiteCardParams);
                    whiteCard.setRadius(16);
                    whiteCard.setElevation(16);
                    whiteCard.setClipToPadding(false);
                    whiteCard.setClipChildren(false);
                    whiteCard.setCardElevation(20);
                    whiteCard.setPreventCornerOverlap(false);
                    if (isDarkThemeEnabled){
                        whiteCard.setCardBackgroundColor(Color.parseColor("#292929"));
                        //whiteCard.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));
                    } else {
                        whiteCard.setCardBackgroundColor(Color.parseColor("#ffffff"));

                    }
                    //relativelayout
                    RelativeLayout innerLayout = new RelativeLayout(getContext());
                    RelativeLayout.LayoutParams innerParams = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    innerLayout.setLayoutParams(innerParams);
                    innerLayout.setPadding(20,16,16,16);

                    //first textview
                    TextView provinceName = new TextView(getContext());
                    RelativeLayout.LayoutParams tv1Params = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    provinceName.setLayoutParams(tv1Params);
                    provinceName.setTypeface(ResourcesCompat.getFont(getContext(),R.font.bevietnambold));
                    if (isDarkThemeEnabled)
                        provinceName.setTextColor(Color.parseColor("#a8a8a8"));
                    else
                        provinceName.setTextColor(Color.parseColor("#000000"));
                    provinceName.setText(selectedProvince.get(i).getDenominazione_provincia());

                    //second textview
                    TextView provinceCasi = new TextView(getContext());
                    RelativeLayout.LayoutParams tv2Params = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    tv2Params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    provinceCasi.setLayoutParams(tv2Params);
                    provinceCasi.setTypeface(ResourcesCompat.getFont(getContext(),R.font.bevietnambold));
                    if (isDarkThemeEnabled)
                        provinceCasi.setTextColor(Color.parseColor("#a8a8a8"));
                    else
                        provinceCasi.setTextColor(Color.parseColor("#000000"));

                    provinceCasi.setTextSize(16);
                    provinceCasi.setText(decim.format(selectedProvince.get(i).getTotale_casi())+" casi");

                    //third textview
                    TextView provinceCasiPiu = new TextView(getContext());
                    RelativeLayout.LayoutParams tv3Params = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    tv3Params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    tv3Params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                    provinceCasiPiu.setLayoutParams(tv3Params);
                    provinceCasiPiu.setTypeface(ResourcesCompat.getFont(getContext(),R.font.bevietnam));

                    provinceCasiPiu.setTextSize(16);
                    int nuoviCasi = selectedProvinceNuovi.get(i).getTotale_casi();
                    if (nuoviCasi > 0){
                        provinceCasiPiu.setText("+" + decim.format(nuoviCasi));
                        provinceCasiPiu.setTextColor(Color.RED);
                    } else {
                        provinceCasiPiu.setText("" + decim.format(nuoviCasi));
                        provinceCasiPiu.setTextColor(Color.rgb(0,153,51));
                    }

                    innerLayout.addView(provinceName);
                    innerLayout.addView(provinceCasi);
                    innerLayout.addView(provinceCasiPiu);

                    whiteCard.addView(innerLayout);

                    whiteLayout.addView(whiteCard);

                    masterLayout.addView(whiteLayout);
                }
            }
            provinceProgressBar.setVisibility(View.GONE);
            provinceEditButton.setEnabled(true);

            masterLayout.setVisibility(View.VISIBLE);
        } else {
            provinceProgressBar.setVisibility(View.VISIBLE);
            provinceEditButton.setEnabled(false);
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
            provinceProgressBar.setVisibility(View.GONE);
            provinceEditButton.setEnabled(true);
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
        provinceEditButton.getBackground().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);

        spinner.setBackgroundColor(Color.parseColor("#1d1d1d"));

        regioneTextView.setTextColor(Color.parseColor("#a8a8a8"));

    }
}