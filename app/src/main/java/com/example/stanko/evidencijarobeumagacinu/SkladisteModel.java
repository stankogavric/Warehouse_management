package com.example.stanko.evidencijarobeumagacinu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SkladisteModel {
    private String id, naziv;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv (String naziv) {
        this.naziv = naziv;
    }

    public SkladisteModel(String id, String naziv) {
        this.id = id;
        this.naziv = naziv;
    }

    public SkladisteModel(){

    }

    public static SkladisteModel parseJSON(JSONObject object){
        SkladisteModel model = new SkladisteModel();

        try{
            if(object.has("id")){
                model.setId(object.getString("id"));
            }

            if(object.has("naziv")){
                model.setNaziv(object.getString("naziv"));
            }
        }
        catch(Exception e){

        }
        return model;
    }

    public static ArrayList<SkladisteModel> parseJSONArray(JSONArray array){
        ArrayList<SkladisteModel> list = new ArrayList<>();
        try{
            for(int i = 0; i < array.length(); i++){
                SkladisteModel model = parseJSON(array.getJSONObject(i));
                list.add(model);
            }
        }
        catch(Exception e){

        }
        return list;
    }
}
