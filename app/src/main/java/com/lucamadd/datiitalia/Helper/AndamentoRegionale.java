package com.lucamadd.datiitalia.Helper;


public class AndamentoRegionale extends AndamentoNazionale {


    private int codice_regione;
    private String denominazione_regione;
    private double lat;
    private double lon;

    public AndamentoRegionale(String data, String stato, int codice_regione, String denominazione_regione,
                              double lat, double lon, int ricoverati_con_sintomi, int terapia_intensiva,
                              int totale_ospedalizzati, int isolamento_domiciliare,
                              int totale_positivi, int variazione_totale_positivi,
                              int nuovi_positivi, int dimessi_guariti, int deceduti,
                              int totale_casi, int tamponi, String note_it, String note_en){
        super(data,stato,ricoverati_con_sintomi,terapia_intensiva,totale_ospedalizzati,isolamento_domiciliare,
                totale_positivi,variazione_totale_positivi,nuovi_positivi,dimessi_guariti,deceduti,
                totale_casi,tamponi,note_it,note_en);
        setCodice_regione(codice_regione);
        setDenominazione_regione(denominazione_regione);
    }

    public void setCodice_regione(int codice_regione){
        this.codice_regione = codice_regione;
    }
    public void setDenominazione_regione(String denominazione_regione){
        this.denominazione_regione = denominazione_regione;
    }


    public int getCodice_regione(){
        return codice_regione;
    }

    public String getDenominazione_regione(){
        return denominazione_regione;
    }
}
