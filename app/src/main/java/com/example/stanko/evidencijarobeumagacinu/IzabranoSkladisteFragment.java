package com.example.stanko.evidencijarobeumagacinu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IzabranoSkladisteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IzabranoSkladisteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IzabranoSkladisteFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SKLADISTE = "skladiste";

    private String skladiste;

    private OnFragmentInteractionListener mListener;

    public IzabranoSkladisteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param skladiste Parameter 1.
     * @return A new instance of fragment IzabranoSkladisteFragment.
     */
    public static IzabranoSkladisteFragment newInstance(String skladiste) {
        IzabranoSkladisteFragment fragment = new IzabranoSkladisteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SKLADISTE, skladiste);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            skladiste = getArguments().getString(ARG_SKLADISTE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_izabrano_skladiste, container, false);
        LinearLayout listaIzabranog = v.findViewById(R.id.listaIzabranog);
        popunjavanjeDetalja(skladiste, listaIzabranog);
        return v;
    }

    @SuppressLint("HandlerLeak")
    private void popunjavanjeDetalja(String skladiste, final LinearLayout linearLayout){
        //uklonimo prethodno postojece view-ove
        linearLayout.removeAllViews();
        if(skladiste != null) {
            API.getJSON("http://10.0.2.2:5000/json/" + skladiste, new ReadDataHandler() {
                @Override
                public void handleMessage(Message msg) {
                    String odgovor = getJson();

                    try {
                        JSONArray array = new JSONArray(odgovor);
                        //odgovor spakujemo u JSONArray, i onda parsiramo da bismo dobili linked list
                        final ArrayList<RobaModel> roba = RobaModel.parseJSONArray(array);

                        for (final RobaModel r : roba) {

                            final LinearLayout item = (LinearLayout) getLayoutInflater().inflate(R.layout.roba, null);
                            ImageView icon = item.findViewById(R.id.icon);
                            if (r.getTip().equals("Piće"))
                                icon.setImageResource(R.drawable.drink);
                            else if(r.getTip().equals("Voće"))
                                icon.setImageResource(R.drawable.fruit);
                            else if(r.getTip().equals("Povrće"))
                                icon.setImageResource(R.drawable.vegetables);
                            TextView naziv = (TextView) item.findViewById(R.id.nazivRobe);
                            naziv.setKeyListener(null);
                            naziv.setText("Naziv: " + r.getNaziv());
                            TextView tip = (TextView) item.findViewById(R.id.tipRobe);
                            tip.setKeyListener(null);
                            tip.setText("Tip: " + r.getTip());
                            TextView tezina = (TextView) item.findViewById(R.id.tezinaRobe);
                            tezina.setKeyListener(null);
                            tezina.setText("Težina: " + r.getTezina());
                            final TextView kolicina = (TextView) item.findViewById(R.id.kolicinaRobe);
                            kolicina.setKeyListener(null);
                            kolicina.setText("Količina: " + r.getKolicina());
                            final TextView napomena = (TextView) item.findViewById(R.id.napomenaRobe);
                            napomena.setKeyListener(null);
                            napomena.setText("Napomena: " + r.getNapomena());

                            item.setTag(r.getId());

                            item.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    DialogFragment uklanjanjeRobe = new BrisanjeRobeDialog(linearLayout, item);
                                    Bundle extras = new Bundle();
                                    extras.putString("id", r.getId());
                                    uklanjanjeRobe.setArguments(extras);
                                    uklanjanjeRobe.show(getFragmentManager(), "izlaz");
                                    //getFragmentManager().beginTransaction().detach(IzabranoSkladisteFragment.this).attach(IzabranoSkladisteFragment.this).commit();
                                    return true;
                                }
                            });

                            item.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final EditText kolicinaInput = (EditText)v.findViewById(R.id.izmjenaKolicina);
                                    final EditText napomenaInput = (EditText)v.findViewById(R.id.izmjenaNapomena);
                                    final Button sacuvaj = (Button) v.findViewById(R.id.sacuvajIzmjene);
                                    final Button otkazi = (Button) v.findViewById(R.id.otkaziIzmjene);
                                    final TextView labelaKolicina = v.findViewById(R.id.labelaKolicina);
                                    final TextView labelaNapomena = v.findViewById(R.id.labelaNapomena);
                                    labelaKolicina.setVisibility(View.VISIBLE);
                                    kolicinaInput.setVisibility(View.VISIBLE);
                                    labelaNapomena.setVisibility(View.VISIBLE);
                                    napomenaInput.setVisibility(View.VISIBLE);
                                    sacuvaj.setVisibility(View.VISIBLE);
                                    otkazi.setVisibility(View.VISIBLE);
                                    kolicinaInput.setText(r.getKolicina());
                                    napomenaInput.setText(r.getNapomena());
                                    kolicinaInput.requestFocus();

                                    otkazi.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            labelaKolicina.setVisibility(View.GONE);
                                            kolicinaInput.setVisibility(View.GONE);
                                            labelaNapomena.setVisibility(View.GONE);
                                            napomenaInput.setVisibility(View.GONE);
                                            sacuvaj.setVisibility(View.GONE);
                                            otkazi.setVisibility(View.GONE);
                                        }
                                    });

                                    sacuvaj.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(kolicinaInput.getText().toString().equals(r.getKolicina()) && napomenaInput.getText().toString().equals(r.getNapomena())){
                                                labelaKolicina.setVisibility(View.GONE);
                                                kolicinaInput.setVisibility(View.GONE);
                                                labelaNapomena.setVisibility(View.GONE);
                                                napomenaInput.setVisibility(View.GONE);
                                                sacuvaj.setVisibility(View.GONE);
                                                otkazi.setVisibility(View.GONE);
                                                return;
                                            }
                                            if(kolicinaInput.getText().toString().isEmpty() || kolicinaInput.getText().toString().equals("0")){
                                                kolicinaInput.setText("");
                                                kolicinaInput.requestFocus();
                                                return;
                                            }
                                            int kolicinaI = Integer.parseInt(kolicinaInput.getText().toString());
                                            kolicina.setText("Količina: " + Integer.toString(kolicinaI));
                                            napomena.setText("Napomena: " + napomenaInput.getText().toString());
                                            r.setKolicina(Integer.toString(kolicinaI));
                                            r.setNapomena(napomenaInput.getText().toString());
                                            labelaKolicina.setVisibility(View.GONE);
                                            kolicinaInput.setVisibility(View.GONE);
                                            labelaNapomena.setVisibility(View.GONE);
                                            napomenaInput.setVisibility(View.GONE);
                                            sacuvaj.setVisibility(View.GONE);
                                            otkazi.setVisibility(View.GONE);

                                            List<API.Element> data = new ArrayList<>();
                                            //formiranje objekta
                                            data.add(new API.Element("id", r.getId()));
                                            data.add(new API.Element("kolicina", r.getKolicina()));
                                            data.add(new API.Element("napomena", r.getNapomena()));
                                            API.postDataJSON("http://10.0.2.2:5000/change", data, new ReadDataHandler(){
                                                @Override
                                                public void handleMessage(Message msg) {
                                                    System.out.println(getJson());
                                                }
                                            });
                                            Toast.makeText(getContext(), "Roba je uspiješno izmijenjena!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });

                            linearLayout.addView(item);
                        }
                    } catch (Exception e) {

                    }
                }

            });
        }
        else{
            TextView msg = new TextView(getContext());
            msg.setText("Odaberite jedno skladište");
            msg.setTextSize(18);
            linearLayout.addView(msg);
        }

    }

    public void zamjenaSkladista(String skladiste){
        this.skladiste = skladiste;
        LinearLayout listaIzabranog = getView().findViewById(R.id.listaIzabranog);

        popunjavanjeDetalja(skladiste, listaIzabranog);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
                    //+ " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
