package it.mahd.taxiadmin.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.mahd.taxiadmin.Main;
import it.mahd.taxiadmin.R;
import it.mahd.taxiadmin.util.Controllers;
import it.mahd.taxiadmin.util.ServerRequest;

/**
 * Created by salem on 04/05/16.
 */
public class TaxiProfile extends Fragment {
    SharedPreferences pref;
    Controllers conf = new Controllers();
    ServerRequest sr = new ServerRequest();

    private TextView Model_txt, Serial_txt, Places_txt, Luggages_txt, Date_txt;
    private String idTaxi, model, serial, places, luggages, date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.taxi_profile, container, false);
        ((Main) getActivity()).getSupportActionBar().setTitle(getString(R.string.taxi_profile));
        pref = getActivity().getSharedPreferences(conf.app, Context.MODE_PRIVATE);

        idTaxi = getArguments().getString(conf.tag_id);
        model = getArguments().getString(conf.tag_model);
        serial = getArguments().getString(conf.tag_serial);
        places = getArguments().getString(conf.tag_places);
        luggages = getArguments().getString(conf.tag_luggages);
        date = getArguments().getString(conf.tag_date);

        Model_txt = (TextView) rootView.findViewById(R.id.Model_txt);
        Serial_txt = (TextView) rootView.findViewById(R.id.Serial_txt);
        Places_txt = (TextView) rootView.findViewById(R.id.Places_txt);
        Luggages_txt = (TextView) rootView.findViewById(R.id.Luggages_txt);
        Date_txt = (TextView) rootView.findViewById(R.id.Date_txt);

        Model_txt.setText(model);
        Serial_txt.setText(serial);
        Places_txt.setText(places + " Places");
        Luggages_txt.setText(luggages + " Luggages");
        Date_txt.setText(date);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container_body, new Taxi());
        ft.addToBackStack(null);
        ft.commit();
        ((Main) getActivity()).getSupportActionBar().setTitle(getString(R.string.taxi));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
