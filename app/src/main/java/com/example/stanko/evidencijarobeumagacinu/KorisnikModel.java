package com.example.stanko.evidencijarobeumagacinu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class KorisnikModel {
    private String ime, prezime, id, pin;

    public KorisnikModel(String ime, String prezime, String id, String pin) {
        this.ime = ime;
        this.prezime = prezime;
        this.id = id;
        this.pin = pin;
    }

    public KorisnikModel(){

    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public static KorisnikModel parseJSON(JSONObject object){
        KorisnikModel model = new KorisnikModel();

        try{
            if(object.has("ime")){
                model.setIme(object.getString("ime"));
            }

            if(object.has("prezime")){
                model.setPrezime(object.getString("prezime"));
            }

            if(object.has("id")){
                model.setId(object.getString("id"));
            }

            if(object.has("pin")){
                model.setPin(object.getString("pin"));
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return model;
    }

    public static ArrayList<KorisnikModel> parseJSONArray(JSONArray array){
        ArrayList<KorisnikModel> list = new ArrayList<>();
        try{
            for(int i = 0; i < array.length(); i++){
                KorisnikModel model = parseJSON(array.getJSONObject(i));
                list.add(model);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return list;
    }
}
