package com.example.stanko.evidencijarobeumagacinu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class DodavanjeRobeActivity extends AppCompatActivity {

    private EditText unesiNaziv;
    private EditText unesiTezinu;
    private EditText unesiKolicinu;
    private EditText unesiNapomenu;
    private Spinner spinerSkladista;
    private Spinner spinerTipova;
    private ArrayList<String> skladista = new ArrayList<>();
    private ArrayList<String> tipovi = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodavanje_robe);

        unesiNaziv = ((EditText)findViewById(R.id.unesiNazivRobe));
        unesiTezinu = ((EditText)findViewById(R.id.unesiTezinuRobe));
        unesiKolicinu = ((EditText)findViewById(R.id.unesiKolicinuRobe));
        unesiNapomenu = ((EditText)findViewById(R.id.unesiNapomenuRobe));

        spinerSkladista = findViewById(R.id.unesiSkladiste);
        skladista.add("Skladište 1");
        skladista.add("Skladište 2");
        skladista.add("Skladište 3");
        ArrayAdapter adapterSkladista = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, skladista);
        spinerSkladista.setAdapter(adapterSkladista);

        spinerTipova = findViewById(R.id.unesiTipRobe);
        tipovi.add("Voće");
        tipovi.add("Povrće");
        tipovi.add("Piće");
        ArrayAdapter adapterTipova = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipovi);
        spinerTipova.setAdapter(adapterTipova);

        Button dodajRobu = (Button) findViewById(R.id.unesiDodajRobu);
        dodajRobu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View view) {
                API.getJSON("http://10.0.2.2:5000/roba", new ReadDataHandler(){
                    @Override
                    public void handleMessage(Message msg) {
                        String odgovor = getJson();
                        try {
                            JSONArray array = new JSONArray(odgovor);
                            //odgovor spakujemo u JSONArray, i onda parsiramo da bismo dobili linked list
                            ArrayList<RobaModel> roba = RobaModel.parseJSONArray(array);

                            TextView dodavanjeGreska = findViewById(R.id.dodavanjeGreska);
                            dodavanjeGreska.setTextColor(Color.RED);
                            String naziv = unesiNaziv.getText().toString();
                            String skladiste = spinerSkladista.getSelectedItem().toString();
                            String tip = spinerTipova.getSelectedItem().toString();
                            String napomena = unesiNapomenu.getText().toString();
                            if (naziv.isEmpty() || tip.isEmpty() || unesiTezinu.getText().toString().isEmpty() || unesiKolicinu.getText().toString().isEmpty() || napomena.isEmpty()) {
                                dodavanjeGreska.setText("Sva polja moraju biti popunjena");
                                return;
                            }

                            int tezina = Integer.parseInt(unesiTezinu.getText().toString());
                            int kolicina = Integer.parseInt(unesiKolicinu.getText().toString());
                            if (tezina == 0){
                                dodavanjeGreska.setText("Težina robe mora biti veća od 0");
                                unesiTezinu.setText("");
                                unesiTezinu.requestFocus();
                                return;
                            }
                            if (kolicina == 0){
                                dodavanjeGreska.setText("Količina robe mora biti veća od 0");
                                unesiKolicinu.setText("");
                                unesiKolicinu.requestFocus();
                                return;
                            }

                            dodavanjeGreska.setText("");

                            List<API.Element> data = new ArrayList<>();
                            //formiranje objekta
                            if(roba.size() == 0)
                                data.add(new API.Element("id", "0"));
                            else
                                data.add(new API.Element("id", Integer.toString(Integer.parseInt(roba.get(roba.size()-1).getId())+1)));
                            data.add(new API.Element("idSkladista", Integer.toString(skladista.indexOf(skladiste))));
                            data.add(new API.Element("naziv", naziv));
                            data.add(new API.Element("tip", tip));
                            data.add(new API.Element("tezina", Integer.toString(tezina)));
                            data.add(new API.Element("kolicina", Integer.toString(kolicina)));
                            data.add(new API.Element("napomena", napomena));

                            API.postDataJSON("http://10.0.2.2:5000/add", data, new ReadDataHandler(){
                                @Override
                                public void handleMessage(Message msg) {
                                    System.out.println(getJson());
                                }
                            });
                            unesiNaziv.setText("");
                            unesiTezinu.setText("");
                            unesiKolicinu.setText("");
                            unesiNapomenu.setText("");
                            Toast.makeText(getApplicationContext(), "Roba je uspiješno dodata!", Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DodavanjeRobeActivity.this, HomeActivity.class));
    }
}
