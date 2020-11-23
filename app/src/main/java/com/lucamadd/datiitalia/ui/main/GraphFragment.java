package com.lucamadd.datiitalia.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lucamadd.datiitalia.Helper.AndamentoNazionale;
import com.lucamadd.datiitalia.Helper.AndamentoRegionale;
import com.lucamadd.datiitalia.Helper.DataHelper;
import com.lucamadd.datiitalia.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GraphFragment extends Fragment {

    private BarChart chart1;
    private BarChart chart2;
    private BarChart chart3;
    private BarChart chart4;
    private DataHelper data;
    private ArrayList<AndamentoNazionale> datiNazionali = null;
    private ArrayList<AndamentoRegionale> datiRegionali = null;
    private ArrayList<AndamentoRegionale> datiRegioni = null;

    private Button eyeButton;

    private boolean isDarkThemeEnabled;
    private LinearLayout graphLayout;
    private TextView andamentoTextView;

    private CardView cardGraph1;
    private CardView cardGraph2;
    private CardView cardGraph3;
    private CardView cardGraph4;
    private TextView textGraph1;
    private TextView textGraph2;
    private TextView textGraph3;
    private TextView textGraph4;

    private TextView regioniAlertGraph;

    private String favRegion;

    private TextView tvAbruzzo;
    private TextView tvAbruzzoPiu;
    private TextView tvBasilicata;
    private TextView tvBasilicataPiu;
    private TextView tvPABolzano;
    private TextView tvPABolzanoPiu;
    private TextView tvCalabria;
    private TextView tvCalabriaPiu;
    private TextView tvCampania;
    private TextView tvCampaniaPiu;
    private TextView tvEmiliaRomagna;
    private TextView tvEmiliaRomagnaPiu;
    private TextView tvFriuliVeneziaGiulia;
    private TextView tvFriuliVeneziaGiuliaPiu;
    private TextView tvLazio;
    private TextView tvLazioPiu;
    private TextView tvLiguria;
    private TextView tvLiguriaPiu;
    private TextView tvLombardia;
    private TextView tvLombardiaPiu;
    private TextView tvMarche;
    private TextView tvMarchePiu;
    private TextView tvMolise;
    private TextView tvMolisePiu;
    private TextView tvPiemonte;
    private TextView tvPiemontePiu;
    private TextView tvPuglia;
    private TextView tvPugliaPiu;
    private TextView tvSardegna;
    private TextView tvSardegnaPiu;
    private TextView tvSicilia;
    private TextView tvSiciliaPiu;
    private TextView tvToscana;
    private TextView tvToscanaPiu;
    private TextView tvPATrento;
    private TextView tvPATrentoPiu;
    private TextView tvUmbria;
    private TextView tvUmbriaPiu;
    private TextView tvValleDAosta;
    private TextView tvValleDAostaPiu;
    private TextView tvVeneto;
    private TextView tvVenetoPiu;

    private TextView tvRiepilogoRegionale;

    private CardView cardAbruzzo;
    private CardView cardBasilicata;
    private CardView cardBolzano;
    private CardView cardCalabria;
    private CardView cardCampania;
    private CardView cardEmiliaRomagna;
    private CardView cardFriuliVeneziaGiulia;
    private CardView cardLazio;
    private CardView cardLiguria;
    private CardView cardLombardia;
    private CardView cardMarche;
    private CardView cardMolise;
    private CardView cardPiemonte;
    private CardView cardPuglia;
    private CardView cardSardegna;
    private CardView cardSicilia;
    private CardView cardToscana;
    private CardView cardTrento;
    private CardView cardUmbria;
    private CardView cardValleDAosta;
    private CardView cardVeneto;

    private TextView textAbruzzo;
    private TextView textBasilicata;
    private TextView textBolzano;
    private TextView textCalabria;
    private TextView textCampania;
    private TextView textEmiliaRomagna;
    private TextView textFriuliVeneziaGiulia;
    private TextView textLazio;
    private TextView textLiguria;
    private TextView textLombardia;
    private TextView textMarche;
    private TextView textMolise;
    private TextView textPiemonte;
    private TextView textPuglia;
    private TextView textSardegna;
    private TextView textSicilia;
    private TextView textToscana;
    private TextView textTrento;
    private TextView textUmbria;
    private TextView textValleDAosta;
    private TextView textVeneto;


    private DecimalFormat decim = new DecimalFormat("#,###");



    public GraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment GraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphFragment newInstance(int param1) {
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getContext());
        isDarkThemeEnabled = prefs.getBoolean("dark_theme",false);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_graph, container, false);
        chart1 = root.findViewById(R.id.barchart1);
        chart2 = root.findViewById(R.id.barchart2);
        chart3 = root.findViewById(R.id.barchart3);
        chart4 = root.findViewById(R.id.barchart4);
        eyeButton = root.findViewById(R.id.eye_button);
        graphLayout = root.findViewById(R.id.graph_layout);
        andamentoTextView = root.findViewById(R.id.andamento_tv_title);

        cardGraph1 = root.findViewById(R.id.card_graph1);
        cardGraph2 = root.findViewById(R.id.card_graph2);
        cardGraph3 = root.findViewById(R.id.card_graph3);
        cardGraph4 = root.findViewById(R.id.card_graph4);

        textGraph1 = root.findViewById(R.id.graph_tv1);
        textGraph2 = root.findViewById(R.id.graph_tv2);
        textGraph3 = root.findViewById(R.id.graph_tv3);
        textGraph4 = root.findViewById(R.id.graph_tv4);

        tvAbruzzo = root.findViewById(R.id.casiAbruzzo);
        tvAbruzzoPiu = root.findViewById(R.id.casiAbruzzoPiu);
        tvBasilicata = root.findViewById(R.id.casiBasilicata);
        tvBasilicataPiu = root.findViewById(R.id.casiBasilicatapiu);
        tvPABolzano = root.findViewById(R.id.casiBolzano);
        tvPABolzanoPiu = root.findViewById(R.id.casiBolzanopiu);
        tvCalabria = root.findViewById(R.id.casiCalabria);
        tvCalabriaPiu = root.findViewById(R.id.casiCalabriapiu);
        tvCampania = root.findViewById(R.id.casiCampania);
        tvCampaniaPiu = root.findViewById(R.id.casiCampaniapiu);
        tvEmiliaRomagna = root.findViewById(R.id.casiEmiliaRomagna);
        tvEmiliaRomagnaPiu = root.findViewById(R.id.casiEmiliaRomagnapiu);
        tvFriuliVeneziaGiulia = root.findViewById(R.id.casiFriuliVeneziaGiulia);
        tvFriuliVeneziaGiuliaPiu = root.findViewById(R.id.casiFriuliVeneziaGiuliapiu);
        tvLazio = root.findViewById(R.id.casiLazio);
        tvLazioPiu = root.findViewById(R.id.casiLaziopiu);
        tvLiguria = root.findViewById(R.id.casiLiguria);
        tvLiguriaPiu = root.findViewById(R.id.casiLiguriapiu);
        tvLombardia = root.findViewById(R.id.casiLombardia);
        tvLombardiaPiu = root.findViewById(R.id.casiLombardiapiu);
        tvMarche = root.findViewById(R.id.casiMarche);
        tvMarchePiu = root.findViewById(R.id.casiMarchepiu);
        tvMolise = root.findViewById(R.id.casiMolise);
        tvMolisePiu = root.findViewById(R.id.casiMolisepiu);
        tvPiemonte = root.findViewById(R.id.casiPiemonte);
        tvPiemontePiu = root.findViewById(R.id.casiPiemontepiu);
        tvPuglia = root.findViewById(R.id.casiPuglia);
        tvPugliaPiu = root.findViewById(R.id.casiPugliapiu);
        tvSardegna = root.findViewById(R.id.casiSardegna);
        tvSardegnaPiu = root.findViewById(R.id.casiSardegnapiu);
        tvSicilia = root.findViewById(R.id.casiSicilia);
        tvSiciliaPiu = root.findViewById(R.id.casiSiciliaPiu);
        tvToscana = root.findViewById(R.id.casiToscana);
        tvToscanaPiu = root.findViewById(R.id.casiToscanapiu);
        tvPATrento = root.findViewById(R.id.casiTrento);
        tvPATrentoPiu = root.findViewById(R.id.casiTrentopiu);
        tvUmbria = root.findViewById(R.id.casiUmbria);
        tvUmbriaPiu = root.findViewById(R.id.casiUmbriaPiu);
        tvValleDAosta = root.findViewById(R.id.casiValleDAosta);
        tvValleDAostaPiu = root.findViewById(R.id.casiValleDAostapiu);
        tvVeneto = root.findViewById(R.id.casiVeneto);
        tvVenetoPiu = root.findViewById(R.id.casiVenetopiu);

        tvRiepilogoRegionale = root.findViewById(R.id.tvandamentoregionalegraph);

        cardAbruzzo = root.findViewById(R.id.cardAbruzzo);
        cardBasilicata = root.findViewById(R.id.cardBasilicata);
        cardBolzano = root.findViewById(R.id.cardBolzano);
        cardCalabria = root.findViewById(R.id.cardCalabria);
        cardCampania = root.findViewById(R.id.cardCampania);
        cardEmiliaRomagna = root.findViewById(R.id.cardEmiliaRomagna);
        cardFriuliVeneziaGiulia = root.findViewById(R.id.cardFriuliVeneziaGiulia);
        cardLazio = root.findViewById(R.id.cardLazio);
        cardLiguria = root.findViewById(R.id.cardLiguria);
        cardLombardia = root.findViewById(R.id.cardLombardia);
        cardMarche = root.findViewById(R.id.cardMarche);
        cardMolise = root.findViewById(R.id.cardMolise);
        cardPiemonte = root.findViewById(R.id.cardPiemonte);
        cardPuglia = root.findViewById(R.id.cardPuglia);
        cardSardegna = root.findViewById(R.id.cardSardegna);
        cardSicilia = root.findViewById(R.id.cardSicilia);
        cardToscana = root.findViewById(R.id.cardToscana);
        cardTrento = root.findViewById(R.id.cardTrento);
        cardUmbria = root.findViewById(R.id.cardUmbria);
        cardValleDAosta = root.findViewById(R.id.cardValleDAosta);
        cardVeneto = root.findViewById(R.id.cardVeneto);

        textAbruzzo = root.findViewById(R.id.textAbruzzo);
        textBasilicata = root.findViewById(R.id.textBasilicata);
        textBolzano = root.findViewById(R.id.textBolzano);
        textCalabria = root.findViewById(R.id.textCalabria);
        textCampania = root.findViewById(R.id.textCampania);
        textEmiliaRomagna = root.findViewById(R.id.textEmiliaRomagna);
        textFriuliVeneziaGiulia = root.findViewById(R.id.textFriuliVeneziaGiulia);
        textLazio = root.findViewById(R.id.textLazio);
        textLiguria = root.findViewById(R.id.textLiguria);
        textLombardia = root.findViewById(R.id.textLombardia);
        textMarche = root.findViewById(R.id.textMarche);
        textMolise = root.findViewById(R.id.textMolise);
        textPiemonte = root.findViewById(R.id.textPiemonte);
        textPuglia = root.findViewById(R.id.textPuglia);
        textSardegna = root.findViewById(R.id.textSardegna);
        textSicilia = root.findViewById(R.id.textSicilia);
        textToscana = root.findViewById(R.id.textToscana);
        textTrento = root.findViewById(R.id.textTrento);
        textUmbria = root.findViewById(R.id.textUmbria);
        textValleDAosta = root.findViewById(R.id.textValleDAosta);
        textVeneto = root.findViewById(R.id.textVeneto);



        regioniAlertGraph = root.findViewById(R.id.regioni_alert_graph);
        if (isDarkThemeEnabled)
            setDarkTheme(root);
        else
            eyeButton.getBackground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getContext());
        favRegion= prefs.getString("regione","");
        if (!favRegion.equals("")){
            textGraph3.setText("Nuovi casi (" + favRegion + ")");
            textGraph4.setText("Totale attualmente positivi (" + favRegion + ")");
        } else {
            cardGraph3.setVisibility(View.GONE);
            cardGraph4.setVisibility(View.GONE);
            regioniAlertGraph.setVisibility(View.VISIBLE);
        }
        return root;
    }

    private void setDarkTheme(View root){
        graphLayout.setBackgroundColor(Color.parseColor("#1d1d1d"));
        andamentoTextView.setTextColor(Color.parseColor("#a8a8a8"));
        eyeButton.getBackground().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        cardGraph1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));
        textGraph1.setTextColor(Color.parseColor("#a8a8a8"));

        cardGraph2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));
        textGraph2.setTextColor(Color.parseColor("#a8a8a8"));

        cardGraph3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));
        textGraph3.setTextColor(Color.parseColor("#a8a8a8"));

        cardGraph4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#292929")));
        textGraph4.setTextColor(Color.parseColor("#a8a8a8"));
        regioniAlertGraph.setTextColor(Color.parseColor("#a8a8a8"));

        tvRiepilogoRegionale.setTextColor(Color.parseColor("#a8a8a8"));

        cardAbruzzo.setCardBackgroundColor(Color.parseColor("#292929"));
        cardBasilicata.setCardBackgroundColor(Color.parseColor("#292929"));
        cardBolzano.setCardBackgroundColor(Color.parseColor("#292929"));
        cardCalabria.setCardBackgroundColor(Color.parseColor("#292929"));
        cardCampania.setCardBackgroundColor(Color.parseColor("#292929"));
        cardEmiliaRomagna.setCardBackgroundColor(Color.parseColor("#292929"));
        cardFriuliVeneziaGiulia.setCardBackgroundColor(Color.parseColor("#292929"));
        cardLazio.setCardBackgroundColor(Color.parseColor("#292929"));
        cardLiguria.setCardBackgroundColor(Color.parseColor("#292929"));
        cardLombardia.setCardBackgroundColor(Color.parseColor("#292929"));
        cardMarche.setCardBackgroundColor(Color.parseColor("#292929"));
        cardMolise.setCardBackgroundColor(Color.parseColor("#292929"));
        cardPiemonte.setCardBackgroundColor(Color.parseColor("#292929"));
        cardPuglia.setCardBackgroundColor(Color.parseColor("#292929"));
        cardSardegna.setCardBackgroundColor(Color.parseColor("#292929"));
        cardSicilia.setCardBackgroundColor(Color.parseColor("#292929"));
        cardToscana.setCardBackgroundColor(Color.parseColor("#292929"));
        cardTrento.setCardBackgroundColor(Color.parseColor("#292929"));
        cardUmbria.setCardBackgroundColor(Color.parseColor("#292929"));
        cardValleDAosta.setCardBackgroundColor(Color.parseColor("#292929"));
        cardVeneto.setCardBackgroundColor(Color.parseColor("#292929"));

        textAbruzzo.setTextColor(Color.parseColor("#a8a8a8"));
        textBasilicata.setTextColor(Color.parseColor("#a8a8a8"));
        textBolzano.setTextColor(Color.parseColor("#a8a8a8"));
        textCalabria.setTextColor(Color.parseColor("#a8a8a8"));
        textCampania.setTextColor(Color.parseColor("#a8a8a8"));
        textEmiliaRomagna.setTextColor(Color.parseColor("#a8a8a8"));
        textFriuliVeneziaGiulia.setTextColor(Color.parseColor("#a8a8a8"));
        textLazio.setTextColor(Color.parseColor("#a8a8a8"));
        textLiguria.setTextColor(Color.parseColor("#a8a8a8"));
        textLombardia.setTextColor(Color.parseColor("#a8a8a8"));
        textMarche.setTextColor(Color.parseColor("#a8a8a8"));
        textMolise.setTextColor(Color.parseColor("#a8a8a8"));
        textPiemonte.setTextColor(Color.parseColor("#a8a8a8"));
        textPuglia.setTextColor(Color.parseColor("#a8a8a8"));
        textSardegna.setTextColor(Color.parseColor("#a8a8a8"));
        textSicilia.setTextColor(Color.parseColor("#a8a8a8"));
        textToscana.setTextColor(Color.parseColor("#a8a8a8"));
        textTrento.setTextColor(Color.parseColor("#a8a8a8"));
        textUmbria.setTextColor(Color.parseColor("#a8a8a8"));
        textValleDAosta.setTextColor(Color.parseColor("#a8a8a8"));
        textVeneto.setTextColor(Color.parseColor("#a8a8a8"));

        tvAbruzzo.setTextColor(Color.parseColor("#a8a8a8"));
        tvBasilicata.setTextColor(Color.parseColor("#a8a8a8"));
        tvPABolzano.setTextColor(Color.parseColor("#a8a8a8"));
        tvCalabria.setTextColor(Color.parseColor("#a8a8a8"));
        tvCampania.setTextColor(Color.parseColor("#a8a8a8"));
        tvEmiliaRomagna.setTextColor(Color.parseColor("#a8a8a8"));
        tvFriuliVeneziaGiulia.setTextColor(Color.parseColor("#a8a8a8"));
        tvLazio.setTextColor(Color.parseColor("#a8a8a8"));
        tvLiguria.setTextColor(Color.parseColor("#a8a8a8"));
        tvLombardia.setTextColor(Color.parseColor("#a8a8a8"));
        tvMarche.setTextColor(Color.parseColor("#a8a8a8"));
        tvMolise.setTextColor(Color.parseColor("#a8a8a8"));
        tvPiemonte.setTextColor(Color.parseColor("#a8a8a8"));
        tvPuglia.setTextColor(Color.parseColor("#a8a8a8"));
        tvSardegna.setTextColor(Color.parseColor("#a8a8a8"));
        tvSicilia.setTextColor(Color.parseColor("#a8a8a8"));
        tvToscana.setTextColor(Color.parseColor("#a8a8a8"));
        tvPATrento.setTextColor(Color.parseColor("#a8a8a8"));
        tvUmbria.setTextColor(Color.parseColor("#a8a8a8"));
        tvValleDAosta.setTextColor(Color.parseColor("#a8a8a8"));
        tvVeneto.setTextColor(Color.parseColor("#a8a8a8"));
    }

    private class Connection extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            data.getAllData();
            data.getRegioniDataLatest();
            data.getMoreRegioniData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("onPostExecute()","iniziato");
            datiNazionali = data.getGraphData();
            datiRegionali = data.getGraphRegionData();
            datiRegioni = data.getDatiRegionali();
            setData(datiNazionali);
            setRegionData(datiRegionali);
            setAndamentoRegioniData(datiRegioni);
            Log.i("onPostExecute()","terminato");

        }

        private void setAndamentoRegioniData(ArrayList<AndamentoRegionale> datiRegioni) {
            if (datiRegioni != null){
                for (AndamentoRegionale ar:datiRegioni){
                    switch (ar.getDenominazione_regione()){
                        case "Abruzzo":
                            tvAbruzzo.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvAbruzzoPiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvAbruzzoPiu.setTextColor(Color.RED);
                            } else {
                                tvAbruzzoPiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvAbruzzoPiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "Basilicata":
                            tvBasilicata.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvBasilicataPiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvBasilicataPiu.setTextColor(Color.RED);
                            } else {
                                tvBasilicataPiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvBasilicataPiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "P.A. Bolzano":
                            tvPABolzano.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvPABolzanoPiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvPABolzanoPiu.setTextColor(Color.RED);
                            } else {
                                tvPABolzanoPiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvPABolzanoPiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "Calabria":
                            tvCalabria.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvCalabriaPiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvCalabriaPiu.setTextColor(Color.RED);
                            } else {
                                tvCalabriaPiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvCalabriaPiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "Campania":
                            tvCampania.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvCampaniaPiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvCampaniaPiu.setTextColor(Color.RED);
                            } else {
                                tvCampaniaPiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvCampaniaPiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "Emilia-Romagna":
                            tvEmiliaRomagna.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvEmiliaRomagnaPiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvEmiliaRomagnaPiu.setTextColor(Color.RED);
                            } else {
                                tvEmiliaRomagnaPiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvEmiliaRomagnaPiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "Friuli Venezia Giulia":
                            tvFriuliVeneziaGiulia.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvFriuliVeneziaGiuliaPiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvFriuliVeneziaGiuliaPiu.setTextColor(Color.RED);
                            } else {
                                tvFriuliVeneziaGiuliaPiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvFriuliVeneziaGiuliaPiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "Lazio":
                            tvLazio.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvLazioPiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvLazioPiu.setTextColor(Color.RED);
                            } else {
                                tvLazioPiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvLazioPiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "Liguria":
                            tvLiguria.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvLiguriaPiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvLiguriaPiu.setTextColor(Color.RED);
                            } else {
                                tvLiguriaPiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvLiguriaPiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "Lombardia":
                            tvLombardia.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvLombardiaPiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvLombardiaPiu.setTextColor(Color.RED);
                            } else {
                                tvLombardiaPiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvLombardiaPiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "Marche":
                            tvMarche.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvMarchePiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvMarchePiu.setTextColor(Color.RED);
                            } else {
                                tvMarchePiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvMarchePiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "Molise":
                            tvMolise.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvMolisePiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvMolisePiu.setTextColor(Color.RED);
                            } else {
                                tvMolisePiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvMolisePiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "Piemonte":
                            tvPiemonte.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvPiemontePiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvPiemontePiu.setTextColor(Color.RED);
                            } else {
                                tvPiemontePiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvPiemontePiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "Puglia":
                            tvPuglia.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvPugliaPiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvPugliaPiu.setTextColor(Color.RED);
                            } else {
                                tvPugliaPiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvPugliaPiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "Sardegna":
                            tvSardegna.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvSardegnaPiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvSardegnaPiu.setTextColor(Color.RED);
                            } else {
                                tvSardegnaPiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvSardegnaPiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "Sicilia":
                            tvSicilia.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvSiciliaPiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvSiciliaPiu.setTextColor(Color.RED);
                            } else {
                                tvSiciliaPiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvSiciliaPiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "Toscana":
                            tvToscana.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvToscanaPiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvToscanaPiu.setTextColor(Color.RED);
                            } else {
                                tvToscanaPiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvToscanaPiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "P.A. Trento":
                            tvPATrento.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvPATrentoPiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvPATrentoPiu.setTextColor(Color.RED);
                            } else {
                                tvPATrentoPiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvPATrentoPiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "Umbria":
                            tvUmbria.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvUmbriaPiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvUmbriaPiu.setTextColor(Color.RED);
                            } else {
                                tvUmbriaPiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvUmbriaPiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "Valle d'Aosta":
                            tvValleDAosta.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvValleDAostaPiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvValleDAostaPiu.setTextColor(Color.RED);
                            } else {
                                tvValleDAostaPiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvValleDAostaPiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                        case "Veneto":
                            tvVeneto.setText(decim.format(ar.getTotale_casi())+" casi");
                            if (ar.getNuovi_positivi() > 0){
                                tvVenetoPiu.setText("+" + decim.format(ar.getNuovi_positivi()));
                                tvVenetoPiu.setTextColor(Color.RED);
                            } else {
                                tvVenetoPiu.setText("" + decim.format(ar.getNuovi_positivi()));
                                tvVenetoPiu.setTextColor(Color.rgb(0,153,51));
                            }
                            break;
                    }
                }
            } else {
                Toast.makeText(getContext(), "C'è stato un errore nella ricezione dei dati. " +
                        "Contatta lo sviluppatore per maggiori informazioni.", Toast.LENGTH_LONG);
            }
        }
    }

    private void setRegionData(ArrayList<AndamentoRegionale> dati){
        Log.i("setRegionData()","iniziato");
        ArrayList<AndamentoRegionale> regionData = new ArrayList<>();
        if (!favRegion.equals("")) {
            Log.i("setRegionData()","entrato nel primo if");
            if (dati != null) {
                Log.i("setRegionData()","entrato nel secondo if");
                for (int i=0;i<dati.size();i++){
                    if (dati.get(i).getDenominazione_regione().equals(favRegion))
                        regionData.add(dati.get(i));
                }
                for (int i=0;i<regionData.size();i++){
                    Log.i("setRegionData() - for","favRegion= "+ favRegion + "  denom_regione= "+regionData.get(i).getDenominazione_regione());

                }
                Log.i("setRegionData()","inoltrato");
                for (int i=0;i<dati.size();i++)
                    Log.i("pippobaudo", "regione: "+dati.get(i).getDenominazione_regione()+"   casi: "+dati.get(i).getTotale_casi());
                final ArrayList<BarChart> charts = new ArrayList<>();
                charts.add(chart3);
                charts.add(chart4);
                ArrayList<BarEntry> nuoviCasi = new ArrayList<>();
                ArrayList<BarEntry> totaleCasi = new ArrayList<>();

                ArrayList<ArrayList<BarEntry>> entries = new ArrayList<>();
                entries.add(nuoviCasi);
                entries.add(totaleCasi);

                ArrayList<String> year = new ArrayList<>();
                for (int i = 0; i < regionData.size(); i++) {
                    nuoviCasi.add(new BarEntry(regionData.get(i).getNuovi_positivi(), i));
                    totaleCasi.add(new BarEntry(regionData.get(i).getTotale_positivi(), i));
                    year.add(getRightDate(regionData.get(i).getData()));
                }
                for (int j = 0; j < charts.size(); j++) {
                    BarDataSet bardataset = new BarDataSet(entries.get(j), "Nuovi casi");
                    charts.get(j).getLegend().setEnabled(false);
                    charts.get(j).animateY(500);
                    charts.get(j).getAxisLeft().setAxisMinValue(0);
                    charts.get(j).getAxisLeft().setDrawGridLines(false);
                    charts.get(j).getAxisLeft().setDrawAxisLine(true);
                    if (isDarkThemeEnabled)
                        charts.get(j).getAxisLeft().setTextColor(Color.parseColor("#a8a8a8"));
                    charts.get(j).getAxisRight().setEnabled(false);
                    charts.get(j).setDescription("");
                    charts.get(j).getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    charts.get(j).getXAxis().setDrawGridLines(false);
                    if (isDarkThemeEnabled)
                        charts.get(j).getXAxis().setTextColor(Color.parseColor("#a8a8a8"));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        charts.get(j).getXAxis().setTypeface(getResources().getFont(R.font.bevietnambold));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        charts.get(j).getAxisLeft().setTypeface(getResources().getFont(R.font.bevietnambold));
                    }
                    charts.get(j).setNoDataText("Description that you want");
                    Paint p = charts.get(j).getPaint(Chart.PAINT_INFO);
                    p.setTextSize(13);
                    if (isDarkThemeEnabled)
                        p.setColor(Color.parseColor("#a8a8a8"));
                    else
                        p.setColor(Color.parseColor("#000000"));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        p.setTypeface(getResources().getFont(R.font.bevietnambold));
                    }
                    BarData data = new BarData(year, bardataset);
                    bardataset.setColors(ColorTemplate.PASTEL_COLORS);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        charts.get(j).getLegend().setTypeface(getResources().getFont(R.font.bevietnambold));
                    }
                    if (isDarkThemeEnabled)
                        charts.get(j).getLegend().setTextColor(Color.parseColor("#a8a8a8"));
                    charts.get(j).setData(data);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        charts.get(j).getBarData().setValueTypeface(getResources().getFont(R.font.bevietnambold));
                    }
                    if (isDarkThemeEnabled)
                        charts.get(j).getBarData().setValueTextColor(Color.parseColor("#a8a8a8"));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        charts.get(j).setDescriptionTypeface(getResources().getFont(R.font.bevietnambold));
                    }
                    final int finalJ = j;
                    eyeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (charts.get(finalJ).isDrawValueAboveBarEnabled()) {
                                eyeButton.setBackground(getResources().getDrawable(R.drawable.eye_closed));
                                if (isDarkThemeEnabled)
                                    eyeButton.getBackground().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
                                for (BarChart chart : charts) {
                                    chart.setDrawValueAboveBar(false);
                                    chart.getBarData().setDrawValues(false);
                                }

                            } else {
                                eyeButton.setBackground(getResources().getDrawable(R.drawable.eye_open));
                                if (isDarkThemeEnabled)
                                    eyeButton.getBackground().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
                                for (BarChart chart : charts) {
                                    chart.setDrawValueAboveBar(true);
                                    chart.getBarData().setDrawValues(true);
                                }
                            }
                            for (BarChart chart : charts) {
                                chart.invalidate();
                            }
                        }
                    });
                }
            }
        }
    }

    private void setData(ArrayList<AndamentoNazionale> dati){
        if (dati !=null){
            final ArrayList<BarChart> charts = new ArrayList<>();
            charts.add(chart1);
            charts.add(chart2);
            ArrayList<BarEntry> nuoviCasi = new ArrayList<>();
            ArrayList<BarEntry> totaleCasi = new ArrayList<>();

            ArrayList<ArrayList<BarEntry>> entries = new ArrayList<>();
            entries.add(nuoviCasi);
            entries.add(totaleCasi);

            ArrayList<String> year = new ArrayList<>();
            for (int i=0;i<dati.size();i++){
                nuoviCasi.add(new BarEntry(dati.get(i).getNuovi_positivi(),i));
                totaleCasi.add(new BarEntry(dati.get(i).getTotale_positivi(),i));
                year.add(getRightDate(dati.get(i).getData()));
            }
            for (int j=0;j<charts.size();j++){
                BarDataSet bardataset = new BarDataSet(entries.get(j), "Nuovi casi");
                charts.get(j).getLegend().setEnabled(false);
                charts.get(j).animateY(500);
                charts.get(j).getAxisLeft().setAxisMinValue(0);
                charts.get(j).getAxisLeft().setDrawGridLines(false);
                charts.get(j).getAxisLeft().setDrawAxisLine(true);
                if (isDarkThemeEnabled)
                    charts.get(j).getAxisLeft().setTextColor(Color.parseColor("#a8a8a8"));
                charts.get(j).getAxisRight().setEnabled(false);
                charts.get(j).setDescription("");
                charts.get(j).getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                charts.get(j).getXAxis().setDrawGridLines(false);
                if (isDarkThemeEnabled)
                    charts.get(j).getXAxis().setTextColor(Color.parseColor("#a8a8a8"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    charts.get(j).getXAxis().setTypeface(getResources().getFont(R.font.bevietnambold));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    charts.get(j).getAxisLeft().setTypeface(getResources().getFont(R.font.bevietnambold));
                }
                charts.get(j).setNoDataText("Description that you want");
                Paint p = charts.get(j).getPaint(Chart.PAINT_INFO);
                p.setTextSize(13);
                if (isDarkThemeEnabled)
                    p.setColor(Color.parseColor("#a8a8a8"));
                else
                    p.setColor(Color.parseColor("#000000"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    p.setTypeface(getResources().getFont(R.font.bevietnambold));
                }
                BarData data = new BarData(year,bardataset);
                bardataset.setColors(ColorTemplate.PASTEL_COLORS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    charts.get(j).getLegend().setTypeface(getResources().getFont(R.font.bevietnambold));
                }
                if (isDarkThemeEnabled)
                    charts.get(j).getLegend().setTextColor(Color.parseColor("#a8a8a8"));
                charts.get(j).setData(data);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    charts.get(j).getBarData().setValueTypeface(getResources().getFont(R.font.bevietnambold));
                }
                if (isDarkThemeEnabled)
                    charts.get(j).getBarData().setValueTextColor(Color.parseColor("#a8a8a8"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    charts.get(j).setDescriptionTypeface(getResources().getFont(R.font.bevietnambold));
                }
                final int finalJ = j;
                eyeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (charts.get(finalJ).isDrawValueAboveBarEnabled()){
                            eyeButton.setBackground(getResources().getDrawable(R.drawable.eye_closed));
                            if (isDarkThemeEnabled)
                                eyeButton.getBackground().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
                            for (BarChart chart:charts){
                                chart.setDrawValueAboveBar(false);
                                chart.getBarData().setDrawValues(false);
                            }

                        }
                        else {
                            eyeButton.setBackground(getResources().getDrawable(R.drawable.eye_open));
                            if (isDarkThemeEnabled)
                                eyeButton.getBackground().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
                            for (BarChart chart:charts){
                                chart.setDrawValueAboveBar(true);
                                chart.getBarData().setDrawValues(true);
                            }
                        }
                        for (BarChart chart:charts){
                            chart.invalidate();
                        }
                    }
                });
            }
        } else {
            Toast.makeText(getContext(), "C'è stato un errore nella ricezione dei dati. " +
                    "Contatta lo sviluppatore per maggiori informazioni.", Toast.LENGTH_LONG);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        data = new DataHelper();
        new Connection().execute();
    }

    private String getRightDate(String date){
        try {
            String day = date.substring(8,10);
            String month = date.substring(5,7);

            switch (month) {
                case "01":
                    month = "gen";
                    break;
                case "02":
                    month = "feb";
                    break;
                case "03":
                    month = "mar";
                    break;
                case "04":
                    month = "apr";
                    break;
                case "05":
                    month = "mag";
                    break;
                case "06":
                    month = "giu";
                    break;
                case "07":
                    month = "lug";
                    break;
                case "08":
                    month = "ago";
                    break;
                case "09":
                    month = "set";
                    break;
                case "10":
                    month = "ott";
                    break;
                case "11":
                    month = "nov";
                    break;
                case "12":
                    month = "dic";
                    break;
            }
            if (day.startsWith("0"))
                day = day.substring(1,2);

            return day + " " + month;

        } catch (Exception e){

        }
        return date;
    }


}
