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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.mahd.taxiadmin.Main;
import it.mahd.taxiadmin.R;
import it.mahd.taxiadmin.util.Calculator;
import it.mahd.taxiadmin.util.Controllers;
import it.mahd.taxiadmin.util.ServerRequest;

/**
 * Created by salem on 04/05/16.
 */
public class TaxiProfile extends Fragment {
    SharedPreferences pref;
    Controllers conf = new Controllers();
    ServerRequest sr = new ServerRequest();

    private TextView Model_txt, Serial_txt, Places_txt, Luggages_txt, Date_txt, Pub_txt, DateExp_txt;
    private Button Delete_btn, Renewal_btn;
    private String idTaxi, model, serial, places, luggages, date, pub, dateExp;

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
        pub = getArguments().getString(conf.tag_pub);
        dateExp = getArguments().getString(conf.tag_dateExp);

        Model_txt = (TextView) rootView.findViewById(R.id.Model_txt);
        Serial_txt = (TextView) rootView.findViewById(R.id.Serial_txt);
        Places_txt = (TextView) rootView.findViewById(R.id.Places_txt);
        Luggages_txt = (TextView) rootView.findViewById(R.id.Luggages_txt);
        Date_txt = (TextView) rootView.findViewById(R.id.Date_txt);
        Pub_txt = (TextView) rootView.findViewById(R.id.Pub_txt);
        DateExp_txt = (TextView) rootView.findViewById(R.id.DateExp_txt);
        Delete_btn = (Button) rootView.findViewById(R.id.Delete_btn);
        Renewal_btn = (Button) rootView.findViewById(R.id.Renewal_btn);

        Model_txt.setText(model);
        Serial_txt.setText(serial);
        Places_txt.setText(places + " Places");
        Luggages_txt.setText(luggages + " Luggages");
        Date_txt.setText(date);
        if (pub != "null") {
            String[] strTemp = date.split("/");
            int year = Integer.parseInt(strTemp[0].toString());
            int month = Integer.parseInt(strTemp[1].toString());
            int day = Integer.parseInt(strTemp[2].toString());
            long l[] = new Calculator().getDifference2Dates(new Date(year,month,day),new Date());
            if (l[0] < 500) {
                Renewal_btn.setVisibility(View.VISIBLE);
            }

            Pub_txt.setText(pub);
            DateExp_txt.setText(dateExp);
        } else {
            Pub_txt.setVisibility(View.GONE);
            DateExp_txt.setVisibility(View.GONE);
        }

        Delete_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(conf.tag_id, idTaxi));
                JSONObject json = sr.getJSON(conf.url_deleteTaxi, params);
                if(json != null){
                    try {
                        Toast.makeText(getActivity(), json.getString(conf.response), Toast.LENGTH_SHORT).show();
                        if(json.getBoolean(conf.res)) {
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.container_body, new Taxi());
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Renewal_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(conf.tag_id, idTaxi));
                JSONObject json = sr.getJSON(conf.url_renewalTaxi, params);
                if(json != null){
                    try {
                        Toast.makeText(getActivity(), json.getString(conf.response), Toast.LENGTH_SHORT).show();
                        if(json.getBoolean(conf.res)) {
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.container_body, new Taxi());
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

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
