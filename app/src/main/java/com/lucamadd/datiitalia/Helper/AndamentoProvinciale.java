package com.lucamadd.datiitalia.Helper;

public class AndamentoProvinciale {

    private String data;
    private String stato;
    private int codice_regione;
    private String denominazione_regione;
    private int codice_provincia;
    private String denominazione_provincia;
    private String sigla_provincia;
    private double lat;
    private double lon;
    private int totale_casi;
    private String note_it;
    private String note_en;

    public AndamentoProvinciale(String data, String stato, int codice_regione, String denominazione_regione,
                                int codice_provincia, String denominazione_provincia, String sigla_provincia,
                                int lat, int lon, int totale_casi, String note_it, String note_en){
        setData(data);
        setStato(stato);
        setCodice_regione(codice_regione);
        setDenominazione_regione(denominazione_regione);
        setCodice_provincia(codice_provincia);
        setDenominazione_provincia(denominazione_provincia);
        setSigla_provincia(sigla_provincia);
        setLat(lat);
        setLon(lon);
        setTotale_casi(totale_casi);
        setNote_en(note_en);
        setNote_it(note_it);
    }

    public void setCodice_regione(int codice_regione) {
        this.codice_regione = codice_regione;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setCodice_provincia(int codice_provincia) {
        this.codice_provincia = codice_provincia;
    }

    public void setDenominazione_provincia(String denominazione_provincia) {
        this.denominazione_provincia = denominazione_provincia;
    }

    public void setDenominazione_regione(String denominazione_regione) {
        this.denominazione_regione = denominazione_regione;
    }

    public void setSigla_provincia(String sigla_provincia) {
        this.sigla_provincia = sigla_provincia;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public void setTotale_casi(int totale_casi) {
        this.totale_casi = totale_casi;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public void setNote_en(String note_en) {
        this.note_en = note_en;
    }

    public void setNote_it(String note_it) {
        this.note_it = note_it;
    }

    public int getCodice_provincia() {
        return codice_provincia;
    }

    public int getCodice_regione() {
        return codice_regione;
    }

    public double getLat() {
        return lat;
    }

    public String getData() {
        return data;
    }

    public int getTotale_casi() {
        return totale_casi;
    }

    public String getDenominazione_provincia() {
        return denominazione_provincia;
    }

    public String getDenominazione_regione() {
        return denominazione_regione;
    }

    public double getLon() {
        return lon;
    }

    public String getSigla_provincia() {
        return sigla_provincia;
    }

    public String getStato() {
        return stato;
    }

    public String getNote_en() {
        return note_en;
    }

    public String getNote_it() {
        return note_it;
    }
}
