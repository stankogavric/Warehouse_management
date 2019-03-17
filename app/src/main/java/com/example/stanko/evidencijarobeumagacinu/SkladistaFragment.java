package com.example.stanko.evidencijarobeumagacinu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SkladistaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SkladistaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SkladistaFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    public SkladistaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * @return A new instance of fragment SkladistaFragment.
     */
    public static SkladistaFragment newInstance() {
        SkladistaFragment fragment = new SkladistaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = (View) inflater.inflate(R.layout.fragment_skladista, container, false);
        API.getJSON("http://10.0.2.2:5000/json", new ReadDataHandler(){
            @Override
            public void handleMessage(Message msg) {
                String odgovor = getJson();

                try {
                    JSONArray array = new JSONArray(odgovor);
                    //odgovor spakujemo u JSONArray, i onda parsiramo da bismo dobili linked list
                    ArrayList<SkladisteModel> skladista = SkladisteModel.parseJSONArray(array);
                    //postavljamo dobijeni povratni tekst kao tekst labele (ručno formatirano)

                    LinearLayout listaSkladista = (LinearLayout) v.findViewById(R.id.listaSkladista);

                    for (final SkladisteModel s : skladista) {

                        LinearLayout item = (LinearLayout) inflater.inflate(R.layout.skladiste, null);

                        TextView naziv = (TextView) item.findViewById(R.id.naziv);
                        naziv.setKeyListener(null);
                        naziv.setText(s.getNaziv());

                        item.setTag(s.getId());

                        item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mListener.odabranoJeSkladiste(s.getId());
                            }
                        });

                        listaSkladista.addView(item);
                    }

                } catch (Exception e) {

                }
            }
        });

        return v;
    }

    public void onButtonPressed(Uri uri) {

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

    @Override
    public void onClick(View view) {
        String skladiste = (String) view.getTag();

        if(mListener != null){
            mListener.odabranoJeSkladiste(skladiste);
        }
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
        void odabranoJeSkladiste(String skladiste);
    }
}
