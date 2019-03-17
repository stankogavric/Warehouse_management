package com.example.stanko.evidencijarobeumagacinu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RobaModel {
    private String id, idSkladista, naziv, tip, tezina, kolicina, napomena;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdSkladista() {
        return idSkladista;
    }

    public void setIdSkladista(String idSkladista) {
        this.idSkladista = idSkladista;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv (String naziv) {
        this.naziv = naziv;
    }

    public String getTip() { return tip; }

    public void setTip(String tip) { this.tip = tip; }

    public String getTezina() { return tezina; }

    public void setTezina (String tezina) { this.tezina = tezina; }

    public String getKolicina() {
        return kolicina;
    }

    public void setKolicina(String kolicina) {
        this.kolicina = kolicina;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena (String napomena) {
        this.napomena = napomena;
    }

    public RobaModel(String id, String idSkladista, String naziv, String tip, String tezina, String kolicina, String napomena) {
        this.id = id;
        this.idSkladista = idSkladista;
        this.naziv = naziv;
        this.tip = tip;
        this.tezina = tezina;
        this.kolicina = kolicina;
        this.napomena = napomena;
    }

    public RobaModel(){

    }

    public static RobaModel parseJSON(JSONObject object){
        RobaModel model = new RobaModel();

        try{
            if(object.has("id")){
                model.setId(object.getString("id"));
            }

            if(object.has("idSkladista")){
                model.setIdSkladista(object.getString("idSkladista"));
            }

            if(object.has("naziv")){
                model.setNaziv(object.getString("naziv"));
            }

            if(object.has("tip")){
                model.setTip(object.getString("tip"));
            }

            if(object.has("tezina")){
                model.setTezina(object.getString("tezina"));
            }

            if(object.has("kolicina")){
                model.setKolicina(object.getString("kolicina"));
            }

            if(object.has("napomena")){
                model.setNapomena(object.getString("napomena"));
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return model;
    }

    public static ArrayList<RobaModel> parseJSONArray(JSONArray array){
        ArrayList<RobaModel> list = new ArrayList<>();
        try{
            for(int i = 0; i < array.length(); i++){
                RobaModel model = parseJSON(array.getJSONObject(i));
                list.add(model);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return list;
    }
}
