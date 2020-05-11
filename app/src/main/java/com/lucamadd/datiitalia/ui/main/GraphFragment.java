package com.lucamadd.datiitalia.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

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

    private BarChart chart;
    private DataHelper data;
    private ArrayList<AndamentoNazionale> dati = null;
    private Button eyeButton;

    private boolean isDarkThemeEnabled;
    private LinearLayout graphLayout;
    private TextView andamentoTextView;



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
        chart = root.findViewById(R.id.barchart);
        eyeButton = root.findViewById(R.id.eye_button);
        graphLayout = root.findViewById(R.id.graph_layout);
        andamentoTextView = root.findViewById(R.id.andamento_tv_title);
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
            ArrayList<BarEntry> NoOfEmp = new ArrayList<>();

            ArrayList<String> year = new ArrayList<>();
            for (int i=0;i<dati.size();i++){
                NoOfEmp.add(new BarEntry(dati.get(i).getNuovi_positivi(),i));
                year.add(getRightDate(dati.get(i).getData()));
                Log.i(dati.get(i).getData(),dati.get(i).getNuovi_positivi() + "");
            }
            BarDataSet bardataset = new BarDataSet(NoOfEmp, "Nuovi casi");
            chart.animateY(500);
            chart.getAxisLeft().setAxisMinValue(0);
            chart.getAxisLeft().setDrawGridLines(false);
            chart.getAxisLeft().setDrawAxisLine(true);
            if (isDarkThemeEnabled)
                chart.getAxisLeft().setTextColor(Color.parseColor("#a8a8a8"));
            chart.getAxisRight().setEnabled(false);
            chart.setDescription("");
            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            chart.getXAxis().setDrawGridLines(false);
            if (isDarkThemeEnabled)
                chart.getXAxis().setTextColor(Color.parseColor("#a8a8a8"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                chart.getXAxis().setTypeface(getResources().getFont(R.font.bevietnambold));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                chart.getAxisLeft().setTypeface(getResources().getFont(R.font.bevietnambold));
            }
            chart.setNoDataText("Description that you want");
            Paint p = chart.getPaint(Chart.PAINT_INFO);
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
                chart.getLegend().setTypeface(getResources().getFont(R.font.bevietnambold));
            }
            if (isDarkThemeEnabled)
                chart.getLegend().setTextColor(Color.parseColor("#a8a8a8"));
            chart.setData(data);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                chart.getBarData().setValueTypeface(getResources().getFont(R.font.bevietnambold));
            }
            if (isDarkThemeEnabled)
                chart.getBarData().setValueTextColor(Color.parseColor("#a8a8a8"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                chart.setDescriptionTypeface(getResources().getFont(R.font.bevietnambold));
            }
            eyeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (chart.isDrawValueAboveBarEnabled()){
                        eyeButton.setBackground(getResources().getDrawable(R.drawable.eye_closed));
                        if (isDarkThemeEnabled)
                            eyeButton.getBackground().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
                        chart.setDrawValueAboveBar(false);
                        chart.getBarData().setDrawValues(false);
                    }
                    else {
                        eyeButton.setBackground(getResources().getDrawable(R.drawable.eye_open));
                        if (isDarkThemeEnabled)
                            eyeButton.getBackground().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
                        chart.setDrawValueAboveBar(true);
                        chart.getBarData().setDrawValues(true);
                    }
                    chart.invalidate();
                }
            });
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
