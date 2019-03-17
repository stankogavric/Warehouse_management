package com.example.stanko.evidencijarobeumagacinu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<KorisnikModel> korisnici = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        API.getJSON("http://10.0.2.2:5000/korisnici", new ReadDataHandler(){
            @Override
            public void handleMessage(Message msg) {
                String odgovor = getJson();

                try {
                    JSONArray array = new JSONArray(odgovor);
                    //odgovor spakujemo u JSONArray, i onda parsiramo da bismo dobili linked list
                    korisnici = KorisnikModel.parseJSONArray(array);
                    //postavljamo dobijeni povratni tekst kao tekst labele (ručno formatirano)
                } catch (Exception e) {

                }
            }
        });

        final EditText pinKodUnos = (EditText)findViewById(R.id.pinKod);
        pinKodUnos.setEnabled(false);

        final EditText idUnos = (EditText)findViewById(R.id.idKorisnika);
        idUnos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String id = idUnos.getText().toString();
                if (id.isEmpty())
                    pinKodUnos.setEnabled(false);
                else
                    pinKodUnos.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pinKodUnos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String id = idUnos.getText().toString();
                String pinKodString = pinKodUnos.getText().toString();
                if (pinKodString.isEmpty() || id.isEmpty())
                    return;
                for (KorisnikModel k : korisnici){
                    if (id.equals(k.getId()) && md5(pinKodString).equals(k.getPin())){
                        Toast.makeText(getApplicationContext(), "Dobro došli, " + k.getIme() + " " + k.getPrezime() + "!", Toast.LENGTH_LONG).show();
                        idUnos.setText("");
                        pinKodUnos.setText("");
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public String md5(String s) {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(s.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
