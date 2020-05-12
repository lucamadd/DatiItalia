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
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lucamadd.datiitalia.Helper.AndamentoNazionale;
import com.lucamadd.datiitalia.Helper.DataHelper;
import com.lucamadd.datiitalia.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GraphFragment extends Fragment {

    private BarChart chart1;
    private BarChart chart2;
    private BarChart chart3;
    private BarChart chart4;
    private DataHelper data;
    private ArrayList<AndamentoNazionale> dati = null;
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
        if (isDarkThemeEnabled)
            setDarkTheme();
        else
            eyeButton.getBackground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
        return root;
    }

    private void setDarkTheme(){
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
    }

    private class Connection extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            data.getAllData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("onPostExecute()","iniziato");
            dati = data.getGraphData();
            setData(dati);
            Log.i("onPostExecute()","terminato");

        }
    }

    private void setData(ArrayList<AndamentoNazionale> dati){
        if (dati !=null){
            final ArrayList<BarChart> charts = new ArrayList<>();
            charts.add(chart1);
            charts.add(chart2);
            charts.add(chart3);
            charts.add(chart4);
            ArrayList<BarEntry> nuoviCasi = new ArrayList<>();
            ArrayList<BarEntry> deceduti = new ArrayList<>();
            ArrayList<BarEntry> guariti = new ArrayList<>();
            ArrayList<BarEntry> totaleCasi = new ArrayList<>();

            ArrayList<ArrayList<BarEntry>> entries = new ArrayList<>();
            entries.add(nuoviCasi);
            entries.add(deceduti);
            entries.add(guariti);
            entries.add(totaleCasi);

            ArrayList<String> year = new ArrayList<>();
            for (int i=0;i<dati.size();i++){
                nuoviCasi.add(new BarEntry(dati.get(i).getNuovi_positivi(),i));
                deceduti.add(new BarEntry(dati.get(i).getDeceduti(),i));
                guariti.add(new BarEntry(dati.get(i).getDimessi_guariti(),i));
                totaleCasi.add(new BarEntry(dati.get(i).getTotale_casi(),i));
                year.add(getRightDate(dati.get(i).getData()));
                Log.i(dati.get(i).getData(),dati.get(i).getNuovi_positivi() + "");
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
