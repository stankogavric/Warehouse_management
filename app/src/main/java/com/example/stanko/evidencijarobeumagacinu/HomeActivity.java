package com.example.stanko.evidencijarobeumagacinu;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements SkladistaFragment.OnFragmentInteractionListener{

    private Button dodajRobu;
    private Button pretraga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_home);

            dodajRobu = (Button) findViewById(R.id.dodajRobu);
            dodajRobu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeActivity.this, DodavanjeRobeActivity.class));
                }
            });

            pretraga = findViewById(R.id.pretraga);
            pretraga.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeActivity.this, PretragaActivity.class));
                }
            });

            SkladistaFragment ff = SkladistaFragment.newInstance();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.portraitFrame, ff);
            ft.commit();
        }
        else {
            setContentView(R.layout.activity_home_landscape);
            dodajRobu = (Button) findViewById(R.id.dodajRobu2);
            dodajRobu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeActivity.this, DodavanjeRobeActivity.class));
                }
            });

            pretraga = findViewById(R.id.pretraga2);
            pretraga.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeActivity.this, PretragaActivity.class));
                }
            });
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_home);
            getSupportFragmentManager().beginTransaction().add(R.id.portraitFrame, SkladistaFragment.newInstance()).commit();
        }
        else
            setContentView(R.layout.activity_home_landscape);
    }

    @Override
    public void odabranoJeSkladiste(String skladiste) {
        IzabranoSkladisteFragment pf = (IzabranoSkladisteFragment) getSupportFragmentManager()
                .findFragmentById(R.id.izabranoFragment);

        if(pf != null && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            pf.zamjenaSkladista(skladiste);

        }else{
            IzabranoSkladisteFragment pf1 = IzabranoSkladisteFragment.newInstance(skladiste);

            FragmentTransaction pt = getSupportFragmentManager().beginTransaction();
            pt.replace(R.id.portraitFrame, pf1);
            pt.addToBackStack(null);
            pt.commit();
        }
    }
}
