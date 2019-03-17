package com.example.stanko.evidencijarobeumagacinu;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BrisanjeRobeDialog extends DialogFragment {

    private LinearLayout linearLayout;
    private LinearLayout item;

    @SuppressLint("ValidFragment")
    public BrisanjeRobeDialog(LinearLayout linearLayout, LinearLayout item) {
        this.linearLayout = linearLayout;
        this.item = item;
    }

    public BrisanjeRobeDialog() {
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Uklanjanje robe")
                .setPositiveButton("Da",
                        new DialogInterface.OnClickListener() {
                            @SuppressLint("HandlerLeak")
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Bundle extras = getArguments();
                                List<API.Element> data = new ArrayList<>();
                                //formiranje objekta
                                data.add(new API.Element("id", extras.getString("id")));
                                API.postDataJSON("http://10.0.2.2:5000/delete", data, new ReadDataHandler(){
                                    @Override
                                    public void handleMessage(Message msg) {
                                        System.out.println(getJson());
                                    }
                                });
                                linearLayout.removeView(item);
                                Toast.makeText(getContext(), "Roba je uspiješno uklonjena!", Toast.LENGTH_LONG).show();
                            }
                        })
                .setNegativeButton("Ne",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
        return builder.create();
    }
}
