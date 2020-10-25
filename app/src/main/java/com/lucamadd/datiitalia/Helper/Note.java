package com.lucamadd.datiitalia.Helper;

public class Note {
    private String data;
    private String note;

    public Note(String data, String note){
        setData(data);
        setNote(note);
    }

    public void setData(String data){
        this.data = data;
    }

    public void setNote(String note){
        this.note = note;
    }

    public String getData(){
        if (data != null)
            return data;
        return "";
    }

    public String getNote(){
        if (note != null)
            return note;
        return "";
    }
}
