package com.lucamadd.datiitalia.Helper;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataHelper {

    private String currentDayText;

    private static final String URL_ANDAMENTO_NAZIONALE_LATEST = "https://raw.githubusercontent.com/" +
            "pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-andamento-nazionale-latest.json";

    private static final String URL_ANDAMENTO_NAZIONALE = "https://raw.githubusercontent.com/" +
            "pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-andamento-nazionale.json";

    private static final String URL_ANDAMENTO_REGIONALE_LATEST = "https://raw.githubusercontent.com/" +
            "pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-regioni-latest.json";

    private static final String URL_ANDAMENTO_REGIONALE = "https://raw.githubusercontent.com/pcm-dpc/" +
            "COVID-19/master/dati-json/dpc-covid19-ita-regioni.json";

    private static final String URL_ANDAMENTO_PROVINCIALE_LATEST = "https://raw.githubusercontent.com/" +
            "pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-province-latest.json";

    private static final String URL_ANDAMENTO_PROVINCIALE = "https://raw.githubusercontent.com/" +
            "pcm-dpc/COVID-19/master/dati-json/dpc-covid19-ita-province.json";

    private OkHttpClient client = new OkHttpClient();


    private AndamentoNazionale datiNazionali = null;
    private AndamentoNazionale variazioneDatiNazionali = null;

    private ArrayList<AndamentoRegionale> datiRegionali = null;
    private ArrayList<AndamentoRegionale> variazioneDatiRegionali = null;

    private ArrayList<AndamentoProvinciale> datiProvinciali = null;
    private ArrayList<AndamentoProvinciale> variazioneDatiProvinciali = null;

    private ArrayList<AndamentoNazionale> graphData = null;
    private ArrayList<AndamentoRegionale> graphRegionData = null;


    @SuppressWarnings("deprecation")
    public void getData(){
        Log.i("getData()","iniziato");
        try {
            String httpResponse = run(URL_ANDAMENTO_NAZIONALE_LATEST);
            if (httpResponse != null){
                JsonParser parser = new JsonParser();
                JsonArray jArray = (JsonArray) parser.parse(httpResponse);
                Gson gson = new Gson();
                AndamentoNazionale dati = gson.fromJson(jArray.get(0).toString(),
                        AndamentoNazionale.class);
                if (dati!=null){
                    setDatiNazionali(dati);
                    setDay(dati.getData());
                }
            } else {
                setDatiNazionali(null);
            }
        } catch (JsonSyntaxException e) {
            Log.i("ERRORE ","JSON");
        }
        Log.i("getData()","terminato");
    }



    public void getMoreData(){
        Log.i("getMoreData()","iniziato");

        try {
            String httpResponse = run(URL_ANDAMENTO_NAZIONALE);
            if (httpResponse != null){
                JsonParser parser = new JsonParser();
                JsonArray jArray = (JsonArray) parser.parse(httpResponse);
                Gson gson = new Gson();
                AndamentoNazionale datiNuovi = gson.fromJson(jArray.get(jArray.size()-1)
                        .toString(), AndamentoNazionale.class);
                AndamentoNazionale datiVecchi = gson.fromJson(jArray.get(jArray.size()-2)
                        .toString(), AndamentoNazionale.class);
                variazioneDatiNazionali = new AndamentoNazionale(null,null,
                        datiNuovi.getRicoverati_con_sintomi() - datiVecchi.getRicoverati_con_sintomi(),
                        datiNuovi.getTerapia_intensiva() - datiVecchi.getTerapia_intensiva(), datiNuovi.getTotale_ospedalizzati() - datiVecchi.getTotale_ospedalizzati(),
                        datiNuovi.getIsolamento_domiciliare() - datiVecchi.getIsolamento_domiciliare(), datiNuovi.getTotale_positivi() - datiVecchi.getTotale_positivi(),
                        0, 0, datiNuovi.getDimessi_guariti() - datiVecchi.getDimessi_guariti(),
                        datiNuovi.getDeceduti() - datiVecchi.getDeceduti(),0, datiVecchi.getTamponi() - datiNuovi.getTamponi(), null, null);
            }
        } catch(JsonSyntaxException e){
            Log.i("ERRORE ","JSON");
        }
        Log.i("getMoreData()","terminato");

    }

    public void getAllData(){
        try {
            String httpResponse = run(URL_ANDAMENTO_NAZIONALE);
            if (httpResponse != null){
                JsonParser parser = new JsonParser();
                JsonArray jArray = (JsonArray) parser.parse(httpResponse);
                Gson gson = new Gson();
                ArrayList<AndamentoNazionale> dati = new ArrayList<>();
                for (int i=0;i<jArray.size();i++){
                    dati.add(gson.fromJson(jArray.get(i)
                            .toString(), AndamentoNazionale.class));
                }
                graphData = dati;
                }
        } catch(JsonSyntaxException e){
            Log.i("ERRORE ","JSON");
        }

    }

    @SuppressWarnings("deprecation")
    public void getRegioniDataLatest(){

        try {
            String httpResponse = run(URL_ANDAMENTO_REGIONALE_LATEST);
            if (httpResponse != null){
                JsonParser parser = new JsonParser();
                JsonArray jArray = (JsonArray) parser.parse(httpResponse);
                Gson gson = new Gson();
                ArrayList<AndamentoRegionale> datiRegioni = new ArrayList<>();
                for (int i=0; i<jArray.size();i++){
                    datiRegioni.add(gson.fromJson(jArray.get(i).toString(),
                            AndamentoRegionale.class));
                }
                if (datiRegioni != null){
                    setDatiRegionali(datiRegioni);
                    setDay(datiRegioni.get(0).getData());
                }
            }
        } catch (JsonSyntaxException e) {
            Log.i("ERRORE ","JSON");
        } catch (Exception ex){
            Log.e("Error in getMoreData()",ex.getLocalizedMessage()+" d");
        }


    }



    public void getMoreRegioniData(){

        String response = run(URL_ANDAMENTO_REGIONALE);
        if (response != null){
            try {

                JsonParser parser = new JsonParser();
                JsonArray jArray = (JsonArray) parser.parse(response);
                Gson gson = new Gson();
                        /*
                         public AndamentoNazionale(String data, String stato,int ricoverati_con_sintomi, int terapia_intensiva,
                          int totale_ospedalizzati, int isolamento_domiciliare,
                          int totale_positivi, int variazione_totale_positivi,
                          int nuovi_positivi, int dimessi_guariti, int deceduti,
                          int totale_casi, int tamponi, String note_it, String note_en){
                         */
                ArrayList<AndamentoRegionale> datiNuovi = new ArrayList<>();
                ArrayList<AndamentoRegionale> datiVecchi = new ArrayList<>();
                ArrayList<AndamentoRegionale> tempRegionData = new ArrayList<>();
                            /*
                            ArrayList<AndamentoRegionale> datiRegioni = new ArrayList<>();
                            for (int i=0; i<jArray.size();i++){
                                datiRegioni.add(gson.fromJson(jArray.get(i).toString(),
                                        AndamentoRegionale.class));
                            }
                             */
                for (int i=jArray.size()-21;i<jArray.size();i++){
                    //Log.i("first for" , jArray.get(i).toString());
                    datiNuovi.add(gson.fromJson(jArray.get(i).toString(),
                            AndamentoRegionale.class));
                }

                for (int i=jArray.size()-42;i<jArray.size()-21;i++){
                    //Log.i("second for" , jArray.get(i).toString());
                    datiVecchi.add(gson.fromJson(jArray.get(i).toString(),
                            AndamentoRegionale.class));
                }

                for (int i=0;i<jArray.size();i++){
                    //Log.i("second for" , jArray.get(i).toString());
                    tempRegionData.add(gson.fromJson(jArray.get(i).toString(),
                            AndamentoRegionale.class));
                }
                graphRegionData = tempRegionData;

                variazioneDatiRegionali = new ArrayList<>();
                Log.i("sizes","datinuovi.size() is "+datiNuovi.size()+"\ndativecchi.size() is "+datiVecchi.size());
                for (int i=0;i<21;i++){
                                /*
                                variazioneDatiNazionali = new AndamentoNazionale(null,null,
                                    datiNuovi.getRicoverati_con_sintomi() - datiVecchi.getRicoverati_con_sintomi(),
                                    datiNuovi.getTerapia_intensiva() - datiVecchi.getTerapia_intensiva(), datiNuovi.getTotale_ospedalizzati() - datiVecchi.getTotale_ospedalizzati(),
                                    datiNuovi.getIsolamento_domiciliare() - datiVecchi.getIsolamento_domiciliare(), datiNuovi.getTotale_positivi() - datiVecchi.getTotale_positivi(),
                                    0, 0, datiNuovi.getDimessi_guariti() - datiVecchi.getDimessi_guariti(),
                                    datiNuovi.getDeceduti() - datiVecchi.getDeceduti(),0, datiVecchi.getTamponi() - datiNuovi.getTamponi(), null, null);

                                 */
                    variazioneDatiRegionali.add(new AndamentoRegionale( null, null,
                            datiNuovi.get(i).getCodice_regione(), datiNuovi.get(i).getDenominazione_regione(),
                            0,0,datiNuovi.get(i).getRicoverati_con_sintomi() - datiVecchi.get(i).getRicoverati_con_sintomi(),
                            datiNuovi.get(i).getTerapia_intensiva() - datiVecchi.get(i).getTerapia_intensiva(),
                            datiNuovi.get(i).getTotale_ospedalizzati() - datiVecchi.get(i).getTotale_ospedalizzati(),
                            datiNuovi.get(i).getIsolamento_domiciliare() - datiVecchi.get(i).getIsolamento_domiciliare(),
                            datiNuovi.get(i).getTotale_positivi() - datiVecchi.get(i).getTotale_positivi(),
                            0, datiNuovi.get(i).getNuovi_positivi() - datiVecchi.get(i).getNuovi_positivi(),
                            datiNuovi.get(i).getDimessi_guariti() - datiVecchi.get(i).getDimessi_guariti(),
                            datiNuovi.get(i).getDeceduti() - datiVecchi.get(i).getDeceduti(),
                            datiNuovi.get(i).getTotale_casi() - datiNuovi.get(i).getTotale_casi(),
                            datiNuovi.get(i).getTamponi() - datiVecchi.get(i).getTamponi(),null,null));
                }


            } catch(JsonSyntaxException e){

            }
        }

    }

    @SuppressWarnings("deprecation")
    public void getProvinceDataLatest(){
        try {
            String httpResponse = run(URL_ANDAMENTO_PROVINCIALE_LATEST);
            if (httpResponse != null){
                //System.out.println(httpResponse);
                JsonParser parser = new JsonParser();
                JsonArray jArray = (JsonArray) parser.parse(httpResponse);
                Gson gson = new Gson();
                ArrayList<AndamentoProvinciale> datiProvince = new ArrayList<>();
                Log.i("Dim array",jArray.size()+"  d");
                for (int i=0; i<jArray.size();i++){
                    datiProvince.add(gson.fromJson(jArray.get(i).toString(),
                            AndamentoProvinciale.class));
                }
                if (datiProvince != null){
                    setDatiProvinciali(datiProvince);
                    setDay(datiProvinciali.get(0).getData());

                }
            }
        } catch (JsonSyntaxException e) {
            setDatiProvinciali(null);
            Log.i("ERRORE JSON:",e.getLocalizedMessage()+" d");
        } catch (Exception ex){
            Log.i("ERRORE :",ex.getLocalizedMessage()+" d");
        }

    }



        public void getMoreProvinceData(){
            try {
                String response = run(URL_ANDAMENTO_PROVINCIALE);
                if (response != null){
                    JsonParser parser = new JsonParser();
                    JsonArray jArray = (JsonArray) parser.parse(response);
                    Gson gson = new Gson();

                    ArrayList<AndamentoProvinciale> datiNuovi = new ArrayList<>();
                    ArrayList<AndamentoProvinciale> datiVecchi = new ArrayList<>();

                    Log.i("Dim array",jArray.size()+"  e");
                    for (int i=jArray.size()-149;i<jArray.size();i++){
                        //Log.i("first for" , jArray.get(i).toString());
                        datiNuovi.add(gson.fromJson(jArray.get(i).toString(),
                                AndamentoProvinciale.class));
                    }

                    for (int i=jArray.size()-298;i<jArray.size()-149;i++){
                        //Log.i("second for" , jArray.get(i).toString());
                        datiVecchi.add(gson.fromJson(jArray.get(i).toString(),
                                AndamentoProvinciale.class));
                    }

                    variazioneDatiProvinciali = new ArrayList<>();
                    //Log.i("sizes","datinuovi.size() is "+datiNuovi.size()+"\ndativecchi.size() is "+datiVecchi.size());
                    for (int i=0;i<149;i++) {
                        Log.i("debugtag","\n\nnuovi: " + datiNuovi.get(i).getTotale_casi() + "("+datiNuovi.get(i).getDenominazione_provincia()+")" + "\nvecchi: " + datiVecchi.get(i).getTotale_casi()+ "("+datiVecchi.get(i).getDenominazione_provincia()+")");
                        variazioneDatiProvinciali.add(new AndamentoProvinciale(null, null, datiNuovi.get(i).getCodice_regione(),datiNuovi.get(i).getDenominazione_regione(),
                                datiNuovi.get(i).getCodice_provincia(),datiNuovi.get(i).getDenominazione_provincia(),datiNuovi.get(i).getSigla_provincia(),
                                0,0,datiNuovi.get(i).getTotale_casi() - datiVecchi.get(i).getTotale_casi(),null,null));
                    }
                }
                else {
                    Log.i("ERRORE NELLA SECONDA ","RISPOSTA");
                }
            } catch(JsonSyntaxException e){

            }
        }


    public AndamentoNazionale getDatiNazionali() {
        return datiNazionali;
    }

    public AndamentoNazionale getVariazioneDatiNazionali() {
        return variazioneDatiNazionali;
    }

    public ArrayList<AndamentoRegionale> getDatiRegionali() {
        return datiRegionali;
    }

    public ArrayList<AndamentoRegionale> getVariazioneDatiRegionali() {
        return variazioneDatiRegionali;
    }

    public ArrayList<AndamentoProvinciale> getDatiProvinciali() {
        return datiProvinciali;
    }

    public ArrayList<AndamentoProvinciale> getVariazioneDatiProvinciali() {
        return variazioneDatiProvinciali;
    }

    public ArrayList<AndamentoNazionale> getGraphData() { return graphData; }

    public ArrayList<AndamentoRegionale> getGraphRegionData() {
        /*
        String response = run(URL_ANDAMENTO_REGIONALE);
        if (response != null){
            try {
                JsonParser parser = new JsonParser();
                JsonArray jArray = (JsonArray) parser.parse(response);
                Gson gson = new Gson();

                ArrayList<AndamentoRegionale> tempRegionData = new ArrayList<>();

                for (int i=0;i<jArray.size();i++){
                    //Log.i("second for" , jArray.get(i).toString());
                    tempRegionData.add(gson.fromJson(jArray.get(i).toString(),
                            AndamentoRegionale.class));
                }
                graphRegionData = tempRegionData;

            }
            catch(JsonSyntaxException e){

            }
        }

         */
        Log.i("graphregiondata","graphRegionData.size() is "+ (graphRegionData == null? "null": graphRegionData.size()));
        return graphRegionData;
    }

    public void setDatiNazionali(AndamentoNazionale dati) {
        this.datiNazionali = dati;
    }
    public void setDatiRegionali(ArrayList<AndamentoRegionale> dati) {
        this.datiRegionali = dati;
    }
    public void setDatiProvinciali(ArrayList<AndamentoProvinciale> dati) {
        this.datiProvinciali = dati;
    }


    private String run(String url) {
        Log.i("run()","iniziato");
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e){
            Log.e("ERROR",e.getLocalizedMessage());
            return null;
        }
    }

    private void setDay(String day_){
        try {
            String day = day_.substring(8,10);
            String month = day_.substring(5,7);
            String year = day_.substring(0,4);
            String complete_date = day + "/" + month + "/" + year;

            if (complete_date.length() == 10){
                currentDayText = "Totale casi (agg. al " + complete_date + ")";
            } else {
                DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
                currentDayText = "Totale casi (agg. al " + df.format(new Date()) + ")";
            }
        } catch (Exception e){
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
            currentDayText = "Totale casi (agg. al " + df.format(new Date()) + ")";
        }
    }

    public String getCurrentDayText(){
        return currentDayText;
    }


}
