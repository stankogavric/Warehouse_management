package com.example.stanko.evidencijarobeumagacinu;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class PretragaActivity extends AppCompatActivity {
    private Spinner spinerTipova;
    private ArrayList<String> tipovi = new ArrayList<>();
    private LinearLayout rezultatiPretrage = null;
    private TextView poruka = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretraga);

        poruka = findViewById(R.id.nemaRezultata);

        spinerTipova = findViewById(R.id.pretragaTip);
        tipovi.add("Svi tipovi");
        tipovi.add("Voće");
        tipovi.add("Povrće");
        tipovi.add("Piće");
        ArrayAdapter adapterTipova = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipovi);
        spinerTipova.setAdapter(adapterTipova);

        Button trazi = findViewById(R.id.pretragaTrazi);
        trazi.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View view) {
                final String nazivRobe = ((EditText)findViewById(R.id.pretragaNaziv)).getText().toString();
                final String tipRobe = spinerTipova.getSelectedItem().toString();

                poruka.setText("");

                rezultatiPretrage = findViewById(R.id.rezultatiPretrage);
                rezultatiPretrage.removeAllViews();

                API.getJSON("http://10.0.2.2:5000/roba", new ReadDataHandler(){
                    @Override
                    public void handleMessage(Message msg) {
                        String odgovor = getJson();
                        try {
                            JSONArray array = new JSONArray(odgovor);
                            //odgovor spakujemo u JSONArray, i onda parsiramo da bismo dobili linked list
                            ArrayList<RobaModel> roba = RobaModel.parseJSONArray(array);

                            for (RobaModel r : roba) {
                                if (tipRobe.equals("Svi tipovi") && r.getNaziv().contains(nazivRobe))
                                    ispisiRezultate(r);
                                else if(tipRobe.equals("Svi tipovi") && nazivRobe.isEmpty())
                                    ispisiRezultate(r);
                                else if (tipRobe.equals(r.getTip()) && nazivRobe.isEmpty())
                                    ispisiRezultate(r);
                                else if (tipRobe.equals(r.getTip()) && r.getNaziv().contains(nazivRobe))
                                    ispisiRezultate(r);
                            }
                            if (rezultatiPretrage.getChildCount() == 0) {
                                poruka.setText("Nema rezultata pretrage");
                            }
                        }
                        catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void ispisiRezultate(RobaModel r){
        LinearLayout item = (LinearLayout)getLayoutInflater().inflate(R.layout.roba, null);

        ImageView icon = item.findViewById(R.id.icon);
        if (r.getTip().equals("Piće"))
            icon.setImageResource(R.drawable.drink);
        else if(r.getTip().equals("Voće"))
            icon.setImageResource(R.drawable.fruit);
        else if(r.getTip().equals("Povrće"))
            icon.setImageResource(R.drawable.vegetables);
        TextView naziv = (TextView) item.findViewById(R.id.nazivRobe);
        naziv.setKeyListener(null);
        naziv.setText(r.getNaziv());
        TextView tip = (TextView) item.findViewById(R.id.tipRobe);
        tip.setKeyListener(null);
        tip.setText(r.getTip());
        TextView tezina = (TextView) item.findViewById(R.id.tezinaRobe);
        tezina.setKeyListener(null);
        tezina.setText(r.getTezina());
        TextView kolicina = (TextView) item.findViewById(R.id.kolicinaRobe);
        kolicina.setKeyListener(null);
        kolicina.setText(r.getKolicina());
        TextView napomena = (TextView) item.findViewById(R.id.napomenaRobe);
        napomena.setKeyListener(null);
        napomena.setText(r.getNapomena());

        item.setTag(r.getId());

        item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rezultatiPretrage.addView(item);
    }
}
